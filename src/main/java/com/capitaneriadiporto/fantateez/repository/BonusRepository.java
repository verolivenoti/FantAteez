package com.capitaneriadiporto.fantateez.repository;

import com.capitaneriadiporto.fantateez.entity.Bonuses;
import com.capitaneriadiporto.fantateez.entity.Teams;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Transactional
public interface BonusRepository extends JpaRepository<Bonuses, Integer> {

    public List<Bonuses> findAll();
    @Modifying
    @Query(value = "UPDATE members SET score=(SELECT b.points FROM bonuses b WHERE b.bonus=?2) + (SELECT score FROM members WHERE name=?1) WHERE name=?1", nativeQuery = true)
    public int updatePoints(String member, String bonus);
}
