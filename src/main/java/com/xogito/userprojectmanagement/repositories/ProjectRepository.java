package com.xogito.userprojectmanagement.repositories;

import com.xogito.userprojectmanagement.entities.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByNameContaining(String projectNameQuery, Pageable pageable);

}