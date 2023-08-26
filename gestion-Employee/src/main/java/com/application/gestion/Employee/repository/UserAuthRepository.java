package com.application.gestion.Employee.repository;

import com.application.gestion.Employee.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
  UserAuth findByFirstname(String name);
}

