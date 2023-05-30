package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ImageRepository extends JpaRepository<Image, String> {
    List<Image> findByUsername(@Param("username") String username);
    Optional<Image> findById(@Param("id") String id);

}
