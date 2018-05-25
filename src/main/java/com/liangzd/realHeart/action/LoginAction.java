package com.liangzd.realHeart.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.liangzd.realHeart.bean.ResponseJson;
import com.liangzd.realHeart.bean.UserInfo;

@Controller
public class LoginAction {
	private ResponseJson responseJson;
	
	@RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello(Model model) {
        model.addAttribute("name", "Dear");
        return "hello";
    }

	@RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index() {
        return "index";
    }

	@ResponseBody
	@RequestMapping(value = "/verifyLogin",method = RequestMethod.POST)
    public ResponseJson verifyLogin(UserInfo userInfo) {
		System.out.println(userInfo.toString());
		System.out.println("验证登陆");
		responseJson = new ResponseJson();
		responseJson.setSuccess(true);
		responseJson.setMessage("登陆成功");
		responseJson.setData("/hello");
		System.out.println(JSON.toJSONString(responseJson));
        return responseJson;
    }

	@RequestMapping(value = "/registUser",method = RequestMethod.POST)
    public String registUser(UserInfo userInfo) {
		System.out.println(userInfo.toString());
		System.out.println("注册用户");
        return "selfDetails";
    }

	@ResponseBody
	@RequestMapping(value = "/getUser",method = RequestMethod.GET)
    public UserInfo getUser() {
		UserInfo userInfo = new UserInfo("admin","female","123","ceshi@qq.com","1235646654");
		System.out.println(JSON.toJSONString(userInfo));
		return userInfo;
    }
}
