package com.example.sarmaye.service;

import com.example.sarmaye.models.Request;
import com.example.sarmaye.models.User;
import com.example.sarmaye.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    EntityManager em;

    @Autowired
    RequestRepository requestRepository;


    public void save(Request request){
        requestRepository.save(request);
    }

    public List<Request> loadByUsername(String username){
        return requestRepository.findAllByUsername(username);
    }

    public List<Request> loadAll(){
        return requestRepository.findAll();
    }

    public void deleteById(Long id){
        requestRepository.deleteById(id);
    }

    public Request loadById(Long id) throws Exception {
        Optional<Request> request = requestRepository.findById(id);
        if ((request.isPresent())){
            return request.get();
        }else throw new Exception("not find");
    }
}
