package com.liangzd.realHeart.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.liangzd.realHeart.bean.ResponseJson;
import com.liangzd.realHeart.bean.UserVo;
import com.liangzd.realHeart.entity.User;

//@ControllerAdvice
@Controller
public class LoginAction {

    private static final transient Logger log = LoggerFactory.getLogger(LoginAction.class);
	private ResponseJson responseJson;
	
	@RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello(Model model) {
        model.addAttribute("name", "Dear");
        return "hello";
    }

	@ResponseBody
	@RequestMapping("/verifyLogin")
	public ResponseJson login(HttpServletRequest request,UserVo user){
	    System.out.println("HomeController.login()");
	    
		Subject currentUser = SecurityUtils.getSubject();
		responseJson = new ResponseJson();
		UsernamePasswordToken token = null;
		if (!currentUser.isAuthenticated()) {
        	// 把用户名和密码封装为 UsernamePasswordToken 对象
            token = new UsernamePasswordToken(user.getUserinp(),user.getPassword());
//            token.setRememberMe(true);
            try {
                currentUser.login(token);
            	responseJson.setCode(0);
            	log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
            } catch (UnknownAccountException uae) {
            	responseJson.setCode(1);
                log.info(token.getPrincipal()+"未注册");
                responseJson.setMsg(token.getPrincipal()+"未注册");
            } catch (IncorrectCredentialsException ice) {
            	responseJson.setCode(2);
                log.info(token.getPrincipal()+"密码不正确");
                responseJson.setMsg(token.getPrincipal()+"密码不正确");
            } catch (LockedAccountException lae) {
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            } catch (Exception lae) {
            	System.out.println("错误");
            }
        }else {
        	responseJson.setCode(0);
        }
		if(responseJson.getCode() == 0) {
			SuccJson succInfo = new SuccJson();
			succInfo.setJump(true);
			succInfo.setMsg(token==null? (String) currentUser.getPrincipal(): token.getPrincipal()+"欢迎您");
			succInfo.setSetTime(0);
			succInfo.setURL("http://localhost:8080/index.html");
			responseJson.setMsg(succInfo);
		}
	    // 此方法不处理登录成功,由shiro进行处理
	    return responseJson;
	}
	
//	@ResponseBody
//	@RequestMapping(value = "/verifyLogin",method = RequestMethod.POST)
//    public ResponseJson verifyLogin(UserInfo userInfo) {
//		System.out.println(userInfo.toString());
//		System.out.println("验证登陆");
//		responseJson = new ResponseJson();
//		responseJson.setSuccess(true);
//		responseJson.setMessage("登陆成功");
//		responseJson.setData("/hello");
//		System.out.println(JSON.toJSONString(responseJson));
//        return responseJson;
//    }

	@RequestMapping(value = "/registUser",method = RequestMethod.POST)
    public String registUser(User user) {
		System.out.println(user.toString());
		System.out.println("注册用户");
        return "selfDetails";
    }

	@ResponseBody
	@RequestMapping(value = "/getResult",method = RequestMethod.GET)
    public ResponseJson getUser() {
		responseJson = new ResponseJson();
		responseJson.setCode(0);
		responseJson.setMsg("ceshi");
		System.out.println(JSON.toJSONString(responseJson));
//		if(responseJson.getCode() == 0) {
//			responseJson.setMsg(new SuccJson());
//		}else{
//			responseJson.setMsg(responseJson.getCode() == 1 ? "用户未注册" : "密码错误");
//		}
		responseJson.setMsg(new SuccJson(3,"15893866112欢迎您来到UI中国","http://www.ui.cn",true));
		System.out.println(JSON.toJSONString(responseJson));
		return responseJson;
    }
	
//	@ExceptionHandler(value = Exception.class)
//	public ModelAndView myErrorHandler(Exception ex) {
//	    ModelAndView modelAndView = new ModelAndView();
//	    modelAndView.setViewName("403");
//	    return modelAndView;
//	}
}
