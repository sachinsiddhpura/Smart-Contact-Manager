package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entity.User;
import com.smart.helper.Message;


@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","Home -Smart Contact Manager");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title","About -Smart Contact Manager");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signUp(Model model)
	{
		model.addAttribute("title","SignUp -Smart Contact Manager");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	@RequestMapping(value = "/do_register",method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1,@RequestParam(value = "agreement",defaultValue="false") boolean agreement,Model model, HttpSession session)
	{
		try
		{
			if(!agreement)
			{
				throw new Exception("You are not agreed terms and conditions");
			}
			
			if(result1.hasErrors())
			{
				model.addAttribute("user",user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			User result = this.userRepository.save(user);
			
			model.addAttribute("user",result);
			session.setAttribute("message", new Message("Successfully Registered..","alert-success"));
			return "signup";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong..!!"+e.getMessage(),"alert-danger"));
			return "signup";
		}
	}
	
	@GetMapping("/signin")
	public String login(Model model)
	{
		model.addAttribute("title","Sign In");
		return "login";
	}
	
	@GetMapping("/login_fail")
	public String loginFail(Model model)
	{
		model.addAttribute("title","LoginIn failed");
		return "loginFail";
	}
}
