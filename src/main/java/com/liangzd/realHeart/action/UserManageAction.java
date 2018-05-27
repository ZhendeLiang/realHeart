package com.liangzd.realHeart.action;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.liangzd.realHeart.VO.ResponseJson;
import com.liangzd.realHeart.entity.User;
import com.liangzd.realHeart.service.UserService;
import com.liangzd.realHeart.util.MethodUtil;

@RequestMapping(value="/filter/json")
@Controller
public class UserManageAction {
	private ResponseJson responseJson;
	
	@Autowired
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private static final transient Logger log = LoggerFactory.getLogger(UserManageAction.class);
	
    @ResponseBody
	@RequestMapping(value = "/usersList",method = RequestMethod.GET)
    public List<User> getAllUsers() {
    	List<User> users = userService.findAllUsersWithViprankName();
        return users;
    }
    
	@ResponseBody
	@RequestMapping(value = "/addUser",method = RequestMethod.POST)
	public ResponseJson addUser(User user, HttpServletRequest request) {
		user.setCreateTime(new Date(System.currentTimeMillis()));
		user.setPassword(StringUtils.isEmpty(user.getPassword()) ? "202cb962ac59075b964b07152d234b70" : 
			MethodUtil.encrypt("MD5", user.getPassword(), null, 1));
		userService.addUser(user);
		responseJson = new ResponseJson();
		responseJson.setCode(0);
		responseJson.setMsg("添加用户成功");
		return responseJson;
	}
}
