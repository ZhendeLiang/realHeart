package com.liangzd.realHeart.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.liangzd.realHeart.VO.UserImgVo;
import com.liangzd.realHeart.entity.TbAddress;
import com.liangzd.realHeart.entity.TbNationalProvinceCityTown;
import com.liangzd.realHeart.entity.TbUserImg;
import com.liangzd.realHeart.entity.User;
import com.liangzd.realHeart.service.AddressService;
import com.liangzd.realHeart.service.UserImgService;
import com.liangzd.realHeart.service.UserService;
import com.liangzd.realHeart.util.ConstantParams;
import com.liangzd.realHeart.util.MethodUtil;

@RequestMapping(value="/filter")
@Controller
public class ForwardingAction {
	@Autowired
	private UserService userService;
	@Autowired
	private UserImgService userImgService;
	@Autowired
	private AddressService addressService;
	
    private static final transient Logger log = LoggerFactory.getLogger(ForwardingAction.class);

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserImgService getUserImgService() {
		return userImgService;
	}

	public void setUserImgService(UserImgService userImgService) {
		this.userImgService = userImgService;
	}
	
	public AddressService getAddressService() {
		return addressService;
	}

	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	@RequestMapping(value = "/welcome",method = RequestMethod.GET)
    public String welcome(HttpServletRequest request, Model model) {
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		String identityInfo = (String) request.getSession().getAttribute("username");
		User user = userService.queryByIdentityInfo(identityInfo);
		user.setPassword("");
		List<TbUserImg> userImgs = userImgService.findByUid(user.getUid());
		List<String> backgroundImgs = new ArrayList<String> ();
		for(TbUserImg userImg : userImgs) {
			if(!StringUtils.isEmpty(userImg.getImgType())) {
				if("0".equals(userImg.getImgType())) {
					model.addAttribute("userHeadImg", ConstantParams.SERVER_HEADIMG_UPLOAD_PATH+userImg.getImgUUID());
				}else {
					backgroundImgs.add(basePath+ConstantParams.SERVER_BACKGROUNDIMGS_UPLOAD_PATH+userImg.getImgUUID());
				}
			}
		}
		model.addAttribute("userInfo", user);
		model.addAttribute("userBackgroundImgs", backgroundImgs);
		model.addAttribute("uploadImageAndParams", "uploadImage?type=1&uid="+user.getUid());
		return "welcome";
    }
	
	@RequestMapping(value = "/uploadImage",method = RequestMethod.GET)
    public String uploadImage(HttpServletRequest request, Model model) {
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		String type = request.getParameter("type");
		String uid = request.getParameter("uid");
		if(!StringUtils.isEmpty(type) && "1".equals(type)) {
			List<TbUserImg> userImgs = userImgService.findByUid(Integer.parseInt(uid));
			/*String backgroundImgs = "";
			for(TbUserImg userImg : userImgs) {
				if(!StringUtils.isEmpty(userImg.getImgType())) {
					if("0".equals(userImg.getImgType())) {
						model.addAttribute("userHeadImg", ConstantParams.SERVER_HEADIMG_UPLOAD_PATH+userImg.getImgUUID());
					}else {
						backgroundImgs += ConstantParams.SERVER_BACKGROUNDIMGS_UPLOAD_PATH+userImg.getImgUUID() +"\",\"";
					}
				}
			}
			
			System.out.println(backgroundImgs.substring(0, backgroundImgs.length()-3));
			model.addAttribute("userBackgroundImgs", backgroundImgs.substring(0, backgroundImgs.length()-3));
			*/
			List<String> backgroundImgs = new ArrayList<String> ();
			Integer increasement = 0;
			for(TbUserImg userImg : userImgs) {
				if(!StringUtils.isEmpty(userImg.getImgType())) {
					if("0".equals(userImg.getImgType())) {
						model.addAttribute("userHeadImg", ConstantParams.SERVER_HEADIMG_UPLOAD_PATH+userImg.getImgUUID());
					}else {
						backgroundImgs.add(ConstantParams.SERVER_BACKGROUNDIMGS_UPLOAD_PATH+userImg.getImgUUID());
					}
					increasement = userImg.getIncreasement();
				}
			}
			Optional<TbAddress> userAddress = addressService.findByUid(Integer.parseInt(uid));
			List<TbNationalProvinceCityTown> provinces = addressService.findAllByParentCode("0");
			if(userAddress.isPresent()) {
				model.addAttribute("address", userAddress.get());
			}else {
				model.addAttribute("address", "");
			}
			model.addAttribute("provinces", provinces);
			//获取省市区信息
			model.addAttribute("increasement", String.valueOf(increasement == null ? 0 : (increasement+1)));
			model.addAttribute("uid", uid);
			model.addAttribute("userBackgroundImgs", backgroundImgs);
		}
        return "uploadHeadImg";
    }
	
	@RequestMapping(value = "/heartbeat",method = RequestMethod.GET)
    public String heartbeat(HttpServletRequest request, Model model) {
		Integer pageNum = StringUtils.isEmpty(request.getParameter("pageNum")) ?
				1 : Integer.parseInt(request.getParameter("pageNum"));
		Integer pageSize = StringUtils.isEmpty(request.getParameter("pageSize")) ?
				10 : Integer.parseInt(request.getParameter("pageSize"));
		User currentUser = userService.queryByIdentityInfo((String) request.getSession().getAttribute("username"));
		Page<User> users = userService.findByGenderAndState(currentUser.getGender() == ConstantParams.TB_USER_GENDER_FEMALE ? ConstantParams.TB_USER_GENDER_MALE : ConstantParams.TB_USER_GENDER_FEMALE
				, ConstantParams.TB_USER_STATE_NORMAL, pageNum-1, pageSize);
		long totalCount = users.getTotalElements();
		List<UserImgVo> userAndimgLists = new ArrayList<UserImgVo>();
		UserImgVo userImg = null;
		for(User user : users) {
			List<TbUserImg> userImgs = userImgService.findByUidAndImgType(user.getUid(), ConstantParams.HEADIMG);
			userImg = new UserImgVo();
			userImg.setUid(user.getUid());
			userImg.setNickname(user.getNickname());
			userImg.setHeadImg((userImgs.size() == 0 ? "/img/defaultHeadImg.jpg" : 
				ConstantParams.SERVER_HEADIMG_UPLOAD_PATH + userImgs.get(0).getImgUUID()));
			userAndimgLists.add(userImg);
			System.out.println(user.getUid());
		}
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", String.valueOf(pageSize));
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageCount", String.valueOf(userAndimgLists.size()-1));
		model.addAttribute("heartbeatLists", userAndimgLists);
        return "heartbeat";
    }
	
	@RequestMapping(value = "/friendsAndChat",method = RequestMethod.GET)
    public String friendsAndChat() {
        return "friendsAndChat";
    }

	@RequestMapping(value = "/adminWelcome",method = RequestMethod.GET)
    public String adminWelcome() {
        return "adminWelcome";
    }
	
	@ResponseBody
	@RequestMapping(value = "/js/bodyTab.js",method = RequestMethod.GET)
    public String queryJs1() {
		String result = "";
		try {
			result = MethodUtil.readFileToString("classpath:bodyTab.js");
		} catch (IOException e) {
			e.printStackTrace();
		}
        return result;
    }
}
