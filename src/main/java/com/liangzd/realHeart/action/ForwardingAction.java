package com.liangzd.realHeart.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@RequestMapping(value="")
@Controller
public class ForwardingAction {

    private static final transient Logger log = LoggerFactory.getLogger(ForwardingAction.class);
	
//	@RequestMapping(value = "/login",method = RequestMethod.GET)
//    public String login() {
//        return "login";
//    }

	@RequestMapping(value = "/filter/welcome",method = RequestMethod.GET)
    public String welcome() {
        return "welcome";
    }

//	@RequestMapping(value = "/403",method = RequestMethod.GET)
//    public String errorPage() {
//        return "403";
//    }

	@RequestMapping(value = "/adminWelcome",method = RequestMethod.GET)
    public String adminWelcome() {
        return "adminWelcome";
    }

	/*@RequestMapping(value = "/home/welcome",method = RequestMethod.GET)
    public String homePage() {
        return "/home/welcome";
    }*/
}
