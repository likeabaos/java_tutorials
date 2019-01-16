package com.play.ground.HelloSpringBoot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimpleController {
	@Value("${spring.application.name}")
	String appName;
	
	@GetMapping("/")
	public String homepage(Model model) {
		model.addAttribute("appName", this.appName);
		return "home";
	}

}
