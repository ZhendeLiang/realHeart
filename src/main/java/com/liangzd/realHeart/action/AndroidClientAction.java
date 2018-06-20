package com.liangzd.realHeart.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liangzd.realHeart.VO.EmailVo;
import com.liangzd.realHeart.VO.ResponseJson;
import com.liangzd.realHeart.VO.SuccJson;
import com.liangzd.realHeart.VO.TestPhone;
import com.liangzd.realHeart.VO.UserImgVo;
import com.liangzd.realHeart.VO.UserVo;
import com.liangzd.realHeart.entity.TbUserChatRecord;
import com.liangzd.realHeart.entity.TbUserImg;
import com.liangzd.realHeart.entity.TbUserRelation;
import com.liangzd.realHeart.entity.TbViprank;
import com.liangzd.realHeart.entity.TrUserViprank;
import com.liangzd.realHeart.entity.User;
import com.liangzd.realHeart.realm.CustomizedToken;
import com.liangzd.realHeart.service.UserChatRecordService;
import com.liangzd.realHeart.service.UserImgService;
import com.liangzd.realHeart.service.UserRelationService;
import com.liangzd.realHeart.service.UserService;
import com.liangzd.realHeart.service.ViprankService;
import com.liangzd.realHeart.util.ConstantParams;
import com.liangzd.realHeart.util.LoginType;
import com.liangzd.realHeart.util.MethodUtil;
import com.liangzd.verifyCode.image.ImageVerifyCode;

/**
 * 
 * @Description: 处理Android Client端的数据请求
 * @author liangzd
 * @date 2018年6月16日 下午7:27:03
 */
@Controller
public class AndroidClientAction {
	//日志属性
	private static final transient Logger log = LoggerFactory.getLogger(LoginAction.class);
	//用于总体返回Json数据格式的定义对象
	private ResponseJson responseJson = new ResponseJson();
	@Autowired
	private UserService userService;
	@Autowired
	private ViprankService viprankService;
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

	public ViprankService getViprankService() {
		return viprankService;
	}

	public void setViprankService(ViprankService viprankService) {
		this.viprankService = viprankService;
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
	 * @Description: 客户端登陆,处理Android Client的登录请求
	 * @param
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年5月26日 上午2:05:12
	 */
	@ResponseBody
	@RequestMapping(value = "/androidLogin", method = RequestMethod.POST)
	public ResponseJson androidLogin(HttpServletRequest request, User user) {
		//获取当前连接请求的主体对象
		Subject currentUser = SecurityUtils.getSubject();
		responseJson = new ResponseJson();
		CustomizedToken token = null;
		//判断当前请求的主体对象是否已经登陆
		if (!currentUser.isAuthenticated()) {
			try {
				//创建自定义的token
				token = new CustomizedToken(user.getUsername(), user.getPassword(), LoginType.USER.toString());
				//主体对象登陆
				currentUser.login(token);
				//设置返回值的code类型 0为成功
				responseJson.setCode(0);
				//打印日志
				log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
				//讲Android Client传送过来的AppToken存储，用于当前客户端的登陆对应用户
				ConstantParams.ANDROID_PARAMS.put(request.getParameter("apptoken"),token.getPrincipal());
				//返回登陆成功的用户名
				responseJson.setMsg(token.getPrincipal());
				//暂无用处
				responseJson.setToken(UUID.randomUUID().toString());
			} catch (UnknownAccountException uae) {
				//用户账号、用户名、手机号、邮箱在系统中并不存在 ,返回code = 1
				responseJson.setCode(1);
				log.info(token.getPrincipal() + "未注册");
				responseJson.setMsg(token.getPrincipal() + "未注册");
			} catch (IncorrectCredentialsException ice) {
				//用户账号与密码不匹配,返回code = 2
				responseJson.setCode(2);
				log.info(token.getPrincipal() + "密码不正确");
				responseJson.setMsg(token.getPrincipal() + "密码不正确");
			} catch (LockedAccountException lae) {
				//系统抛出其他错误
				log.info("The account for username " + token.getPrincipal() + " is locked.  "
						+ "Please contact your administrator to unlock it.");
				responseJson.setCode(3);
				responseJson.setMsg("系统错误");
			}
		}
		//返回Json数据
		return responseJson;
	}

	/**
	 * 
	 * @Description: 客户端手机注册账号、手机/邮箱找回密码,用户获取图片验证码
	 * @param
	 * @return void
	 * @author liangzd
	 * @date 2018年5月31日 下午4:42:03
	 */
	@RequestMapping(value = "/verifyCodeAndroid", method = RequestMethod.GET)
	public void verifyCodeAndroid(HttpServletRequest request, HttpServletResponse response) {
		//获取Android Client来的AppToken
		String apptoken = request.getParameter("apptoken");
		ImageVerifyCode vCode = new ImageVerifyCode(100, 30, 4);
		ConstantParams.ANDROID_PARAMS.remove(apptoken + "verifyCode");
		//存储当前Android Client来的AppToken对应的图片验证码的值
		ConstantParams.ANDROID_PARAMS.put(apptoken + "verifyCode", vCode.getCode());
		try {
			//向Android Client返回验证码图片
			vCode.write(response.getOutputStream());
		} catch (IOException e) {
			log.error("验证码生成错误");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description: 客户端手机注册账号、手机/邮箱找回密码,校验Android Client的验证码值是否正确
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午7:39:53
	 */
	@ResponseBody
	@RequestMapping(value = "/checkImageVerifyCodeAndroid", method = RequestMethod.POST)
	public ResponseJson checkImageVerifyCodeAndroid(HttpServletRequest request, UserVo user) {
		String apptoken = request.getParameter("apptoken");
		//获取当前Android Client的验证码值
		String correctVerifyCode = (String) ConstantParams.ANDROID_PARAMS.get(apptoken + "verifyCode");
		//用户传递的验证码值不能为空 && 接收到的验证码值与服务器中存储的验证码值转为小写并进行判断
		if (!StringUtils.isEmpty(user.getVeri())
				&& correctVerifyCode.toLowerCase().equals(user.getVeri().toLowerCase())) {
			responseJson.setCode(0);
			responseJson.setMsg("验证成功");
		} else {
			responseJson.setCode(1);
			responseJson.setMsg("验证码输入错误");
		}
		return responseJson;
	}

	/**
	 * 
	 * @Description:客户端手机注册账号、手机找回密码,先判断验证码值是否正确，再根据手机号获取手机验证码
	 * @param
	 * @return void
	 * @author liangzd
	 * @date 2018年5月31日 下午4:47:39
	 */
	@ResponseBody
	@RequestMapping(value = "/getPhoneVerifyCodeAndroid", method = RequestMethod.POST)
	public ResponseJson getPhoneVerifyCodeAndroid(HttpServletRequest request, UserVo user) {
		String apptoken = request.getParameter("apptoken");
		String correctVerifyCode = (String) ConstantParams.ANDROID_PARAMS.get(apptoken + "verifyCode");
		//判断验证码值是否正确
		if (!StringUtils.isEmpty(user.getVeri())
				&& correctVerifyCode.toLowerCase().equals(user.getVeri().toLowerCase())) {
			if (!StringUtils.isEmpty(user.getPhone())) {
				try {
					//暂时关闭接口调用，剩余短信条目不足
					// String result = MethodUtil.sendPhoneVerifyCode(user.getPhone());
					//模拟发送短信验证码
					String result = JSONObject.toJSONString(new TestPhone(200, "已关闭短信功能，测试验证码1118", "1118"));
					@SuppressWarnings("unchecked")
					Map<String, Object> resultMap = (Map<String, Object>) JSON.parse(result);
					//校验发送短信后的返回值是否为200
					if (resultMap.get("code") instanceof Integer && (Integer) resultMap.get("code") == 200) {
						responseJson.setCode(0);
						ConstantParams.ANDROID_PARAMS.remove(apptoken + user.getPhone());
						ConstantParams.ANDROID_PARAMS.remove(apptoken + user.getPhone() + "_time");
						//存储当前Android Client的手机验证码与时间
						ConstantParams.ANDROID_PARAMS.put(apptoken + user.getPhone(), resultMap.get("obj"));
						ConstantParams.ANDROID_PARAMS.put(apptoken + user.getPhone() + "_time",
								System.currentTimeMillis());
					} else {
						responseJson.setCode(2);
					}
					responseJson.setMsg(resultMap.get("msg"));
				} catch (Exception e) {
					e.printStackTrace();
					responseJson.setCode(3);
					responseJson.setMsg("系统异常");
				}
			} else {
				responseJson.setCode(4);
				responseJson.setMsg("手机号不能为空");
			}
		} else {

			responseJson.setCode(1);
			responseJson.setMsg("验证码输入错误");
		}
		return responseJson;
	}

	/**
	 * 
	 * @Description: 客户端手机注册账号、手机找回密码,判断Android Client的手机验证码是否正确
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午7:47:19
	 */
	@ResponseBody
	@RequestMapping(value = "/checkPhoneVerifyCodeAndroid", method = RequestMethod.POST)
	public ResponseJson checkPhoneVerifyCodeAndroid(HttpServletRequest request, UserVo user) {
		String phone = user.getPhone();
		String veriCode = user.getVeriCode();
		String apptoken = request.getParameter("apptoken");
		if (!StringUtils.isEmpty((String) ConstantParams.ANDROID_PARAMS.get(apptoken + phone))
				&& ((String) ConstantParams.ANDROID_PARAMS.get(apptoken + phone)).equals(veriCode)) {
			responseJson.setCode(0);
			responseJson.setMsg("手机验证码正确");
		} else {
			responseJson.setCode(1);
			responseJson.setMsg("手机验证码错误");
		}
		return responseJson;
	}

	/**
	 * 
	 * @Description: 客户端手机注册账号,处理Android Client的注册，先校验当前手机号是否存在，在保存当前的注册对象
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午7:48:15
	 */
	@ResponseBody
	@RequestMapping(value = "/registActionAndroid", method = RequestMethod.POST)
	public ResponseJson registActionAndroid(HttpServletRequest request, UserVo user) {
		String phone = user.getPhone();
		String password = user.getPassword();
		Optional<User> hasUser = userService.findByPhoneNumber(phone);
		User addUser = new User();
		if (!hasUser.isPresent()) {
			addUser.setUsername(phone);
			addUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
			addUser.setGender("1");
			addUser.setViprankName("1");
			addUser.setPhoneNumber(phone);
			addUser.setState((byte) 1);
			addUser.setPassword(StringUtils.isEmpty(password) ? "202cb962ac59075b964b07152d234b70"
					: MethodUtil.encrypt("MD5", password, null, 1));
			userService.addUser(addUser);
			ConstantParams.ANDROID_PARAMS.put(request.getParameter("apptoken"),phone);
			responseJson.setCode(0);
			responseJson.setMsg(addUser.getUsername());
		} else {
			responseJson.setCode(1);
			responseJson.setMsg("手机号已存在");
		}
		return responseJson;
	}

	/**
	 * 
	 * @Description: 客户端手机/邮箱找回密码,重置密码的校验，
	 * 1.手机号重置密码
	 * 		判断当前Android Client的手机验证码是否正确，再判断手机号是否存在
	 * 2.邮箱重置密码
	 * 		判断此邮箱是否存在，存在的话，給该邮箱发送邮件，在邮箱中点击连接重置密码
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午7:50:02
	 */
	@ResponseBody
	@RequestMapping(value = "/resetVerifyAndroid", method = RequestMethod.POST)
	public ResponseJson resetVerifyAndroid(HttpServletRequest request, UserVo user) {
		if ("phone".equals(user.getType())) {
			String phone = user.getPhone();
			String veriCode = user.getVeriCode();
			String apptoken = request.getParameter("apptoken");
			if (!StringUtils.isEmpty((String) ConstantParams.ANDROID_PARAMS.get(apptoken + phone))
					&& ((String) ConstantParams.ANDROID_PARAMS.get(apptoken + phone)).equals(veriCode)) {
				Optional<User> hasUser = userService.findByPhoneNumber(user.getPhone());
				if (hasUser.isPresent()) {
					String uid = hasUser.get().getUid().toString();
					String verifyUUID = UUID.randomUUID().toString();
					ConstantParams.ANDROID_PARAMS.remove("uid" + verifyUUID);
					ConstantParams.ANDROID_PARAMS.remove("time" + verifyUUID);
					ConstantParams.ANDROID_PARAMS.remove(uid + "Flag");
					ConstantParams.ANDROID_PARAMS.put("uid" + verifyUUID, uid);
					ConstantParams.ANDROID_PARAMS.put("time" + verifyUUID, System.currentTimeMillis());
					ConstantParams.ANDROID_PARAMS.put(uid + "Flag", "true");
					responseJson.setMsg(verifyUUID);
				} else {
					responseJson.setCode(1);
					responseJson.setMsg("该手机号暂未注册");
					;
				}
			} else {
				responseJson.setCode(2);
				responseJson.setMsg("手机验证码错误");
			}
		} else {
			String email = user.getEmail();
			Optional<User> hasUser = userService.findByEmail(email);
			if (hasUser.isPresent()) {
				User resetUser = hasUser.get();
				String verifyUUID = UUID.randomUUID().toString();
				ConstantParams.ANDROID_PARAMS.remove("uid" + verifyUUID);
				ConstantParams.ANDROID_PARAMS.remove("time" + verifyUUID);
				ConstantParams.ANDROID_PARAMS.remove(resetUser.getUid() + "Flag");
				ConstantParams.ANDROID_PARAMS.put("uid" + verifyUUID, resetUser.getUid());
				ConstantParams.ANDROID_PARAMS.put("time" + verifyUUID, System.currentTimeMillis());
				ConstantParams.ANDROID_PARAMS.put(resetUser.getUid() + "Flag", "true");
				// 发送邮件
				EmailVo emailVo = new EmailVo();
				emailVo.setToEmail(email);
				emailVo.setType("resetPassword");
				emailVo.setUsername(resetUser.getUsername());
				emailVo.setURL("/resetpass.html?verifyUUID=" + verifyUUID+"&type=android");
				emailVo.setContextPath(request.getScheme() + "://127.0.0.1:"
						+ request.getServerPort() + request.getContextPath());
				responseJson.setCode(0);
				String hideEmail = MethodUtil.hideEmail(email);
				String emailType = MethodUtil.getEmailLoginURL(email);
				SuccJson succInfo = new SuccJson();
				succInfo.setJump(true);
				succInfo.setMsg("验证成功");
				succInfo.setSetTime(0);
				succInfo.setUrl("resetShow.html?hideEmail=" + hideEmail + "&emailType=" + emailType + "&verifyUUID="
						+ verifyUUID);
				responseJson.setMsg(succInfo);
				try {
					MethodUtil.sendEmailVerifyURL(emailVo);
				} catch (IOException e) {
					e.printStackTrace();
					responseJson.setCode(2);
					responseJson.setMsg("获取邮件模板出错");
				} catch (MessagingException e) {
					e.printStackTrace();
					responseJson.setCode(3);
					responseJson.setMsg("发送邮件失败");
				}
			} else {
				responseJson.setCode(4);
				responseJson.setMsg("系统出错");
			}
		}
		return responseJson;
	}
	
	/**
	 * 
	 * @Description: 个人主页-首页:根据Android Client传送的AppToken判读当前用户是否已登录，并返回当前用户的会员信息与头像
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午7:54:27
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserByAppTokenAndroid", method = RequestMethod.POST)
	public ResponseJson getUserByAppTokenAndroid(HttpServletRequest request) {
		String apptoken = request.getParameter("apptoken");
		String username = (String) ConstantParams.ANDROID_PARAMS.get(apptoken);
		User currentUser = userService.queryByIdentityInfo(username);
		if(currentUser == null) {
			responseJson.setCode(1);
			responseJson.setMsg("登陆信息错误请重新登陆");
		}else {
			TrUserViprank userViprank = userService.findUserViprankByUserId(currentUser.getUid());
			TbViprank viprank = viprankService.findById(userViprank.getViprankId());
			currentUser.setPassword("");
			currentUser.setViprankName(viprank.getName());
			
			List<TbUserImg> userImgs = userImgService.findByUid(currentUser.getUid());
			for(TbUserImg userImg : userImgs) {
				if(!StringUtils.isEmpty(userImg.getImgType())) {
					if("0".equals(userImg.getImgType())) {
						currentUser.setHeadImgPath(ConstantParams.SERVER_HEADIMG_UPLOAD_PATH+userImg.getImgUUID());
						break;
					}
				}
			}
			responseJson.setCode(0);
			responseJson.setMsg(currentUser);
		}
		return responseJson;
	}
	
	/**
	 * 
	 * @Description: 个人主页-个人详情根据Uid返回当前用户的具体信息,除去密码,与加密身份证
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午7:56:02
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserInfoByUidAndroid", method = RequestMethod.POST)
	public ResponseJson getUserInfoByUidAndroid(HttpServletRequest request) {
		Integer uid = Integer.parseInt(request.getParameter("uid"));
		String principal = (String) ConstantParams.ANDROID_PARAMS.get(request.getParameter("apptoken"));
		if(!StringUtils.isEmpty(principal)) {
			Optional<User> hasUser = userService.findById(uid);
			User user = null;
			if(hasUser.isPresent()) {
				user = hasUser.get();
				user.setPassword("");
				user.setIdCard(MethodUtil.hideIdCard(user.getIdCard()));
				responseJson.setCode(0);
				responseJson.setMsg(user);
			}else {
				responseJson.setCode(2);
				responseJson.setMsg("找不到用户");
			}
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("系统验证错误");
		}
		return responseJson;
	}
	
	/**
	 * 
	 * @Description: 个人主页-心动列表,查找系统中与该用户尚未确认关系的异性列表
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午7:57:39
	 */
	@ResponseBody
	@RequestMapping(value = "/heartbeatAndroid",method = RequestMethod.POST)
    public ResponseJson heartbeat(HttpServletRequest request) {
		Integer pageNum = 1;
		Integer pageSize = 1;
		String apptoken = request.getParameter("apptoken");
		User currentUser = userService.queryByIdentityInfo(ConstantParams.ANDROID_PARAMS.get(apptoken).toString());
		List<Integer> uids = userRelationService.findByUid(currentUser.getUid());
		Page<User> users = userService.findByGenderAndState(ConstantParams.TB_USER_GENDER_FEMALE.equals(currentUser.getGender()) ? ConstantParams.TB_USER_GENDER_MALE : ConstantParams.TB_USER_GENDER_FEMALE
				, ConstantParams.TB_USER_STATE_NORMAL, pageNum-1, pageSize, uids);
		long totalCount = users.getTotalElements();
		if(users.getSize() == 1) {
			UserImgVo userImg = new UserImgVo();
			List<TbUserImg> userImgs = userImgService.findByUidAndImgType(users.getContent().get(0).getUid(), ConstantParams.HEADIMG);
			userImg.setUid(users.getContent().get(0).getUid());
			userImg.setNickname(users.getContent().get(0).getNickname());
			userImg.setHeadImg((userImgs.size() == 0 ? "/img/defaultHeadImg.jpg" : 
				ConstantParams.SERVER_HEADIMG_UPLOAD_PATH + userImgs.get(0).getImgUUID()));
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("totalCount", totalCount);
			data.put("userImg", userImg);
			responseJson.setCode(0);
			responseJson.setMsg(data);
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("用户列表为空");
		}
        return responseJson;
    }
	
	/**
	 * 
	 * @Description:个人主页-心动列表-喜欢,处理用户关系,0为喜欢,1为不喜欢,2为未确认关系
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月7日 上午12:56:52
	 */
	@ResponseBody
	@RequestMapping(value = "/sendUserRelationAndroid", method = RequestMethod.POST)
	public ResponseJson sendUserRelationAndroid(HttpServletRequest request) {
		String relation = request.getParameter("relation");
		if(!StringUtils.isEmpty(relation) && !StringUtils.isEmpty(request.getParameter("targetUid"))) {
			Integer targetUid = Integer.parseInt(request.getParameter("targetUid"));
			User hasUser = userService.queryByIdentityInfo(ConstantParams.ANDROID_PARAMS.get(request.getParameter("apptoken")).toString());
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
	 * @Description: 个人主页-好友列表,显示该用户的所有好友
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午8:00:14
	 */
	@ResponseBody
	@RequestMapping(value = "/userFriendsListAndroid",method = RequestMethod.POST)
    public ResponseJson userFriendsListAndroid(HttpServletRequest request) {
		User user = userService.queryByIdentityInfo(ConstantParams.ANDROID_PARAMS.get(request.getParameter("apptoken")).toString());
		List<Integer> uids = userRelationService.findUserByUidAndAndRelations(user.getUid(),
				ConstantParams.USER_RELATION_LIKE, ConstantParams.USER_RELATION_LIKE);
		//查找用户朋友列表
		List<User> friendsList = userService.findAllUsersByUid(uids);
		List<User> filterFriendsInfoList = new ArrayList<User>();
		try {
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
					friend.setLastUserChat(defaultChat.length()> 5 ? defaultChat.substring(0,5)+"..." : defaultChat);
					//查找对应用户关系的对象，用于获取成为朋友的时间
					Optional<TbUserRelation> userRelation = userRelationService.findByUidAndTargetUid(user.getUid(), friends.getUid());
					friend.setLastUserChatTime(userRelation.isPresent() ? userRelation.get().getCreateTime() : new Timestamp(System.currentTimeMillis()));
				}else {
					friend.setLastUserChat(lastChatRecord.getChatRecode().length()> 5 ? lastChatRecord.getChatRecode().substring(0,5)+"..." : lastChatRecord.getChatRecode());
					friend.setLastUserChatTime(lastChatRecord.getLastChatTime());
				}
				filterFriendsInfoList.add(friend);
			}
			if(filterFriendsInfoList.size() == 0) {
				responseJson.setCode(2);
			}else {
				responseJson.setCode(0);
			}
			responseJson.setMsg(filterFriendsInfoList);
		} catch (Exception e) {
			e.printStackTrace();
			responseJson.setCode(1);
			responseJson.setMsg("系统错误");
		}
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
	@RequestMapping(value = "/saveUserChatAndroid",method = RequestMethod.POST)
    public ResponseJson saveUserChatAndroid(HttpServletRequest request) {
		try {
			request.setCharacterEncoding("utf-8");
			Integer toUid = Integer.parseInt(request.getParameter("toUid"));
			String chatRecode = request.getParameter("chatRecode");
			User user = userService.queryByIdentityInfo(ConstantParams.ANDROID_PARAMS.get(request.getParameter("apptoken")).toString());
			TbUserChatRecord userChatRecord = new TbUserChatRecord();
			userChatRecord.setFromUid(user.getUid());
			userChatRecord.setToUid(toUid);
			userChatRecord.setChatRecode(chatRecode);
//			userChatRecord.setType(0);
			userChatRecord.setLastChatTime(new Timestamp(System.currentTimeMillis()));
			userChatRecordService.saveUserChat(userChatRecord);
			responseJson.setCode(0);
			responseJson.setMsg("发送成功");
		} catch (Exception e) {
			e.printStackTrace();
			responseJson.setCode(1);
			responseJson.setMsg("系统错误");
		}
		return responseJson;
    }
	
	/**
	 * 
	 * @Description: 个人主页-好友聊天-聊天记录,显示当前用户与该好友的所有聊天记录
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午8:01:19
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserChatDetailsAndroid",method = RequestMethod.POST)
    public ResponseJson getUserChatDetailsAndroid(HttpServletRequest request) {
		try {
			Integer toUid = Integer.parseInt(request.getParameter("toUid"));
			User user = userService.queryByIdentityInfo(ConstantParams.ANDROID_PARAMS.get(request.getParameter("apptoken")).toString());
			TbUserChatRecord userChat = new TbUserChatRecord();
			userChat.setFromUid(toUid);
			userChat.setToUid(user.getUid());
			List<TbUserChatRecord> allChatRecords = userChatRecordService.findAllByFromUidAndToUid(userChat);
			List<TbUserImg> fromUidHeadImgs = userImgService.findByUidAndImgType(user.getUid(), ConstantParams.HEADIMG);
			List<TbUserImg> toUidHeadImgs = userImgService.findByUidAndImgType(toUid, ConstantParams.HEADIMG);
			if(allChatRecords.size() != 0) {
				allChatRecords.get(0).setFromUserImgPath((fromUidHeadImgs.size() == 0 ? "/img/defaultHeadImg.jpg" : 
					ConstantParams.SERVER_HEADIMG_UPLOAD_PATH + fromUidHeadImgs.get(0).getImgUUID()));
				allChatRecords.get(0).setToUserImgPath((toUidHeadImgs.size() == 0 ? "/img/defaultHeadImg.jpg" : 
					ConstantParams.SERVER_HEADIMG_UPLOAD_PATH + toUidHeadImgs.get(0).getImgUUID()));
			}
			responseJson.setCode(0);
			responseJson.setMsg(allChatRecords);
		} catch (Exception e) {
			e.printStackTrace();
			responseJson.setCode(1);
			responseJson.setMsg("系统错误");
		}
		return responseJson;
    }
	
	/**
	 * 
	 * @Description: 个人主页-好友聊天,聊天记录为0的时候,单独获取当前用户与该好友头像
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午8:02:03
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserChatHeadImgAndroid",method = RequestMethod.POST)
    public ResponseJson getUserChatHeadImgAndroid(HttpServletRequest request) {
		try {
			Integer toUid = Integer.parseInt(request.getParameter("toUid"));
			User user = userService.queryByIdentityInfo(ConstantParams.ANDROID_PARAMS.get(request.getParameter("apptoken")).toString());
			List<TbUserImg> fromUidHeadImgs = userImgService.findByUidAndImgType(user.getUid(), ConstantParams.HEADIMG);
			List<TbUserImg> toUidHeadImgs = userImgService.findByUidAndImgType(toUid, ConstantParams.HEADIMG);
			TbUserChatRecord userChatRecord = new TbUserChatRecord();
			userChatRecord.setFromUserImgPath((fromUidHeadImgs.size() == 0 ? "/img/defaultHeadImg.jpg" : 
				ConstantParams.SERVER_HEADIMG_UPLOAD_PATH + fromUidHeadImgs.get(0).getImgUUID()));
			userChatRecord.setToUserImgPath((toUidHeadImgs.size() == 0 ? "/img/defaultHeadImg.jpg" : 
				ConstantParams.SERVER_HEADIMG_UPLOAD_PATH + toUidHeadImgs.get(0).getImgUUID()));
			responseJson.setCode(0);
			responseJson.setMsg(userChatRecord);
		} catch (Exception e) {
			e.printStackTrace();
			responseJson.setCode(1);
			responseJson.setMsg("系统错误");
		}
		return responseJson;
    }
}
