package com.lich0079.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lich0079.jbpm.JbpmEngine;

@Controller
public class LoginController {
	@RequestMapping("/index")
	public String index(HttpServletRequest request){
	    jbpmEngine.startProcess("MyTestBPMN",null);
		return "index";
	}
	
    @Autowired
    private JbpmEngine jbpmEngine;
	

    @RequestMapping("/task")
    public String task(@RequestParam("u") String userId){
        jbpmEngine.completeTask(userId, null);
        return "index";
    }
}
