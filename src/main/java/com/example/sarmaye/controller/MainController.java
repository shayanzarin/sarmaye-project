package com.example.sarmaye.controller;

import com.example.sarmaye.models.Role;
import com.example.sarmaye.models.User;
import com.example.sarmaye.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String goHome(){
        return"home";
    }

    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "add-user";
    }

    @PostMapping(value = "/register")
    public String getRegister(@Valid User user, BindingResult result, Model model){
        if (result.hasErrors()){
            return "login-error";
        }
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.grantAuthority(Role.USER);
        user.setStatus(1);
//        User user2 = new User();
//        user2.setUsername(user.getUsername());
//        user2.setPassword(passwordEncoder.encode(user.getPassword()));
//        user2.grantAuthority(Role.USER);
        userService.save(user);
        return "/login";
    }

    @GetMapping(value = "/fail")
    public String failLogin(Authentication authentication, Model model){
//        String username = authentication.getName();
//        User user = (User) userService.loadUserByUsername(username);
//        if (user.getStatus() == 0)
//            model.addAttribute("error", "error");
        return "login-error";
    }

    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }


    @GetMapping("/success")
    public String successUrl(Model model, Authentication authentication){
        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);
        if (user.getStatus()==0){
            return "false-status";
        }
        model.addAttribute("user", user);

        return "user-home";
    }

}
