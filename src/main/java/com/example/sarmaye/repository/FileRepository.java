package com.example.sarmaye.repository;

import com.example.sarmaye.models.File;
import com.example.sarmaye.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByRequestId(Long id);
}
