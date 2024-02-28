package com.tom.banking.controller;


import com.tom.banking.domain.TransferDTO;
import com.tom.banking.domain.User;
import com.tom.banking.domain.UserDTO;
import com.tom.banking.service.GenerateIBAN;
import com.tom.banking.service.TransferService;
import com.tom.banking.service.UserService;
import com.tom.banking.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private TransferService transferService;

	@Autowired
	private Util util;
	
	@GetMapping("/")
	public String showLogin(Model model) {
		model.addAttribute("userDTO", new UserDTO());
		return "index";
	}
	
	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("userDTO", new UserDTO());
		return "register";
	}
	
	@GetMapping("/landingPage")
	public String showLandingPage(Model model, HttpSession session) {

		model.addAttribute("recentTransfers", transferService.findRecentTransfers(session));

		String username = (String) session.getAttribute("username");
		User user = userService.findUserByUsername(username);

		if (user != null) {
			model.addAttribute("user", user);
			return "landingPage";
		} else {
			model.addAttribute("errorUserNotFound", "The User could not be found.");
			return "redirect:/";
		}

	}
	@GetMapping("/transfer")
	public String showTransferPage(Model model){
		model.addAttribute("transferDTO", new TransferDTO());
		return "transfer";
	}

	@GetMapping("/personalData")
	public String showPersonalDataPage(Model model) {
		model.addAttribute("userDTO", new UserDTO());
		return "personalData";
	}
	
	@PostMapping("/")
	public String processLogin(Model model, UserDTO user, HttpSession session) {
		User storedUser = userService.findUserByUsername(user.getUsername());
		if(storedUser == null) {
			model.addAttribute("errorUserDoesNotExist", "Unknown Username.");
			return "index";
		}

		final String hashedInputPwd = util.hashPassword(user.getPassword());
		final String hashedPwd = storedUser.getPassword();
        assert hashedInputPwd != null;
        if(!hashedInputPwd.equals(hashedPwd)) {
			model.addAttribute("errorFalsePassword", "Password is not correct.");
			return "index";
		}
		session.setAttribute("username", storedUser.getUsername());
		return "redirect:/landingPage";
	}
	
	@PostMapping("/register")
	public String processRegister(UserDTO user, Model model) {

		if (userService.findUserByUsername(user.getUsername()) != null) {
			model.addAttribute("error", "Username already in use.");
			return "register";
		}
		
		if (user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getEmail().isEmpty()) {
			model.addAttribute("errorEmptyField", "The Field can not be empty.");
			return "register";
		}
		
		if (!user.getPassword().equals(user.getRepeatPassword())) {
			model.addAttribute("errorRepeatPassword", "Password und Password repeat have to be equal.");
			return "register";
		}

		if (user.getBirthDate() == null) {
			model.addAttribute("errorBirthDateNull", "The Birth Date is not in the correct Format.");
			return "index";
		}

		final String hashPassword = util.hashPassword(user.getPassword());
		user.setPassword(hashPassword);

		String generatedIBAN = GenerateIBAN.generateIBAN("DE", "12345678");
		user.setIBAN(generatedIBAN);

		user.setBalance(100.00);
		
		userService.createUser(user);
		
		return "index";
	}

	@PostMapping("/transfer")
	public String processTransfer(TransferDTO transfer, Model model, HttpSession session) {

		//Existiert targetUser?
		if(transfer.getTargetUser() == null || userService.findUserByUsername(transfer.getTargetUser().getUsername()) == null) {
			model.addAttribute("errorTargetUserDoesNotExist", "Target User could not be found.");
			return "transfer";
		}
		String targetUsername = transfer.getTargetUser().getUsername();
		User targetUser = userService.findUserByUsername(targetUsername);

		//Überprüfung auf Match von Target User und Target IBAN
		if(!transfer.getTargetIBAN().equals(targetUser.getIBAN())) {
			model.addAttribute("errorTargetIBANDoNotMatch", "Target and Target IBAN do not match.");
			return "transfer";
		}

		//Überprüfung auf Positivität des Geldbetrages
		if(transfer.getTransferAmount() <= 0) {
			model.addAttribute("errorAmountIsNegative", "Amount of Money has to be positive.");
			return "transfer";
		}

		String loggedUsername = (String) session.getAttribute("username");
		User loggedUser = userService.findUserByUsername(loggedUsername);

		if(loggedUser.getBalance() - transfer.getTransferAmount() < 0) {
			model.addAttribute("errorAccountBalanceTooLow", "You have not enough Money.");
			return "transfer";
		}

		transfer.setSenderUser(loggedUser);
		transfer.setSenderIBAN(loggedUser.getIBAN());
		transfer.setTargetUser(targetUser);
		transfer.setTargetIBAN(transfer.getTargetIBAN());
		transfer.setTransferAmount(transfer.getTransferAmount());
		transfer.setPurpose(transfer.getPurpose());

		loggedUser.setBalance(loggedUser.getBalance() - transfer.getTransferAmount());
		targetUser.setBalance(targetUser.getBalance() + transfer.getTransferAmount());

		transferService.createTransfer(transfer);

		return "redirect:/landingPage";
	}

	@PostMapping("/personalData")
	public String personalData(HttpSession session, UserDTO userDTO, Model model) {

		if(userDTO.getPassword().isEmpty() || userDTO.getEmail().isEmpty()) {
			model.addAttribute("errorEmptyField", "Field can not by empty.");
			return "personalData";
		}

		User existingUser = userService.findUserByUsername(session.getAttribute("username").toString());

		userService.updateUser(userDTO, existingUser);

		return "index";
	}
	
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "redirect:/";
	}
}
