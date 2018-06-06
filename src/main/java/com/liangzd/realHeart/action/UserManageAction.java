package com.liangzd.realHeart.action;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.druid.util.StringUtils;
import com.liangzd.realHeart.VO.ResponseJson;
import com.liangzd.realHeart.VO.UserImgVo;
import com.liangzd.realHeart.entity.TbUserImg;
import com.liangzd.realHeart.entity.User;
import com.liangzd.realHeart.service.UserImgService;
import com.liangzd.realHeart.service.UserService;
import com.liangzd.realHeart.util.ConstantParams;
import com.liangzd.realHeart.util.MethodUtil;

@RequestMapping(value = "/filter/json")
@Controller
public class UserManageAction {
	private ResponseJson responseJson = new ResponseJson();;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserImgService userImgService;

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

	private static final transient Logger log = LoggerFactory.getLogger(UserManageAction.class);

	/**
	 * 
	 * @Description: 查询所有用户(使用地方1.管理员后台用户管理-显示所有用户)
	 * @param 
	 * @return List<User>
	 * @author liangzd
	 * @date 2018年6月5日 上午12:36:25
	 */
	@ResponseBody
	@RequestMapping(value = "/usersList", method = RequestMethod.GET)
	public List<User> getAllUsers() {
		List<User> users = userService.findAllUsersWithViprankName();
		return users;
	}

	/**
	 * 
	 * @Description: 根据条件查找符合条件的用户(使用地方1.管理员后台用户管理-检索)
	 * @param 
	 * @return List<User>
	 * @author liangzd
	 * @date 2018年6月5日 上午12:36:56
	 */
	@ResponseBody
	@RequestMapping(value = "/usersListWithSearch", method = RequestMethod.GET)
	public List<User> getAllUsersWithSearch(User user) {
		List<User> users = userService.findAllUserWithSearch(user);
		return users;
	}

	/**
	 * 
	 * @Description: 根据UID获取用户(使用地方1.管理员后台用户管理-编辑用户，回显用户信息)
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月5日 上午12:38:21
	 */
	@ResponseBody
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ResponseJson getUser(User user) {
		if (user.getUid() == null) {
			responseJson.setCode(1);
			responseJson.setMsg("获取用户失败-uid为空");
		} else {
			User oldUser = userService.findByIdWithViprankName(user.getUid());
			if (oldUser != null) {
				responseJson.setCode(0);
				responseJson.setMsg(oldUser);
			} else {
				responseJson.setCode(0);
				responseJson.setMsg("获取用户失败-系统中无该用户");
			}
		}
		return responseJson;
	}

	/**
	 * 
	 * @Description: 新增用户(使用地方1.管理员后台用户管理-新增用户)
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月5日 上午12:39:25
	 */
	@ResponseBody
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseJson addUser(User user) {
		user.setCreateTime(new Date(System.currentTimeMillis()));
		user.setPassword(StringUtils.isEmpty(user.getPassword()) ? "202cb962ac59075b964b07152d234b70"
				: MethodUtil.encrypt("MD5", user.getPassword(), null, 1));
		userService.addUser(user);
		responseJson.setCode(0);
		responseJson.setMsg("添加用户成功");
		return responseJson;
	}

	/**
	 * 
	 * @Description: 更新用户(使用地方1.管理员后台用户管理-编辑用户，保存跟新)
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月5日 上午12:38:21
	 */
	@ResponseBody
	@RequestMapping(value = "/user", method = RequestMethod.PUT)
	public ResponseJson updateUser(User user) {
		if (user.getUid() == null) {
			responseJson.setCode(1);
			responseJson.setMsg("更新用户失败-uid为空");
		} else {
			Optional<User> oldUserOpt = userService.findById(user.getUid());
			User oldUser = null;
			if (oldUserOpt != null && oldUserOpt.isPresent()) {
				oldUser = oldUserOpt.get();
				user.setCreateTime(oldUser.getCreateTime());
				user.setIdCard(oldUser.getIdCard());
				user.setNickname(oldUser.getNickname());
				user.setRoleList(oldUser.getRoleList());
				user.setPassword(StringUtils.isEmpty(user.getPassword()) ? "202cb962ac59075b964b07152d234b70"
						: MethodUtil.encrypt("MD5", user.getPassword(), null, 1));
				userService.addUser(user);
				responseJson.setCode(0);
				responseJson.setMsg("更新用户成功");
			} else {
				responseJson.setCode(2);
				responseJson.setMsg("更新用户失败-系统中无该用户");
			}
		}
		return responseJson;
	}

	/**
	 * 
	 * @Description: 根据UID删除用户(使用地方1.管理员后台用户管理-删除用户)
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月5日 上午12:38:21
	 */
	@ResponseBody
	@RequestMapping(value = "/user", method = RequestMethod.DELETE)
	public ResponseJson deleteUser(User user) {
		if (user.getUid() == null) {
			responseJson.setCode(1);
			responseJson.setMsg("删除用户失败-uid为空");
		} else {
			Optional<User> oldUserOpt = userService.findById(user.getUid());
			User oldUser = null;
			if (oldUserOpt != null && oldUserOpt.isPresent()) {
				oldUser = oldUserOpt.get();
				userService.deleteUser(oldUser);
				responseJson.setCode(0);
				responseJson.setMsg("删除用户成功");
			}else {
				responseJson.setCode(2);
				responseJson.setMsg("更新用户失败-系统中无该用户");
			}
		}
		return responseJson;
	}
	
	/**
	 * 
	 * @Description: 根据多个UID批量删除，因为user之间没有相关联系，所以不需要事务控制原子性删除用户(使用地方1.管理员后台用户管理-删除用户)
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年5月28日 上午11:13:29
	 */
	@ResponseBody
	@RequestMapping(value = "/users", method = RequestMethod.DELETE)
	public ResponseJson deleteAllUser(HttpServletRequest request) {
		String uids = request.getParameter("uids") == null ? "" : request.getParameter("uids");
		if(!StringUtils.isEmpty(uids)) {
			String[] uidArray = (uids.indexOf(",") == -1 ? uids : uids.substring(0, uids.length() - 1)).split(",");
			User user = null;
			for(String uid : uidArray) {
				user = new User();
				user.setUid(Integer.parseInt(uid));
				Optional<User> oldUserOpt = userService.findById(user.getUid());
				User oldUser = null;
				if (oldUserOpt != null && oldUserOpt.isPresent()) {
					oldUser = oldUserOpt.get();
					userService.deleteUser(oldUser);
				}else {
					responseJson.setCode(2);
					responseJson.setMsg("删除用户失败-系统中无该用户");
					break;
				}
				responseJson.setCode(0);
				responseJson.setMsg("删除用户成功");
			}
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("批量删除用户失败-无uid");
		}
		return responseJson;
	}
    
	/**
	 * 
	 * @Description: 处理头像的上传 (使用地方1.用户上传头像)
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月4日 下午10:05:32
	 */
	@ResponseBody
	@RequestMapping("/uploadHeadImg")
	public ResponseJson fileUpload(@RequestParam("fileHeadImg") MultipartFile file, HttpServletRequest request) {
		String uid = request.getParameter("uid");
		String increasement = request.getParameter("increasement");
		if (file.isEmpty()) {
			responseJson.setCode(1);
			responseJson.setMsg("文件不能为空");
		}
		String fileName = MethodUtil.saveFile(ConstantParams.LOCAL_HEADIMG_UPLOAD_PATH, file);
		List<TbUserImg> tbUserImgs = new ArrayList<TbUserImg>();
		if(!"false".equals(fileName)) {
			TbUserImg tbUserImg = new TbUserImg();
			tbUserImg.setCreateTime(new java.sql.Date(System.currentTimeMillis()));
			tbUserImg.setImgType(ConstantParams.HEADIMG);
			tbUserImg.setImgUUID(fileName);
			tbUserImg.setUid(Integer.parseInt(uid));
			tbUserImg.setIncreasement(Integer.parseInt(increasement));
			tbUserImgs.add(tbUserImg);
			userImgService.addUserImg(tbUserImgs);
			responseJson.setCode(0);
			responseJson.setMsg("上传头像成功,刷新页面后显示");
		}else {
			responseJson.setCode(2);
			responseJson.setMsg("上传头像失败");
		}
		return responseJson;
	}

	/**
	 * 
	 * @Description: 接收多张背景图片的上传 (使用地方1.用户上传背景图片)
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月4日 下午10:04:05
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadBackgroundImgs", method = RequestMethod.POST)
	public ResponseJson multifileUpload(HttpServletRequest request) {
		String uid = request.getParameter("uid");
		String increasement = request.getParameter("increasement");
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("fileBackground");
		if (files.isEmpty()) {
			responseJson.setCode(1);
			responseJson.setMsg("文件不能为空");
		}
		boolean isFalse = false;
		List<TbUserImg> tbUserImgs = new ArrayList<TbUserImg>();
		for(MultipartFile file : files) {
			String fileName = MethodUtil.saveFile(ConstantParams.LOCAL_BACKGROUNDIMGS_UPLOAD_PATH, file);
			if("false".equals(fileName)) {
				isFalse = true;
				break;
			}else {
				TbUserImg tbUserImg = new TbUserImg();
				tbUserImg.setCreateTime(new java.sql.Date(System.currentTimeMillis()));
				tbUserImg.setImgType(ConstantParams.BACKGROUNDIMGS);
				tbUserImg.setImgUUID(fileName);
				tbUserImg.setUid(Integer.parseInt(uid));
				tbUserImg.setIncreasement(Integer.parseInt(increasement));
				tbUserImgs.add(tbUserImg);
			}
		}
		if(!isFalse) {
			userImgService.addUserImg(tbUserImgs);
			responseJson.setCode(0);
			responseJson.setMsg("上传背景图片成功,刷新页面后显示");
		}else {
			responseJson.setCode(2);
			responseJson.setMsg("上传背景图片失败");
		}
		return responseJson;
	}
	
	/**
	 * 
	 * @Description: 用户完善信息，支持修改(用户名,手机号,邮箱,身份证,性别,个人简介,个人所在地)
	 * (使用地方1.用户完善个人信息)
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月5日 上午12:43:24
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo", method = RequestMethod.PUT)
	public ResponseJson updateUserInfo(HttpServletRequest request, User user) {
		User hasUser = userService.queryByIdentityInfo((String)request.getSession().getAttribute("username"));
		if (hasUser != null) {
			hasUser.setNickname(user.getNickname());
			hasUser.setPhoneNumber(user.getPhoneNumber());
			hasUser.setEmail(user.getEmail());
			hasUser.setIdCard(user.getIdCard());
			hasUser.setSelfIntroduction(user.getSelfIntroduction());
			hasUser.setGender(user.getGender());
			userService.addUser(hasUser);
			responseJson.setCode(0);
			responseJson.setMsg("更新用户成功");
		} else {
			responseJson.setCode(2);
			responseJson.setMsg("更新用户失败-系统中无该用户");
		}
		return responseJson;
	}
	
	@ResponseBody
	@RequestMapping(value = "/heartbeatNext",method = RequestMethod.GET)
    public ResponseJson heartbeatNext(HttpServletRequest request) {
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
		if(userAndimgLists.size() > 0) {
			responseJson.setCode(0);
			responseJson.setMsg(userAndimgLists);
			responseJson.setPageNum(String.valueOf(pageNum));
			responseJson.setPageSize(String.valueOf(pageSize));
			responseJson.setCurrentPageCount(String.valueOf(userAndimgLists.size()-1));
			responseJson.setTotalCount(String.valueOf(totalCount));
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("已无新用户");
		}
        return responseJson;
    }
}
