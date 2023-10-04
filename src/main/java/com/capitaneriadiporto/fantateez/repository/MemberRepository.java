package com.capitaneriadiporto.fantateez.repository;

import com.capitaneriadiporto.fantateez.entity.Members;
import com.capitaneriadiporto.fantateez.entity.Teams;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface MemberRepository extends JpaRepository<Members, String> {

    @Modifying
    @Query(value = "UPDATE members SET score=0",  nativeQuery = true)
    public void setScoresToZero();
}
