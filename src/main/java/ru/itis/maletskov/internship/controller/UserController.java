package ru.itis.maletskov.internship.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itis.maletskov.internship.form.UserForm;
import ru.itis.maletskov.internship.service.UserService;
import ru.itis.maletskov.internship.util.validator.UserFormValidator;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserFormValidator userFormValidator;

    @InitBinder
    private void dataBinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(userFormValidator);
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@Validated @ModelAttribute UserForm userForm,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.createUser(userForm);
        return "redirect:/login";
    }
}
