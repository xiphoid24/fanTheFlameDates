package com.cagnosolutions.cei.houseontherock.fantheflamedates.controller;

import com.cagnosolutions.cei.houseontherock.fantheflamedates.domain.User;
import com.cagnosolutions.cei.houseontherock.fantheflamedates.service.FlashService;
import com.cagnosolutions.cei.houseontherock.fantheflamedates.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class MainController {

    @Autowired
	private UserService userService;

	@Autowired
	private FlashService flashService;

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String index() {
        return "redirect:/home";
    }

	@RequestMapping(value="/home", method=RequestMethod.GET)
	public String home(Principal principal, Model model) {
        model.addAttribute("auth", (principal == null));
        return "home";
  	}

	/*@RequestMapping(value = "/resetpass", method = RequestMethod.GET)
    public String resetpassForm() {
        return "resetpass";
    }

    @RequestMapping(value = "/resetpass", method = RequestMethod.POST)
    public String resetpassForm(@RequestParam("username") String username, @RequestParam("email") String email) {
        User user = userService.findById(username);
        if(user != null) {
            if(email.equals(user.getEmail())) {
                String hash = user.getPassword().substring(1, 9);
                // i have decided upon something
                // we will use oauth2 with google, or twitter
                // for password resetting.
            }
        }
        return "resetpass";
    }*/

	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String registerForm(Model model) {
		return "redirect:/home?register";
	}

	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String register(User user, @RequestParam("confirm") String confirm, RedirectAttributes attr) {
		if (!userService.exists(user.getUsername()) &&
                userService.usernameIsValid(user.getUsername()) &&
                    confirm.equals(user.getPassword())) {
			user.setActive(true);
			user.setRole("ROLE_USER");
		    user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
			userService.insert(user);
			flashService.flash(attr, "register.success");
			return "redirect:/home";
		}
		flashService.flash(attr, "register.error");
		return "redirect:/home?register=true";
	}

    @RequestMapping(value="/login")
    public String login(@RequestParam(value="error", required=false) String error, RedirectAttributes attr) {
		if (error != null) {
			flashService.flash(attr, "login.error");
		}
		return ("redirect:/home?login=true");
    }

	@RequestMapping(value="/about")
	public String about(Principal principal, Model model) {
		model.addAttribute("auth", (principal == null));
		return "about";
	}

	@RequestMapping(value="/contact")
	public String contact(Principal principal, Model model) {
		model.addAttribute("auth", (principal == null));
		return "contact";
	}

	@RequestMapping(value="/donate")
	public String donate(Principal principal, Model model) {
		model.addAttribute("auth", (principal == null));
		return "donate";
	}


	@ExceptionHandler(value={Exception.class, RuntimeException.class})
    public ModelAndView errorHandler(Exception e) {
        ModelAndView view = new ModelAndView("main/exception");
        view.addObject("msg", e.getLocalizedMessage());
        StringBuilder sb = new StringBuilder();
        for(StackTraceElement frame : e.getStackTrace())
            sb.append(frame.toString()).append("\n");
        view.addObject("ex", sb.toString());
        return view;
    }
}