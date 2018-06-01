package com.liangzd.realHeart.action;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

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
import com.liangzd.realHeart.VO.ResponseJson;
import com.liangzd.realHeart.VO.TestPhone;
import com.liangzd.realHeart.VO.UserVo;
import com.liangzd.realHeart.entity.User;
import com.liangzd.realHeart.realm.CustomizedToken;
import com.liangzd.realHeart.service.UserService;
import com.liangzd.realHeart.util.LoginType;
import com.liangzd.verifyCode.image.ImageVerifyCode;
import com.liangzd.verifyCode.phone.PhoneConfiguration;
import com.liangzd.verifyCode.phone.PhoneVerifyCode;
@Controller
public class LoginAction {
	private static final transient Logger log = LoggerFactory.getLogger(LoginAction.class);
	private ResponseJson responseJson = new ResponseJson();
	@Autowired
	private UserService userService;
	
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
		System.out.println("HomeController.login()");
		Subject currentUser = SecurityUtils.getSubject();
		responseJson = new ResponseJson();
		CustomizedToken token = null;
		if (!currentUser.isAuthenticated()) {
			// 判断是普通用户还是管理员,对应调用不同的realm进行验证
			token = !isAdmin ? new CustomizedToken(user.getUserinp(), user.getPassword(), LoginType.USER.toString())
					: new CustomizedToken(user.getAdminUsername(), user.getAdminPassword(), LoginType.ADMIN.toString());
			// 把用户名和密码封装为 CustomizedToken 对象
			// token.setRememberMe(true);
			boolean verifyCodeIsCorrect = false;
			String correctVerifyCode = "";
			if(!isAdmin) {
				if(user.getVerifyTimes() < 3) {
					request.getSession().setAttribute("code", "");
				}
				correctVerifyCode = (String) request.getSession().getAttribute("code");
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
				} catch (Exception lae) {
					System.out.println("错误");
				}
			} else {
				responseJson.setCode(3);
				log.info("验证码错误");
				responseJson.setMsg("验证码错误");
				return responseJson;
			}
		} else {
			responseJson.setCode(0);
		}
		if (responseJson.getCode() == 0) {
			SuccJson succInfo = new SuccJson();
			succInfo.setJump(true);
			succInfo.setMsg(token == null ? (String) currentUser.getPrincipal() : token.getPrincipal() + "欢迎您");
			succInfo.setSetTime(0);
			succInfo.setURL(isAdmin ? "/adminWelcome" : "/filter/welcome");
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
					succInfo.setURL("/filter/welcome");
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
		request.getSession().setAttribute("code", vCode.getCode());
		try {
			vCode.write(response.getOutputStream());
		} catch (IOException e) {
			log.error("验证码生成错误");
			e.printStackTrace();
		}
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
    		responseJson.setMsg("手机号存在");
		}else {
			responseJson.setCode(1);
			responseJson.setMsg("该手机号暂未注册");;
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
		if(!StringUtils.isEmpty(user.getPhone())) {
			try {
	        	PhoneConfiguration config = new PhoneConfiguration();
	        	config.setAppKey("2ccbcfe7da5cae79690b870d8e7cfb42");
	        	config.setAppSecret("b3054efaaa42");
//	        	config.setCodelen("6");
	        	config.setNonce("123456");
	        	config.setServerUrl("https://api.netease.im/sms/sendcode.action");
//	        	config.setTemplateid("4073043");
	        	PhoneVerifyCode phoneVerifyCode = new PhoneVerifyCode(config);
	            String result = phoneVerifyCode.sendMsg(user.getPhone());
//	        	String result = JSONObject.toJSONString(new TestPhone(200, "ceshi", "4262"));
	            System.out.println(result);
	            Map<String,Object> resultMap = (Map<String,Object>)JSON.parse(result);
	    		if(resultMap.get("code") instanceof Integer && (Integer)resultMap.get("code") == 200) {
	    			responseJson.setCode(0);
	    			request.getSession().setAttribute(user.getPhone(), resultMap.get("obj"));
	    			request.getSession().setAttribute(user.getPhone()+"_time", System.currentTimeMillis());
	    		}else {
	    			responseJson.setCode(2);
	    		}
	    		responseJson.setMsg(resultMap.get("msg"));
	    		return responseJson;
	        } catch (Exception e) {
	        	e.printStackTrace();
    			responseJson.setCode(3);
	    		responseJson.setMsg("系统异常");
    			return responseJson;
	        }
		}
		responseJson.setCode(1);
		responseJson.setMsg("手机号不能为空");
		return responseJson;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
