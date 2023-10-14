package com.capitaneriadiporto.fantateez.repository;

import com.capitaneriadiporto.fantateez.entity.Captain;
import com.capitaneriadiporto.fantateez.entity.Members;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface CaptainRepository extends JpaRepository<Captain, String> {

    @Modifying
    @Query(value = "UPDATE captains SET points=(SELECT DISTINCT points FROM captains WHERE member_name=?1) - ((SELECT points FROM bonuses WHERE bonus=?2)*2) " +
            "WHERE member_name=?1", nativeQuery = true)
    public void updateCaptainPointsAfterDeletion(String member, String bonus);
}
