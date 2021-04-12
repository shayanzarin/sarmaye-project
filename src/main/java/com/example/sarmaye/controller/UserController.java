package com.example.sarmaye.controller;

import com.example.sarmaye.models.*;
import com.example.sarmaye.repository.FileRepository;
import com.example.sarmaye.service.RequestService;
import com.example.sarmaye.service.UserService;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RequestService requestService;

    @Autowired
    private FileRepository fileRepository;
//    @GetMapping("/home")
//        public String goHome(){
//            return"home";
//        }
//
//    @GetMapping("/signup")
//    public String showSignUpForm(User user) {
//        return "add-user";
//    }
//
//    @PostMapping(value = "/register")
//    public String getRegister(@Valid User user, BindingResult result, Model model){
//        if (result.hasErrors()){
//            return "add-user";
//        }
//        userService.save(user);
//        return "redirect:/home";
//    }
    @GetMapping("/home")
    public String goHome(Model model, Authentication authentication){
        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "user-home";
    }

    @GetMapping("/show")
    public String showProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = (User) userService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "/user/profile";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) throws Exception {
        User user = (User) userService.loadById(id);
        model.addAttribute("username", user.getUsername());
        userService.deleteById(id);
        return "/delete-page";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) throws Exception {
        User user = (User) userService.loadById(id);
        model.addAttribute("user", user);
        return "/user/edit-page";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user,
                             BindingResult result, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            user.setId(id);
            return "/user/edit-page";
        }
//        String password = user.getPassword();
//        user.setPassword(passwordEncoder.encode(password));
        user.grantAuthority(Role.USER);
        user.setStatus(1);
        userService.save(user);
        return goHome(model,authentication);
    }

    @GetMapping("/change-password/{id}")
    public String changePasswordForm(@PathVariable("id") long id, Model model) throws Exception {
        User user = (User) userService.loadById(id);
        model.addAttribute("user", user);
        return "/user/edit-password";
    }

    @PostMapping("/update-password/{id}")
    public String updatePassword(@PathVariable("id") long id, @Valid User user,
                                 BindingResult result, Model model, Authentication authentication ){
        if (user.getPassword().equals(user.getRetypePassword())){
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
            user.grantAuthority(Role.USER);
            user.setStatus(1);
            userService.save(user);
            return goHome(model,authentication);
        }else {
            model.addAttribute("error", "error");
        return "/user/edit-password";
        }
    }

//    @GetMapping("/test")
//    public String test(){
//        return "pro";
//    }

    @GetMapping("/add-request")
    public String showRequestForm(Request request) {
        return "/user/add-request";
    }

    @PostMapping("/save-request")
    public String saveRequest(@Valid Request request, BindingResult result, Model model, Authentication authentication,
                                @RequestParam("document") MultipartFile[] files) throws Exception {

        if (result.hasErrors()) {
            return "/user/edit-page";
        }

        String username = authentication.getName();
        request.setUsername(username);
        request.setStatus(Status.IN_PROGRESS);
        requestService.save(request);

        Set<File> oldFiles =  requestService.loadById(request.getId()).getFiles();
        if (oldFiles != null && oldFiles.size() !=0)
            oldFiles.stream()
            .forEach(file -> fileRepository.delete(file));

        if (files[0].getSize() != 0){
            Set<File> documentFiles = Arrays.stream(files)
                    .map(file -> {
                        File document = new File();
                        document.setName(StringUtils.cleanPath(file.getOriginalFilename()));
                        try {
                            document.setContent(file.getBytes());
                        }catch (IOException e ){
                            e.printStackTrace();
                        }
                        document.setSize(file.getSize());
                        document.setRequest(request);
                        return document;
                    })
                    .peek(file -> fileRepository.save(file))
                    .collect(Collectors.toSet());
        }

        List<Request> requestList = requestService.loadByUsername(username);
        model.addAttribute("requestList", requestList);
        return "/user/request";
    }

    @GetMapping("/show-request")
    public String showRequest(Model model, Authentication authentication){
        String username = authentication.getName();
        List<Request> requestList = requestService.loadByUsername(username);
        model.addAttribute("requestList", requestList);
        return "/user/request";
    }

    @GetMapping("/delete-request/{id}")
    public String deleteRequest(@PathVariable("id") long id, Authentication authentication, Model model) throws Exception {
        if (requestService.loadById(id).getStatus().equals(Status.IN_PROGRESS)) {
            requestService.deleteById(id);
            String username = authentication.getName();
            List<Request> requestList = requestService.loadByUsername(username);
            model.addAttribute("requestList", requestList);
            return showRequest(model,authentication);
        }
        return showRequest(model,authentication);
    }

    @GetMapping("/edit-request/{id}")
    public String editRequest(@PathVariable("id") long id, Model model, Authentication authentication) throws Exception {
        if (requestService.loadById(id).getStatus().equals(Status.IN_PROGRESS)) {
            Request request = requestService.loadById(id);
            model.addAttribute("request", request);
            return "/user/edit-request";
        }else return showRequest(model, authentication);
    }

    @PostMapping("/update-request/{id}")
    public String updateRequest(@PathVariable("id") long id, @Valid Request request, Authentication authentication,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            request.setId(id);
            return showRequest(model, authentication);
        }
        String username = authentication.getName();
        request.setStatus(Status.IN_PROGRESS);
        request.setUsername(username);

        List<Request> requestList = requestService.loadByUsername(username);
        model.addAttribute("requestList", requestList);
        requestService.save(request);

        return showRequest(model, authentication);
    }

    @GetMapping("/show-file/{id}")
    public String showFile(@PathVariable("id") long id, Model model){
        List<File> files = fileRepository.findAllByRequestId(id);
        model.addAttribute("files", files);
        return "/user/show-file";
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

    @GetMapping("/close-request/{id}")
    public String closeRequest(@PathVariable("id") long id, Model model, Authentication authentication) throws Exception {
        Request request = requestService.loadById(id);
        if (request.getStatus() == Status.ANSWERED){
            request.setStatus(Status.CLOSE);
            requestService.save(request);
        }
        return showRequest(model, authentication);
      }
}
