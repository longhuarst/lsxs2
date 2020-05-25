package com.lsxs.oauth.repository;


import com.lsxs.oauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {


    User findByUsername(String username);

    User findByMail(String mail);


}
