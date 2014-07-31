package com.cagnosolutions.cei.houseontherock.fantheflamedates.controller.misc;

import com.cagnosolutions.cei.houseontherock.fantheflamedates.domain.User;
import com.cagnosolutions.cei.houseontherock.fantheflamedates.domain.Worksheet;
import com.cagnosolutions.cei.houseontherock.fantheflamedates.service.FlashService;
import com.cagnosolutions.cei.houseontherock.fantheflamedates.service.UserService;
import com.cagnosolutions.cei.houseontherock.fantheflamedates.service.WorksheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by greg on 7/29/14.
 */

@Controller
public class MiscController {

	@Autowired
	private UserService userService;

	@Autowired
	private WorksheetService worksheetService;

	@Autowired
	private FlashService flashService;

	@RequestMapping(value = "/worksheet", method = RequestMethod.POST)
	public String worksheet(Worksheet worksheet) {
		User user = userService.findById(worksheet.getUserId());
		user.advanceChallenge();
		userService.update(user);
		worksheetService.add(worksheet);
		return "redirect:" + worksheet.getVideoId();
	}

	@RequestMapping(value = "/challenge", method = RequestMethod.POST)
	public String challenge(@RequestParam(value = "username") String username, RedirectAttributes attr) {
		User user = userService.findById(username);
		user.setChallengeAccepted(true);
		user.setChallengeProgress((short) 0);
		userService.update(user);
		flashService.flashAlert(attr, "You Have Successfully Started the challenge", "success", true);
		return "redirect:/user";
	}
}
