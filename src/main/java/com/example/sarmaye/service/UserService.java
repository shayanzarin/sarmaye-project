package com.example.sarmaye.service;

import com.example.sarmaye.models.User;
import com.example.sarmaye.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            return user.get();
        }
        else
        {
            throw new UsernameNotFoundException(String.format("%s  not found!!!!",username));
        }

    }

    public UserDetails loadById(Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if ((user.isPresent())){
            return user.get();
        }else throw new Exception("not find");
    }

    public List<User> loadAll(){
        return userRepository.findAll();
    }

    public void save (User user){
        userRepository.save(user);
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }




}
