package com.capitaneriadiporto.fantateez.repository;

import com.capitaneriadiporto.fantateez.entity.Captain;
import com.capitaneriadiporto.fantateez.entity.Members;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface CaptainRepository extends JpaRepository<Captain, String> {
}
