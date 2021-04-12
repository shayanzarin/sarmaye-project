//package com.example.sarmaye;
//
//import com.example.sarmaye.models.*;
//import com.example.sarmaye.repository.RequestRepository;
//import com.example.sarmaye.repository.UserRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MyCommandLineRunner implements CommandLineRunner {
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RequestRepository requestRepository;
//
//
//    @Override
//    public void run(String... args) throws Exception {
//        User user = new User();
//        user.setUsername("user");
//        user.setEmail("yahoo@gmail.com");
//        user.setFirstName("UserTest");
//        user.setLastName("testy");
//        user.setPhoneNum("0912021");
//        user.setNationalCode("007");
//        user.setStatus(1);
//        user.setPassword(passwordEncoder.encode("123456"));
//        user.grantAuthority(Role.USER);
//
//
//        User user2 = new User();
//        user2.setUsername("admin");
//        user2.setEmail("yahoo@gmail.com");
//        user2.setFirstName("test");
//        user2.setLastName("testy");
//        user2.setPhoneNum("0912021");
//        user2.setNationalCode("007");
//        user2.setStatus(1);
//        user2.setPassword(passwordEncoder.encode("123456"));
//        user2.grantAuthority(Role.ADMIN);
//
//        User user3 = new User();
//        user3.setUsername("hasan");
//        user3.setEmail("yahoo1@gmail.com");
//        user3.setFirstName("hamal");
//        user3.setLastName("hamal");
//        user3.setPhoneNum("0912021");
//        user3.setNationalCode("007");
//        user3.setStatus(1);
//        user3.setPassword(passwordEncoder.encode("123456"));
//        user3.grantAuthority(Role.ASSISTANT);
//
//        Request request = new Request();
//        request.setSubject(Subject.SUBJECT1);
//        request.setStatus(Status.IN_PROGRESS);
//        request.setDescription("test1");
//        request.setUsername("user");
//
//        requestRepository.save(request);
//        userRepository.save(user3);
//        userRepository.save(user);
//        userRepository.save(user2);
//    }
//}
