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

	@RequestMapping(value = "/filter/index",method = RequestMethod.GET)
    public String index() {
        return "index";
    }

//	@RequestMapping(value = "/403",method = RequestMethod.GET)
//    public String errorPage() {
//        return "403";
//    }

	@RequestMapping(value = "/adminIndex",method = RequestMethod.GET)
    public String adminIndex() {
        return "adminIndex";
    }
}
