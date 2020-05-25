package com.lsxs.oauth.repository;

import com.lsxs.oauth.entity.EmailValid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailValidRepository extends JpaRepository<EmailValid, String> {
}
