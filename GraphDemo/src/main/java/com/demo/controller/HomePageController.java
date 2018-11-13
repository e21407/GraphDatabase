package com.demo.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.tool.StringTool;

@Controller
public class HomePageController {
	@RequestMapping("/")
	public String index(Model model, HttpServletResponse response) {
	    model.addAttribute("name", "simonsfan");
	    return "/index";
	}

	
	@RequestMapping("/index")
	public String helloPage() {
		return "index";
	}

	@RequestMapping("/test")
	public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name,
			Model model) {
		model.addAttribute("name", name);
		return "index";
	}

}
