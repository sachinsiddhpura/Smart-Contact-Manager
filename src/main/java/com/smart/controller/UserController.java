package com.smart.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.dao.UserRepository;
import com.smart.entity.Contact;
import com.smart.entity.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@ModelAttribute
	public void addCommonData(Model model,Principal principal)
	{
		String username = principal.getName();
		User user = null;
		
		if(username!=null)
		{
			user = userRepository.getUserByUsername(username);
		}
		
		if(user!=null)
		{
			model.addAttribute("user",user);
		}
	}
	
	@GetMapping("/index")
	public String dashboard(Model model,Principal principal)
	{
		model.addAttribute("title","User Dashboard");
		return "user_dashboard";
	}
	
	@GetMapping("/add_contact")
	public String addContact(Model model,Principal principal)
	{
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact",new Contact());
		return "addContact";
	}
}
