package com.example.sarmaye.controller;

import com.example.sarmaye.models.Request;
import com.example.sarmaye.models.Role;
import com.example.sarmaye.models.User;
import com.example.sarmaye.repository.FileRepository;
import com.example.sarmaye.service.RequestService;
import com.example.sarmaye.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RequestService requestService;

    @Autowired
    private FileRepository fileRepository;

    @GetMapping("show-users")
    public String showUsers(Model model){
        List<User> users = userService.loadAll();
        User user = (User) userService.loadUserByUsername("ADMIN");
        users.remove(user);
        model.addAttribute("users", users);
        return "/admin/show-users";
    }

    @GetMapping("change-status/{id}")
    public String changeStatus(@PathVariable("id") long id, Model model) throws Exception {
        User user = (User) userService.loadById(id);
        if (user.getStatus()==0)
            user.setStatus(1);
        else user.setStatus(0);
        userService.save(user);
        return showUsers(model);
    }

    @GetMapping("add-assistant")
    public String addAssistant(User user){
        return "/admin/add-assistant";
    }

    @PostMapping(value = "/register")
    public String getRegister(@Valid User user, BindingResult result, Model model){
        if (result.hasErrors()){
            return "login-error";
        }
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.grantAuthority(Role.ASSISTANT);
        user.setStatus(1);
//        User user2 = new User();
//        user2.setUsername(user.getUsername());
//        user2.setPassword(passwordEncoder.encode(user.getPassword()));
//        user2.grantAuthority(Role.USER);
        userService.save(user);
        return showUsers(model);
    }

}
