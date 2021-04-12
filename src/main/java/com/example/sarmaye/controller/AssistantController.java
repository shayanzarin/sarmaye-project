package com.example.sarmaye.controller;

import com.example.sarmaye.models.*;
import com.example.sarmaye.repository.FileRepository;
import com.example.sarmaye.service.RequestService;
import com.example.sarmaye.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/assistant")
public class AssistantController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RequestService requestService;

    @Autowired
    private FileRepository fileRepository;

    @GetMapping("/home")
    public String goHome(Model model, Authentication authentication){
        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);
            model.addAttribute("user", user);
            return "/user-home";
    }



    @GetMapping("/show-request")
    public String showRequest(Model model){
        List<Request> requestList = requestService.loadAll();
        model.addAttribute("requestList" ,requestList);
        return "/assistant/request";
    }

    @GetMapping("/answer/{id}")
    public String answer(@PathVariable ("id") long id, Model model) throws Exception {
        Request request = requestService.loadById(id);
        model.addAttribute("request", request);
        return "/assistant/answer-page";
    }

    @PostMapping("/update-request/{id}")
    public String updateRequest(@PathVariable("id") long id, @Valid Request request
            , BindingResult result, Model model){
        if (result.hasErrors()){
            request.setId(id);
            return "/assistant/answer-page";
        }
        request.setStatus(Status.ANSWERED);
        requestService.save(request);
        return showRequest(model);
    }

    @GetMapping("/show-user/{username}")
    public String showUser(@PathVariable("username") String username, Model model ){
        User user = (User) userService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "/assistant/user-profile";
    }

    @GetMapping("/request-of-this-user/{username}")
    public String requestOfThisUser(@PathVariable("username") String username, Model model){
        List<Request> requestList = requestService.loadByUsername(username);
        model.addAttribute("requestList", requestList);
        return "/assistant/request-of-user";
    }

    @GetMapping("/show-file/{id}")
    public String showFile(@PathVariable("id") long id, Model model){
        List<File> files = fileRepository.findAllByRequestId(id);
        model.addAttribute("files", files);
        return "/assistant/show-file";
    }

    @GetMapping("/download-file/{id}")
    public void downloadFile(@PathVariable("id") long id, HttpServletResponse response) throws IOException {
        Optional<File> file = fileRepository.findById(id);
        File myFile = file.get();
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename" + myFile.getName();
        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(myFile.getContent());
        outputStream.close();
    }

}
