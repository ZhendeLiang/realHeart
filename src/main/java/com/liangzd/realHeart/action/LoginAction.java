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
import com.liangzd.realHeart.VO.AdminUserVo;
import com.liangzd.realHeart.VO.ResponseJson;
import com.liangzd.realHeart.VO.UserVo;
import com.liangzd.realHeart.entity.User;
import com.liangzd.realHeart.realm.CustomizedToken;
import com.liangzd.realHeart.util.LoginType;

//@ControllerAdvice
@Controller
public class LoginAction {

    private static final transient Logger log = LoggerFactory.getLogger(LoginAction.class);
	private ResponseJson responseJson;

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
	public ResponseJson login(HttpServletRequest request,UserVo user){
	    boolean isAdmin = user.getAdminUsername() == null ? false : true;
		System.out.println("HomeController.login()");
	    
		Subject currentUser = SecurityUtils.getSubject();
		responseJson = new ResponseJson();
		CustomizedToken token = null;
		if (!currentUser.isAuthenticated()) {
			//判断是普通用户还是管理员,对应调用不同的realm进行验证
			token = !isAdmin ? 
					new CustomizedToken(user.getUserinp(),user.getPassword(),LoginType.USER.toString()) :
						new CustomizedToken(user.getAdminUsername(),user.getAdminPassword(),LoginType.ADMIN.toString());
        	// 把用户名和密码封装为 CustomizedToken 对象
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
			succInfo.setURL(isAdmin ? "http://localhost:8080/filter/adminIndex":"http://localhost:8080/filter/index");
			responseJson.setMsg(succInfo);
		}
	    // 此方法不处理登录成功,由shiro进行处理
	    return responseJson;
	}
	
	/**
	 * 
	 * @Description: 管理员登录校验
	 * @param 
	 * @return ResponseJson
	 * @author liangzd
	 * @date 2018年5月26日 上午2:05:12
	 */
//	@RequestMapping(value = "/verifyAdminLogin",method = RequestMethod.POST)
//    public ResponseJson registUser(AdminUserVo adminUser) {
//		
//        return responseJson;
//    }

}
