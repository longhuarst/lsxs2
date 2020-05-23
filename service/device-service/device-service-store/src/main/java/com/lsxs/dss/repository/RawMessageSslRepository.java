package com.lsxs.dss.repository;
import com.lsxs.dss.entity.RawMessage;
import com.lsxs.dss.entity.RawMessageSsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawMessageSslRepository extends JpaRepository<RawMessageSsl, String>  {
}
