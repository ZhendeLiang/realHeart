package com.liangzd.realHeart.action;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

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
	private ResponseJson responseJson = new ResponseJson();;
	
	@Autowired
	private UserService userService;
	@Autowired

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
	@RequestMapping(value = "/user",method = RequestMethod.GET)
	public ResponseJson getUser(User user) {
    	if(user.getUid() != null && user.getUid()==0) {
			responseJson.setCode(1);
			responseJson.setMsg("获取用户失败-uid为空");
		}else {
			User oldUser = userService.findByIdWithViprankName(user.getUid());
			if(oldUser != null) {
				responseJson.setCode(0);
				responseJson.setMsg(oldUser);
			}else {
				responseJson.setCode(0);
				responseJson.setMsg("获取用户失败-系统中无该用户");
			}
		}
		return responseJson;
	}
    
	@ResponseBody
	@RequestMapping(value = "/user",method = RequestMethod.POST)
	public ResponseJson addUser(User user) {
		user.setCreateTime(new Date(System.currentTimeMillis()));
		user.setPassword(StringUtils.isEmpty(user.getPassword()) ? "202cb962ac59075b964b07152d234b70" : 
			MethodUtil.encrypt("MD5", user.getPassword(), null, 1));
		userService.addUser(user);
		responseJson.setCode(0);
		responseJson.setMsg("添加用户成功");
		return responseJson;
	}
	
	@ResponseBody
	@RequestMapping(value = "/user",method = RequestMethod.PUT)
	public ResponseJson updateUser(User user) {
		if(user.getUid() != null && user.getUid()==0) {
			responseJson.setCode(1);
			responseJson.setMsg("更新用户失败-uid为空");
		}else {
			Optional<User> oldUserOpt = userService.findById(user.getUid());
			User oldUser = null;
			if(oldUserOpt != null && oldUserOpt.isPresent()) {
				oldUser = oldUserOpt.get();
				user.setCreateTime(oldUser.getCreateTime());
				user.setIdCard(oldUser.getIdCard());
				user.setNickname(oldUser.getNickname());
				user.setRoleList(oldUser.getRoleList());
				user.setPassword(StringUtils.isEmpty(user.getPassword()) ? "202cb962ac59075b964b07152d234b70" : 
					MethodUtil.encrypt("MD5", user.getPassword(), null, 1));
				userService.addUser(user);
				responseJson.setCode(0);
				responseJson.setMsg("更新用户成功");
			}else {
				responseJson.setCode(0);
				responseJson.setMsg("更新用户失败-系统中无该用户");
			}
		}
		return responseJson;
	}
	
	@ResponseBody
	@RequestMapping(value = "/user",method = RequestMethod.DELETE)
	public ResponseJson deleteUser(User user) {
		return responseJson;
	}
}
