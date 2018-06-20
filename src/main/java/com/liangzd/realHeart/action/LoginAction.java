package com.liangzd.realHeart.action;

import java.io.IOException;
import java.sql.Timestamp;
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
import com.liangzd.realHeart.VO.UserVo;
import com.liangzd.realHeart.entity.User;
import com.liangzd.realHeart.realm.CustomizedToken;
import com.liangzd.realHeart.service.UserService;
import com.liangzd.realHeart.util.ConstantParams;
import com.liangzd.realHeart.util.LoginType;
import com.liangzd.realHeart.util.MethodUtil;
import com.liangzd.verifyCode.image.ImageVerifyCode;
@Controller
public class LoginAction {
	private static final transient Logger log = LoggerFactory.getLogger(LoginAction.class);
	private ResponseJson responseJson = new ResponseJson();
	@Autowired
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * 
	 * @Description: 用户登录校验 管理员登录校验
	 * @param
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年5月26日 上午2:05:12
	 */
	@ResponseBody
	@RequestMapping("/verifyLogin")
	public ResponseJson login(HttpServletRequest request, UserVo user) {
		boolean isAdmin = user.getAdminUsername() == null ? false : true;
		Subject currentUser = SecurityUtils.getSubject();
		responseJson = new ResponseJson();
		CustomizedToken token = null;
		/*if (!currentUser.isAuthenticated()) {*/
			// 判断是普通用户还是管理员,对应调用不同的realm进行验证
			token = !isAdmin ? new CustomizedToken(user.getUserinp(), user.getPassword(), LoginType.USER.toString())
					: new CustomizedToken(user.getAdminUsername(), user.getAdminPassword(), LoginType.ADMIN.toString());
			// 把用户名和密码封装为 CustomizedToken 对象
			// token.setRememberMe(true);
			boolean verifyCodeIsCorrect = false;
			String correctVerifyCode = "";
			if(!isAdmin) {
				if(user.getVerifyTimes() < 3) {
					request.getSession().setAttribute("loginImageCode", "");
				}
				correctVerifyCode = (String) request.getSession().getAttribute("loginImageCode");
				if (!StringUtils.isEmpty(correctVerifyCode)) {
					if (!StringUtils.isEmpty(user.getVeri()) && correctVerifyCode.toLowerCase().equals(user.getVeri().toLowerCase())) {
						verifyCodeIsCorrect = true;
					}
				}
			}
			if (StringUtils.isEmpty(correctVerifyCode) || verifyCodeIsCorrect) {
				try {
					currentUser.login(token);
					responseJson.setCode(0);
					log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
					request.getSession().setAttribute("username", token.getPrincipal());
				} catch (UnknownAccountException uae) {
					responseJson.setCode(1);
					log.info(token.getPrincipal() + "未注册");
					responseJson.setMsg(token.getPrincipal() + "未注册");
				} catch (IncorrectCredentialsException ice) {
					responseJson.setCode(2);
					log.info(token.getPrincipal() + "密码不正确");
					responseJson.setMsg(token.getPrincipal() + "密码不正确");
					responseJson.setUsingVerifyCode((user.getVerifyTimes() != null && user.getVerifyTimes() >= 3) ? true : false);
				} catch (LockedAccountException lae) {
					log.info("The account for username " + token.getPrincipal() + " is locked.  "
							+ "Please contact your administrator to unlock it.");
				}
			} else {
				responseJson.setCode(3);
				log.info("验证码错误");
				responseJson.setMsg("验证码错误");
				return responseJson;
			}
		/*} else {
			responseJson.setCode(0);
		}*/
		if (responseJson.getCode() == 0) {
			SuccJson succInfo = new SuccJson();
			succInfo.setJump(true);
			succInfo.setMsg(token == null ? (String) currentUser.getPrincipal() : token.getPrincipal() + "欢迎您");
			succInfo.setSetTime(0);
			succInfo.setUrl(isAdmin ? "/filter/adminWelcome" : "/filter/welcome");
			responseJson.setMsg(succInfo);
		}
		// 此方法不处理登录成功,由shiro进行处理
		return responseJson;
	}

	/**
	 * 
	 * @Description: 手机验证码登陆
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年5月31日 下午4:42:03
	 */
	@ResponseBody
	@RequestMapping(value = "/phoneLogin", method = RequestMethod.POST)
	public ResponseJson phoneLogin(HttpServletRequest request, UserVo user) {
		String phone = user.getPhone();
		String veriCode = user.getVeriCode();
		if(!StringUtils.isEmpty((String) request.getSession().getAttribute(phone)) &&
				((String) request.getSession().getAttribute(phone)).equals(veriCode)) {
			Optional<User> hasUser = userService.findByPhoneNumber(user.getPhone());
			if(hasUser.isPresent()) {
				User loginedUser = hasUser.get();
				CustomizedToken token = new CustomizedToken(loginedUser.getUsername(), loginedUser.getPassword(), LoginType.USER.toString());
				Subject currentUser = SecurityUtils.getSubject();
				try {
					currentUser.login(token);
					responseJson.setCode(0);
					log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
					request.getSession().setAttribute("username", token.getPrincipal());
					SuccJson succInfo = new SuccJson();
					succInfo.setJump(true);
					succInfo.setMsg(token == null ? (String) currentUser.getPrincipal() : token.getPrincipal() + "欢迎您");
					succInfo.setSetTime(0);
					succInfo.setUrl("/filter/welcome");
					responseJson.setMsg(succInfo);
				}catch(Exception e) {
					e.printStackTrace();
					responseJson.setCode(3);
					responseJson.setMsg("登陆过程，系统错误");
				}
			}else {
				responseJson.setCode(2);
				responseJson.setMsg("系统错误");
			}
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("您输入的验证码不正确");
		}
		return responseJson;
	}
	
	/**
	 * 
	 * @Description: 用户获取验证码
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年5月31日 下午4:42:03
	 */
	@RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
	public void verifyCode(HttpServletRequest request, HttpServletResponse response) {
		ImageVerifyCode vCode = new ImageVerifyCode(100, 30, 4);
		String type = request.getParameter("type");
		request.getSession().setAttribute(StringUtils.isEmpty(type) ? "imageCode" : type+"ImageCode", vCode.getCode());
		try {
			vCode.write(response.getOutputStream());
		} catch (IOException e) {
			log.error("验证码生成错误");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description:检查该用户名是否已注册
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年5月31日 下午4:47:39
	 */
	@ResponseBody
	@RequestMapping(value = "/checkUsername", method = RequestMethod.POST)
	public ResponseJson checkUsername(HttpServletRequest request, UserVo user) {
		Optional<User> hasUser = userService.findByUsername(user.getUsername());
		if(hasUser.isPresent()) {
			responseJson.setCode(0);
    		responseJson.setMsg("用户名已存在");
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("该用户名暂未注册");;
		}
		return responseJson;
	}
	
	/**
	 * 
	 * @Description:检查该手机号是否已注册
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年5月31日 下午4:47:39
	 */
	@ResponseBody
	@RequestMapping(value = "/checkPhone", method = RequestMethod.POST)
	public ResponseJson checkPhone(HttpServletRequest request, UserVo user) {
		Optional<User> hasUser = userService.findByPhoneNumber(user.getPhone());
		if(hasUser.isPresent()) {
			responseJson.setCode(0);
    		responseJson.setMsg("手机号已存在");
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("该手机号暂未注册");;
		}
		return responseJson;
	}
	
	/**
	 * 
	 * @Description:检查该邮箱是否已注册
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年5月31日 下午4:47:39
	 */
	@ResponseBody
	@RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
	public ResponseJson checkEmail(HttpServletRequest request, UserVo user) {
		Optional<User> hasUser = userService.findByEmail(user.getEmail());
		if(hasUser.isPresent()) {
			responseJson.setCode(0);
    		responseJson.setMsg("邮箱已存在");
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("邮箱暂未注册");;
		}
		return responseJson;
	}
	
	/**
	 * 
	 * @Description:获取手机验证码
	 * @param 
	 * @return void
	 * @author liangzd
	 * @date 2018年5月31日 下午4:47:39
	 */
	@ResponseBody
	@RequestMapping(value = "/getPhoneVerifyCode", method = RequestMethod.POST)
	public ResponseJson getPhoneVerifyCode(HttpServletRequest request, UserVo user) {
		boolean verifyCodeIsCorrect = false;
		String correctVerifyCode = (String) request.getSession().getAttribute(user.getType()+"ImageCode");
		if (!StringUtils.isEmpty(correctVerifyCode)) {
			if (!StringUtils.isEmpty(user.getVeri()) && correctVerifyCode.toLowerCase().equals(user.getVeri().toLowerCase())) {
				verifyCodeIsCorrect = true;
			}
		}
		if (StringUtils.isEmpty(correctVerifyCode) || verifyCodeIsCorrect) {
			if(!StringUtils.isEmpty(user.getPhone())) {
				try {
//					String result = MethodUtil.sendPhoneVerifyCode(user.getPhone());
					String result = JSONObject.toJSONString(new TestPhone(200, "ceshi", "1118"));
					@SuppressWarnings("unchecked")
					Map<String,Object> resultMap = (Map<String,Object>)JSON.parse(result);
					if(resultMap.get("code") instanceof Integer && (Integer)resultMap.get("code") == 200) {
						responseJson.setCode(0);
						request.getSession().setAttribute(user.getPhone(), resultMap.get("obj"));
						request.getSession().setAttribute(user.getPhone()+"_time", System.currentTimeMillis());
					}else {
						responseJson.setCode(2);
					}
					responseJson.setMsg(resultMap.get("msg"));
				} catch (Exception e) {
					e.printStackTrace();
					responseJson.setCode(3);
					responseJson.setMsg("系统异常");
				}
			}else {
				responseJson.setCode(4);
				responseJson.setMsg("手机号不能为空");
			}
		}else {
			responseJson.setCode(5);
			responseJson.setMsg("图形验证码错误!");
		}
		return responseJson;
	}

	/**
	 * 
	 * @Description: 判断手机验证码是否正确
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午8:17:58
	 */
	@ResponseBody
	@RequestMapping(value = "/checkPhoneVerifyCode", method = RequestMethod.POST)
	public ResponseJson checkPhoneVerifyCode(HttpServletRequest request, UserVo user) {
		String phone = user.getPhone();
		String veriCode = user.getVeriCode();
		if(!StringUtils.isEmpty((String) request.getSession().getAttribute(phone)) &&
				((String) request.getSession().getAttribute(phone)).equals(veriCode)) {
			responseJson.setCode(0);
			responseJson.setMsg("手机验证码正确");
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("手机验证码错误");
		}
		return responseJson;
	}

	/**
	 * 
	 * @Description: 手机注册账号,先校验当前手机号是否存在，在保存当前的注册对象
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午8:18:36
	 */
	@ResponseBody
	@RequestMapping(value = "/registAction", method = RequestMethod.POST)
	public ResponseJson registAction(HttpServletRequest request, UserVo user) {
		String phone = user.getPhone();
		String password = user.getPassport();
		Optional<User> hasUser = userService.findByPhoneNumber(phone);
		User addUser = new User();
		if(!hasUser.isPresent()) {
			addUser.setUsername(phone);
			addUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
			addUser.setGender("1");
			addUser.setViprankName("1");
			addUser.setPhoneNumber(phone);
			addUser.setState((byte)1);
			addUser.setPassword(StringUtils.isEmpty(password) ? "202cb962ac59075b964b07152d234b70"
					: MethodUtil.encrypt("MD5", password, null, 1));
			userService.addUser(addUser);
			//根据手机登陆
			CustomizedToken token = new CustomizedToken(phone, addUser.getPassword(), LoginType.USER.toString());
			Subject currentUser = SecurityUtils.getSubject();
			try {
				currentUser.login(token);
				responseJson.setCode(0);
				log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
				request.getSession().setAttribute("username", token.getPrincipal());
				SuccJson succInfo = new SuccJson();
				succInfo.setJump(true);
				succInfo.setMsg(phone+"欢迎您");
				succInfo.setSetTime(0);
				succInfo.setUrl("/filter/welcome");
				responseJson.setMsg(succInfo);
			}catch(Exception e) {
				e.printStackTrace();
				responseJson.setCode(3);
				responseJson.setMsg("登陆过程，系统错误");
			}
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("手机号已存在");
		}
		return responseJson;
	}

	/**
	 * 
	 * @Description: 手机/邮箱找回密码,重置密码的校验，
	 * 1.手机号重置密码
	 * 		判断当前的手机验证码是否正确，再判断手机号是否存在
	 * 2.邮箱重置密码
	 * 		判断此邮箱是否存在，存在的话，給该邮箱发送邮件，在邮箱中点击连接重置密码
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午8:19:31
	 */
	@ResponseBody
	@RequestMapping(value = "/resetVerify", method = RequestMethod.POST)
	public ResponseJson resetVerify(HttpServletRequest request, UserVo user) {
		if("phone".equals(user.getType())) {
			String phone = user.getPhone();
			String veriCode = user.getVeriCode();
			if(!StringUtils.isEmpty((String) request.getSession().getAttribute(phone)) &&
					((String) request.getSession().getAttribute(phone)).equals(veriCode)) {
				Optional<User> hasUser = userService.findByPhoneNumber(user.getPhone());
				if(hasUser.isPresent()) {
					String uid = hasUser.get().getUid().toString();
					String verifyUUID = UUID.randomUUID().toString();
					request.getSession().setAttribute("uid"+verifyUUID, uid);
					request.getSession().setAttribute("time"+verifyUUID, System.currentTimeMillis());
					request.getSession().setAttribute(uid+"Flag", "true");
					
					SuccJson succInfo = new SuccJson();
					succInfo.setJump(true);
					succInfo.setMsg("验证成功");
					succInfo.setSetTime(0);
					succInfo.setUrl("/resetpass.html?verifyUUID="+verifyUUID);
					responseJson.setMsg(succInfo);
				}else {
					responseJson.setCode(1);
					responseJson.setMsg("该手机号暂未注册");;
				}
			}else {
				responseJson.setCode(1);
				responseJson.setMsg("手机验证码错误");
			}
		}else {
			boolean verifyCodeIsCorrect = false;
			String correctVerifyCode = (String) request.getSession().getAttribute("resetImageCode");
			if (!StringUtils.isEmpty(correctVerifyCode)) {
				if (!StringUtils.isEmpty(user.getVeri()) && correctVerifyCode.toLowerCase().equals(user.getVeri().toLowerCase())) {
					verifyCodeIsCorrect = true;
				}
			}
			if (StringUtils.isEmpty(correctVerifyCode) || verifyCodeIsCorrect) {
				String email = user.getEmail();
				Optional<User> hasUser = userService.findByEmail(email);
				if(hasUser.isPresent()) {
					User resetUser = hasUser.get();
					String verifyUUID = UUID.randomUUID().toString();
					request.getSession().setAttribute("uid"+verifyUUID, resetUser.getUid());
					request.getSession().setAttribute("time"+verifyUUID, System.currentTimeMillis());
					request.getSession().setAttribute(resetUser.getUid()+"Flag", "true");
					//发送邮件
					EmailVo emailVo = new EmailVo();
					emailVo.setToEmail(email);
					emailVo.setType("resetPassword");
					emailVo.setUsername(resetUser.getUsername());
					emailVo.setURL("/resetpass.html?verifyUUID="+verifyUUID);
					emailVo.setContextPath(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath());
					
					responseJson.setCode(0);
					String hideEmail = MethodUtil.hideEmail(email);
					String emailType = MethodUtil.getEmailLoginURL(email);
					SuccJson succInfo = new SuccJson();
					succInfo.setJump(true);
					succInfo.setMsg("验证成功");
					succInfo.setSetTime(0);
					succInfo.setUrl("resetShow.html?hideEmail="+hideEmail+"&emailType="+emailType+"&verifyUUID="+verifyUUID);
					responseJson.setMsg(succInfo);
					try {
						MethodUtil.sendEmailVerifyURL(emailVo);
					} catch (IOException e) {
						e.printStackTrace();
						responseJson.setCode(2);
						responseJson.setMsg("获取邮件模板出错");
					}catch(MessagingException e) {
						e.printStackTrace();
						responseJson.setCode(3);
						responseJson.setMsg("发送邮件失败");
					}
				}else {
					responseJson.setCode(4);
					responseJson.setMsg("系统出错");
				}
			}else {
				responseJson.setCode(1);
				responseJson.setMsg("验证码输入错误");
			}
		}
		return responseJson;
	}

	/**
	 * 
	 * @Description: 根据传过来的verifyUUID，重置当前绑定用户的密码
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月16日 下午8:20:27
	 */
	@ResponseBody
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public ResponseJson resetPassword(HttpServletRequest request, UserVo user) {
		String verifyUUID = user.getVerifyUUID();
		if(!StringUtils.isEmpty(verifyUUID)) {
			String correctUid = "";
			String correctTime = "";
			String isResetPassword = "";
			if("android".equals(user.getType())) {
				correctUid = String.valueOf(ConstantParams.ANDROID_PARAMS.get("uid"+verifyUUID));
				correctTime = String.valueOf(ConstantParams.ANDROID_PARAMS.get("time"+verifyUUID));
				isResetPassword = (String) ConstantParams.ANDROID_PARAMS.get(correctUid+"Flag");
			}else {
				correctUid = String.valueOf(request.getSession().getAttribute("uid"+verifyUUID));
				correctTime = String.valueOf(request.getSession().getAttribute("time"+verifyUUID));
				isResetPassword = (String) request.getSession().getAttribute(correctUid+"Flag");
			}
			if((!StringUtils.isEmpty(correctUid) || !StringUtils.isEmpty(correctTime)) &&
					(!StringUtils.isEmpty(isResetPassword) && "true".equals(isResetPassword))) {
				long alreadyPassed = System.currentTimeMillis() - Long.parseLong(correctTime);
				if(alreadyPassed >= (30 * 60 * 1000)) {
					responseJson.setCode(1);
					responseJson.setMsg("超出时间，请重新验证");
				}else {
					String password = StringUtils.isEmpty(user.getPassport()) ? "202cb962ac59075b964b07152d234b70"
							: MethodUtil.encrypt("MD5", user.getPassport(), null, 1);
					userService.updateUserPassword(Integer.parseInt(correctUid), password);
					responseJson.setCode(0);
					SuccJson succInfo = new SuccJson();
					succInfo.setJump(true);
					succInfo.setMsg("修改密码成功");
					succInfo.setSetTime(0);
					succInfo.setUrl("/login.html");
					responseJson.setMsg(succInfo);
				}
			}else {
				responseJson.setCode(1);
				responseJson.setMsg("验证失败,无法修改密码");
			}
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("验证失败,无法修改密码");
		}
		return responseJson;
	}
	
	/**
	 * 
	 * @Description: 单纯用于校验验证码
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月3日 下午3:52:24
	 */
	@ResponseBody
	@RequestMapping(value = "/checkImageVerifyCode", method = RequestMethod.POST)
	public ResponseJson checkImageVerifyCode(HttpServletRequest request, UserVo user) {
		boolean verifyCodeIsCorrect = false;
		String correctVerifyCode = (String) request.getSession().getAttribute(user.getType()+"ImageCode");
		if (!StringUtils.isEmpty(correctVerifyCode)) {
			if (!StringUtils.isEmpty(user.getVeri()) && correctVerifyCode.toLowerCase().equals(user.getVeri().toLowerCase())) {
				verifyCodeIsCorrect = true;
			}
		}
		if (StringUtils.isEmpty(correctVerifyCode) || verifyCodeIsCorrect) {
			responseJson.setCode(0);
			responseJson.setMsg("验证成功");
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("验证码输入错误");
		}
		return responseJson;
	}
	
	/**
	 * 
	 * @Description: 单纯用于校验验证码
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年6月3日 下午3:52:24
	 */
	@ResponseBody
	@RequestMapping(value = "/resetSendEmail", method = RequestMethod.POST)
	public ResponseJson resetSendEmail(HttpServletRequest request, UserVo user) {
		String verifyUUID = user.getVerifyUUID();
		if(!StringUtils.isEmpty(verifyUUID)) {
			String correctTime = String.valueOf(request.getSession().getAttribute("time"+verifyUUID));
			long alreadyPassed = System.currentTimeMillis() - Long.parseLong(correctTime);
			if(alreadyPassed > (5 * 60 * 1000)) {
				
				String correctUid = String.valueOf(request.getSession().getAttribute("uid"+verifyUUID));
				Optional<User> hasUser = userService.findById(Integer.parseInt(correctUid));
				EmailVo emailVo = new EmailVo();
				if(hasUser.isPresent()) {
					
					request.getSession().setAttribute("uid"+verifyUUID, "");
					request.getSession().setAttribute("time"+verifyUUID, "");
					
					User resetUser = hasUser.get();
					verifyUUID = UUID.randomUUID().toString();
					request.getSession().setAttribute("uid"+verifyUUID, resetUser.getUid());
					request.getSession().setAttribute("time"+verifyUUID, System.currentTimeMillis());
					request.getSession().setAttribute(resetUser.getUid()+"Flag", "true");
					//发送邮件
					emailVo.setToEmail(resetUser.getEmail());
					emailVo.setType("resetPassword");
					emailVo.setUsername(resetUser.getUsername());
					emailVo.setURL("/resetpass.html?verifyUUID="+verifyUUID);
					emailVo.setContextPath(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath());
					responseJson.setCode(0);
					responseJson.setMsg("邮件发送成功!");
				}
				try {
					MethodUtil.sendEmailVerifyURL(emailVo);
				} catch (IOException e) {
					e.printStackTrace();
					responseJson.setCode(4);
					responseJson.setMsg("获取邮件模板出错");
				}catch(MessagingException e) {
					e.printStackTrace();
					responseJson.setCode(3);
					responseJson.setMsg("发送邮件失败");
				}
			}else {
				responseJson.setCode(2);
				responseJson.setMsg("请查收邮件，五分钟未收到尝试再次发送");
			}
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("验证错误");
		}
		return responseJson;
	}
}
