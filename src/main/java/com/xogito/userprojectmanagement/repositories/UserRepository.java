package com.xogito.userprojectmanagement.repositories;

import com.xogito.userprojectmanagement.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameContainingOrEmailContaining(String name, String email, Pageable pageable);

}