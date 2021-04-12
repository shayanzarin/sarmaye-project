package com.example.sarmaye.repository;

import com.example.sarmaye.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long>{

    public List<Request> findAllByUsername(String username);
            
    @Override
    Optional<Request> findById(Long aLong);

    void deleteById(Long id);
}
