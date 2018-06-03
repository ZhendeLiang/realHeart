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
import com.liangzd.realHeart.VO.UserVo;
import com.liangzd.realHeart.entity.User;
import com.liangzd.realHeart.service.UserService;
import com.liangzd.realHeart.util.MethodUtil;

@RequestMapping(value = "/filter/json")
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
	@RequestMapping(value = "/usersList", method = RequestMethod.GET)
	public List<User> getAllUsers() {
		List<User> users = userService.findAllUsersWithViprankName();
		return users;
	}

	@ResponseBody
	@RequestMapping(value = "/usersListWithSearch", method = RequestMethod.GET)
	public List<User> getAllUsersWithSearch(User user) {
		List<User> users = userService.findAllUserWithSearch(user);
		return users;
	}

	
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
	 * @Description: 批量删除，因为user之间没有相关联系，所以不需要事务控制原子性
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
}
