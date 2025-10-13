package com.study.StudyUp.controller;

import com.study.StudyUp.DTOs.UserRegistrationDto;
import com.study.StudyUp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FrontendController {

    private final UserService userService;

    public FrontendController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto()); // ✅ correct
        return "register";
    }

    //@PostMapping → handles form submission
    //@Valid → triggers backend validation for UserRegistrationDto
    //@ModelAttribute("userRegistrationDto") → binds form data
    //@BindingResult → captures validation errors
    //@RedirectAttributes → stores flash messages across redirect
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("userRegistrationDto") UserRegistrationDto userDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            // Preserve form data and validation errors
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationDto", result);
            redirectAttributes.addFlashAttribute("userRegistrationDto", userDto);
            return "redirect:/register";
        }

        if (userService.existsByUsername(userDto.getUsername()) || userService.existsByEmail(userDto.getUsername())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username or email is already registered");
            redirectAttributes.addFlashAttribute("userRegistrationDto", userDto);
            return "redirect:/register";
        }

        userService.registerUser(userDto);

        // Add success message as flash attribute
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! You can now log in.");
        return "redirect:/register-success";
    }

    @GetMapping("/register-success")
    public String registrationSuccess() {
        return "register-success"; // Create this Thymeleaf template
    }
}
