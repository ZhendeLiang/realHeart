package com.liangzd.realHeart.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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
import com.liangzd.realHeart.VO.UserComparableVo;
import com.liangzd.realHeart.VO.UserEntityVo;
import com.liangzd.realHeart.VO.UserImgVo;
import com.liangzd.realHeart.entity.TbUserChatRecord;
import com.liangzd.realHeart.entity.TbUserImg;
import com.liangzd.realHeart.entity.TbUserRelation;
import com.liangzd.realHeart.entity.User;
import com.liangzd.realHeart.service.UserChatRecordService;
import com.liangzd.realHeart.service.UserImgService;
import com.liangzd.realHeart.service.UserRelationService;
import com.liangzd.realHeart.service.UserService;
import com.liangzd.realHeart.util.ConstantParams;
import com.liangzd.realHeart.util.MethodUtil;

@RequestMapping(value = "/filter/json")
@Controller
public class UserManageAction {
	private ResponseJson responseJson = new ResponseJson();

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserImgService userImgService;

	@Autowired
	private UserRelationService userRelationService;
	
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

	public UserRelationService getUserRelationService() {
		return userRelationService;
	}

	public void setUserRelationService(UserRelationService userRelationService) {
		this.userRelationService = userRelationService;
	}

	public UserChatRecordService getUserChatRecordService() {
		return userChatRecordService;
	}

	public void setUserChatRecordService(UserChatRecordService userChatRecordService) {
		this.userChatRecordService = userChatRecordService;
	}

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
		user.setCreateTime(new Timestamp(System.currentTimeMillis()));
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
	
	/**
	 * 
	 * @Description: 分页显示当前用户的心动对象
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午8:24:46
	 */
	@ResponseBody
	@RequestMapping(value = "/heartbeatNext",method = RequestMethod.GET)
    public ResponseJson heartbeatNext(HttpServletRequest request) {
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
	

	/**
	 * 
	 * @Description:处理当前用户与目标好友的关系,0为喜欢,1为不喜欢,2为未确认关系
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月7日 上午12:56:52
	 */
	@ResponseBody
	@RequestMapping(value = "/sendUserRelation", method = RequestMethod.POST)
	public ResponseJson sendUserRelation(HttpServletRequest request) {
		String relation = request.getParameter("relation");
		if(!StringUtils.isEmpty(relation) && !StringUtils.isEmpty(request.getParameter("targetUid"))) {
			Integer targetUid = Integer.parseInt(request.getParameter("targetUid"));
			User hasUser = userService.queryByIdentityInfo((String)request.getSession().getAttribute("username"));
			if (hasUser != null) {
				TbUserRelation tbUserRelation = new TbUserRelation();
				tbUserRelation.setFirstUid(hasUser.getUid());
				tbUserRelation.setFirstUserRelation(relation);
				tbUserRelation.setSecondUid(targetUid);
				tbUserRelation.setCreateTime(new Timestamp(System.currentTimeMillis()));
				userRelationService.saveUserRelation(tbUserRelation);
				responseJson.setCode(0);
				responseJson.setMsg("0".equals(relation) ? 
						"如果对方喜欢你的话,就会出现在你的好友列表里面哦" : "next one~ next one~");
			}else {
				responseJson.setCode(2);
				responseJson.setMsg("当前登陆用户异常");
			}
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("关系不明确或对象找不到");
		}
		return responseJson;
	}

	/**
	 * 
	 * @Description: 显示当前用户的好友列表
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午8:25:58
	 */
	@ResponseBody
	@RequestMapping(value = "/userFriendsListJson",method = RequestMethod.GET)
    public ResponseJson userFriendsListJson(HttpServletRequest request) {
		String searchKey = request.getParameter("searchKey");
		String identityInfo = (String) request.getSession().getAttribute("username");
		User user = userService.queryByIdentityInfo(identityInfo);
		List<Integer> uids = userRelationService.findUserByUidAndAndRelations(user.getUid(),
				ConstantParams.USER_RELATION_LIKE, ConstantParams.USER_RELATION_LIKE);
		List<User> friendsList = userService.findAllUsersByUid(uids);
		List<UserEntityVo> filterFriendsInfoList = new ArrayList<UserEntityVo>();
		for(User friends : friendsList) {
			UserEntityVo friend = new UserEntityVo();
			if(!StringUtils.isEmpty(friends.getNickname()) && friends.getNickname().contains(searchKey)) {
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
				}else {
					friend.setLastUserChat(lastChatRecord.getChatRecode());
					friend.setLastUserChatTime(lastChatRecord.getLastChatTime());
				}
				filterFriendsInfoList.add(friend);
			}
		}
		//根据时间-用户uid排序
		Collections.sort(filterFriendsInfoList);
		if(filterFriendsInfoList.size() == 0) {
			responseJson.setCode(1);
			responseJson.setMsg("无对应用户");
		}else{
			Integer toUid = filterFriendsInfoList.get(0).getUid();
			TbUserChatRecord userChat = new TbUserChatRecord();
			userChat.setFromUid(user.getUid());
			userChat.setToUid(toUid);
			List<TbUserChatRecord> allChatRecords = userChatRecordService.findAllByFromUidAndToUid(userChat);
			List<TbUserImg> fromUidHeadImgs = userImgService.findByUidAndImgType(user.getUid(), ConstantParams.HEADIMG);
			List<TbUserImg> toUidHeadImgs = userImgService.findByUidAndImgType(toUid, ConstantParams.HEADIMG);
			if(allChatRecords.size() != 0) {
				allChatRecords.get(0).setFromUserImgPath((fromUidHeadImgs.size() == 0 ? "/img/defaultHeadImg.jpg" : 
					ConstantParams.SERVER_HEADIMG_UPLOAD_PATH + fromUidHeadImgs.get(0).getImgUUID()));
				allChatRecords.get(0).setToUserImgPath((toUidHeadImgs.size() == 0 ? "/img/defaultHeadImg.jpg" : 
					ConstantParams.SERVER_HEADIMG_UPLOAD_PATH + toUidHeadImgs.get(0).getImgUUID()));
			}
			filterFriendsInfoList.get(0).setUserChatRecords(allChatRecords);
			responseJson.setCode(0);
			responseJson.setMsg(filterFriendsInfoList);
		}
        return responseJson;
    }
	
	/**
	 * 
	 * @Description: 查找当前用户的聊天记录
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午8:26:25
	 */
	@ResponseBody
	@RequestMapping(value = "/queryUserChatRecord",method = RequestMethod.GET)
    public ResponseJson queryUserChatRecord(HttpServletRequest request) {
		Integer toUid = Integer.parseInt(request.getParameter("toUid"));
		String identityInfo = (String) request.getSession().getAttribute("username");
		User user = userService.queryByIdentityInfo(identityInfo);
		TbUserChatRecord userChat = new TbUserChatRecord();
		userChat.setFromUid(user.getUid());
		userChat.setToUid(toUid);
		List<TbUserChatRecord> allChatRecords = userChatRecordService.findAllByFromUidAndToUid(userChat);
		List<TbUserImg> fromUidHeadImgs = userImgService.findByUidAndImgType(user.getUid(), ConstantParams.HEADIMG);
		List<TbUserImg> toUidHeadImgs = userImgService.findByUidAndImgType(toUid, ConstantParams.HEADIMG);
		if(allChatRecords.size() != 0) {
			allChatRecords.get(0).setFromUserImgPath((fromUidHeadImgs.size() == 0 ? "/img/defaultHeadImg.jpg" : 
				ConstantParams.SERVER_HEADIMG_UPLOAD_PATH + fromUidHeadImgs.get(0).getImgUUID()));
			allChatRecords.get(0).setToUserImgPath((toUidHeadImgs.size() == 0 ? "/img/defaultHeadImg.jpg" : 
				ConstantParams.SERVER_HEADIMG_UPLOAD_PATH + toUidHeadImgs.get(0).getImgUUID()));
			responseJson.setCode(0);
			responseJson.setMsg(allChatRecords);
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("暂无聊天记录");
		}
		//查找聊天记录
        return responseJson;
    }


	/**
	 * 
	 * @Description: 个人主页-好友聊天,保存该用户与好友的聊天记录
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午8:00:46
	 */
	@ResponseBody
	@RequestMapping(value = "/saveUserChat",method = RequestMethod.POST)
    public ResponseJson saveUserChat(HttpServletRequest request) {
		try {
			request.setCharacterEncoding("utf-8");
			Integer toUid = Integer.parseInt(request.getParameter("toUid"));
			String chatRecode = request.getParameter("chatRecode");
			String identityInfo = (String) request.getSession().getAttribute("username");
			User user = userService.queryByIdentityInfo(identityInfo);
			TbUserChatRecord userChatRecord = new TbUserChatRecord();
			userChatRecord.setFromUid(user.getUid());
			userChatRecord.setToUid(toUid);
			userChatRecord.setChatRecode(chatRecode);
//			userChatRecord.setType(0);
			userChatRecord.setLastChatTime(new Timestamp(System.currentTimeMillis()));
			TbUserChatRecord saveUserChat = userChatRecordService.saveUserChat(userChatRecord);
			responseJson.setCode(0);
			responseJson.setMsg(saveUserChat);
		} catch (Exception e) {
			e.printStackTrace();
			responseJson.setCode(1);
			responseJson.setMsg("系统错误");
		}
		return responseJson;
    }

	/**
	 * 
	 * @Description: 查找当前用户的聊天记录
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午8:26:25
	 */
	@ResponseBody
	@RequestMapping(value = "/queryLatestUserChatRecord",method = RequestMethod.GET)
    public ResponseJson queryLatestUserChatRecord(HttpServletRequest request) {
		String lastChatIdStr = request.getParameter("lastChatIds");
		if(!StringUtils.isEmpty(lastChatIdStr) && !"".equals(lastChatIdStr)) {
			String identityInfo = (String) request.getSession().getAttribute("username");
			User user = userService.queryByIdentityInfo(identityInfo);
			String[] lastUidAndChatIdArray = lastChatIdStr.split(";");
			List<UserComparableVo> latestUserChat = new ArrayList<UserComparableVo>();
			for(String lastUidAndChatId : lastUidAndChatIdArray) {
				String[] params = lastUidAndChatId.split(":");
				UserComparableVo latestUser = new UserComparableVo();
				TbUserChatRecord userChat = new TbUserChatRecord();
				userChat.setFromUid(user.getUid());
				userChat.setToUid(Integer.parseInt(params[0]));
				userChat.setId(Integer.parseInt(params[1]));
				List<TbUserChatRecord> allChatRecords = userChatRecordService.findAllLatestChatByFromUidAndToUid(userChat);
				latestUser.setUid(Integer.parseInt(params[0]));
				latestUser.setUserChatRecords(allChatRecords);
				latestUser.setNewsCount(allChatRecords.size());
				latestUserChat.add(latestUser);
			}
			Collections.sort(latestUserChat);
			if(lastUidAndChatIdArray.length > 0) {
				responseJson.setCode(0);
				responseJson.setMsg(latestUserChat);
			}else {
				responseJson.setCode(1);
				responseJson.setMsg("暂无聊天用户");
			}
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("暂无聊天用户");
		}
		//查找聊天记录
        return responseJson;
    }
}
