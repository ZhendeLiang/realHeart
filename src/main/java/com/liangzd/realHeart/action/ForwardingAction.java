package com.liangzd.realHeart.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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
import com.liangzd.realHeart.entity.TbUserChatRecord;
import com.liangzd.realHeart.entity.TbUserImg;
import com.liangzd.realHeart.entity.TbUserRelation;
import com.liangzd.realHeart.entity.TrUserViprank;
import com.liangzd.realHeart.entity.User;
import com.liangzd.realHeart.service.AddressService;
import com.liangzd.realHeart.service.UserChatRecordService;
import com.liangzd.realHeart.service.UserImgService;
import com.liangzd.realHeart.service.UserRelationService;
import com.liangzd.realHeart.service.UserService;
import com.liangzd.realHeart.service.ViprankService;
import com.liangzd.realHeart.util.ConstantParams;
import com.liangzd.realHeart.util.MethodUtil;

/**
 * 
 * @Description: 用于需要校验用户身份的页面跳转
 * @author liangzd
 * @date 2018年6月16日 下午8:05:40
 */
@RequestMapping(value="/filter")
@Controller
public class ForwardingAction {
	@Autowired
	private UserService userService;
	@Autowired
	private UserImgService userImgService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private UserRelationService userRelationService;
	@Autowired
	private ViprankService viprankService;
	@Autowired
	private UserChatRecordService userChatRecordService;
	
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

	public UserRelationService getUserRelationService() {
		return userRelationService;
	}

	public void setUserRelationService(UserRelationService userRelationService) {
		this.userRelationService = userRelationService;
	}

	public ViprankService getViprankService() {
		return viprankService;
	}

	public void setViprankService(ViprankService viprankService) {
		this.viprankService = viprankService;
	}
	
	public UserChatRecordService getUserChatRecordService() {
		return userChatRecordService;
	}

	public void setUserChatRecordService(UserChatRecordService userChatRecordService) {
		this.userChatRecordService = userChatRecordService;
	}

	/**
	 * 
	 * @Description: 用户登陆成功后-跳转到个人主页,获取个人信息(包括头像与用户设置的背景图片)
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月16日 下午8:06:34
	 */
	@RequestMapping(value = "/welcome",method = RequestMethod.GET)
    public String welcome(HttpServletRequest request, Model model) {
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		String identityInfo = (String) request.getSession().getAttribute("username");
		User user = userService.queryByIdentityInfo(identityInfo);
		TrUserViprank userViprank = userService.findUserViprankByUserId(user.getUid());
		user.setViprankName(viprankService.findById(userViprank.getViprankId()).getName());
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
	
	/**
	 * 
	 * @Description: 登陆成功-上传用户头与背景，跳转到用户上传图片的页面
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月16日 下午8:07:39
	 */
	@RequestMapping(value = "/uploadImage",method = RequestMethod.GET)
    public String uploadImage(HttpServletRequest request, Model model) {
		String type = request.getParameter("type");
		String uid = request.getParameter("uid");
		if(!StringUtils.isEmpty(type) && "1".equals(type)) {
			List<TbUserImg> userImgs = userImgService.findByUid(Integer.parseInt(uid));
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
	
	/**
	 * 
	 * @Description: 登陆成功-显示心动列表,查找系统中与该用户尚未确认关系的异性列表
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月16日 下午8:08:30
	 */
	@RequestMapping(value = "/heartbeat",method = RequestMethod.GET)
    public String heartbeat(HttpServletRequest request, Model model) {
		Integer pageNum = StringUtils.isEmpty(request.getParameter("pageNum")) ?
				1 : Integer.parseInt(request.getParameter("pageNum"));
		Integer pageSize = StringUtils.isEmpty(request.getParameter("pageSize")) ?
				10 : Integer.parseInt(request.getParameter("pageSize"));
		User currentUser = userService.queryByIdentityInfo((String) request.getSession().getAttribute("username"));
		List<Integer> uids = userRelationService.findByUid(currentUser.getUid());
		Page<User> users = userService.findByGenderAndState(ConstantParams.TB_USER_GENDER_FEMALE.equals(currentUser.getGender()) ? ConstantParams.TB_USER_GENDER_MALE : ConstantParams.TB_USER_GENDER_FEMALE
				, ConstantParams.TB_USER_STATE_NORMAL, pageNum-1, pageSize, uids);
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
		}
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", String.valueOf(pageSize));
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageCount", String.valueOf(userAndimgLists.size()-1));
		model.addAttribute("heartbeatLists", userAndimgLists);
        return "heartbeat";
    }
	
	/**
	 * 
	 * @Description: 管理员登陆成功,用于跳转到管理的登陆界面
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月16日 下午8:11:04
	 */
	@RequestMapping(value = "/adminWelcome",method = RequestMethod.GET)
    public String adminWelcome() {
        return "adminWelcome";
    }
	
	/**
	 * @TODO 可优化
	 * @Description: 管理员登陆成功,请求一个相对路径的JS文件,用于转发一个JS文件
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月16日 下午8:11:31
	 */
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

	/**
	 * 
	 * @Description: 用户登陆成功，心动列表中，点击图片显示该用户详情，根据当前账号用户的会员等级进行返回信息的过滤
	 * 钻石会员隐藏密码、身份证		白金、黄金会员隐藏密码、身份证、手机号		
	 * 白银、青铜会员隐藏密码、身份证、手机号、真实姓名		普通会员隐藏密码、身份证、手机号、真实姓名、邮箱
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月16日 下午8:11:50
	 */
	@RequestMapping(value = "/personalDetails",method = RequestMethod.GET)
    public String personalDetails(HttpServletRequest request, Model model) {
		String uid = request.getParameter("uid");
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		User user = userService.findById(Integer.parseInt(uid)).get();
		User currentUser = userService.queryByIdentityInfo((String) request.getSession().getAttribute("username"));
		TrUserViprank hasUserViprank = userService.findUserViprankByUserId(currentUser.getUid());
		user.setPassword("");
		user.setIdCard("");
		if(hasUserViprank.getViprankId() == 2 || hasUserViprank.getViprankId() == 3) {
			user.setPhoneNumber(MethodUtil.hidePhoneNumber(user.getPhoneNumber()));
			user.setUsername(MethodUtil.hideName(user.getUsername(), user.getGender()));
		}else if(hasUserViprank.getViprankId() == 4 || hasUserViprank.getViprankId() == 5) {
			user.setPhoneNumber(MethodUtil.hidePhoneNumber(user.getPhoneNumber()));
		}else if(hasUserViprank.getViprankId() == 6) {
		}else {
			user.setEmail(MethodUtil.hideEmail(user.getEmail()));
			user.setPhoneNumber(MethodUtil.hidePhoneNumber(user.getPhoneNumber()));
			user.setUsername(MethodUtil.hideName(user.getUsername(), user.getGender()));
		}
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
		return "personalDetails";
    }

	/**
	 * 
	 * @Description: 登陆成功-显示当前用户的好友列表与第一个用户的聊天记录
	 * @param 
	 * @return String
	 * @author liangzd
	 * @date 2018年6月16日 下午8:16:23
	 */
	@RequestMapping(value = "/userFriendsList",method = RequestMethod.GET)
    public String userFriendsList(HttpServletRequest request, Model model) {
		String identityInfo = (String) request.getSession().getAttribute("username");
		User user = userService.queryByIdentityInfo(identityInfo);
		List<Integer> uids = userRelationService.findUserByUidAndAndRelations(user.getUid(),
				ConstantParams.USER_RELATION_LIKE, ConstantParams.USER_RELATION_LIKE);
		List<User> friendsList = userService.findAllUsersByUid(uids);
		List<User> filterFriendsInfoList = new ArrayList<User>();
		/*Integer lastChatId = 0;*/
		String lastUserChatIds = "";
		StringBuffer lastUserChatIdsBuffer = new StringBuffer();
		for(User friends : friendsList) {
			User friend = new User();
			friend.setUid(friends.getUid());
			friend.setNickname(friends.getNickname());
			List<TbUserImg> friendsHeadImgs = userImgService.findByUidAndImgType(friends.getUid(), ConstantParams.HEADIMG);
			friend.setHeadImgPath((friendsHeadImgs.size() == 0 ? "/img/defaultHeadImg.jpg" : 
				ConstantParams.SERVER_HEADIMG_UPLOAD_PATH + friendsHeadImgs.get(0).getImgUUID()));
			//查找用户朋友列表对应的最后一条聊天记录
			TbUserChatRecord userChat = new TbUserChatRecord();
			userChat.setFromUid(user.getUid());
			userChat.setToUid(friends.getUid());
			TbUserChatRecord lastChatRecord = userChatRecordService.findLatestByFromUidAndToUid(userChat);
			if(lastChatRecord == null) {
				String defaultChat = "快点来开始聊天吧。";
				friend.setLastUserChat(defaultChat);
				//查找对应用户关系的对象，用于获取成为朋友的时间
				Optional<TbUserRelation> userRelation = userRelationService.findByUidAndTargetUid(user.getUid(), friends.getUid());
				friend.setLastUserChatTime(userRelation.isPresent() ? userRelation.get().getCreateTime() : new Timestamp(System.currentTimeMillis()));
				lastUserChatIdsBuffer.append(friend.getUid()+":0;");
			}else {
				friend.setLastUserChat(lastChatRecord.getChatRecode());
				friend.setLastUserChatTime(lastChatRecord.getLastChatTime());
				/*lastChatId = lastChatRecord.getId() > lastChatId ? lastChatRecord.getId() : lastChatId;*/
				lastUserChatIdsBuffer.append(friend.getUid()+":"+lastChatRecord.getId()+";");
			}
			filterFriendsInfoList.add(friend);
		}
		//根据时间-用户uid排序
		Collections.sort(filterFriendsInfoList);
		String toUserImgPath = "";
		String fromUserImgPath = "";
		List<TbUserImg> fromUidHeadImgs = userImgService.findByUidAndImgType(user.getUid(), ConstantParams.HEADIMG);
		fromUserImgPath = fromUidHeadImgs.size() == 0 ? "/img/defaultHeadImg.jpg" : 
			ConstantParams.SERVER_HEADIMG_UPLOAD_PATH + fromUidHeadImgs.get(0).getImgUUID();
		//获取用户
		if(filterFriendsInfoList.size() > 0) {
			Integer toUid = filterFriendsInfoList.get(0).getUid();
			//获取用户头像
			List<TbUserImg> toUidHeadImgs = userImgService.findByUidAndImgType(toUid, ConstantParams.HEADIMG);
			toUserImgPath = toUidHeadImgs.size() == 0 ? "/img/defaultHeadImg.jpg" : 
				ConstantParams.SERVER_HEADIMG_UPLOAD_PATH + toUidHeadImgs.get(0).getImgUUID();
			TbUserChatRecord userChat = new TbUserChatRecord();
			userChat.setFromUid(user.getUid());
			userChat.setToUid(toUid);
			List<TbUserChatRecord> allChatRecords = userChatRecordService.findAllByFromUidAndToUid(userChat);
			model.addAttribute("currentFriendsChatAllChatRecords", allChatRecords);
			model.addAttribute("currentUserUid", user.getUid());
		}
		lastUserChatIds = lastUserChatIdsBuffer.indexOf(";") != -1 ? 
				lastUserChatIdsBuffer.substring(0, lastUserChatIdsBuffer.length()-1).toString() : lastUserChatIdsBuffer.toString();
		model.addAttribute("chatFlushTime", ConstantParams.CHATFLUSHTIME);
		model.addAttribute("lastChatIds", lastUserChatIds);
		model.addAttribute("toUserImgPath", toUserImgPath);
		model.addAttribute("fromUserImgPath", fromUserImgPath);
		model.addAttribute("friendsList", filterFriendsInfoList);
        return "userFriendsList";
    }
}
