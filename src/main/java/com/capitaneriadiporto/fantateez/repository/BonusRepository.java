package com.capitaneriadiporto.fantateez.repository;

import com.capitaneriadiporto.fantateez.entity.Bonuses;
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

    @Modifying
    @Query(value = "UPDATE captains SET points=((SELECT b.points FROM bonuses b WHERE b.bonus=?2)*2) + (SELECT points FROM captains WHERE member_name=?1) WHERE member_name=?1", nativeQuery = true)
    public int updateCaptain(String member, String bonus);

    @Query(value = "", nativeQuery = true)
    public int deleteByBonus(String bonus);
}
