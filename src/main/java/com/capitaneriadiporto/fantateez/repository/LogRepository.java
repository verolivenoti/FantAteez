package com.capitaneriadiporto.fantateez.repository;

import com.capitaneriadiporto.fantateez.entity.Bonuses;
import com.capitaneriadiporto.fantateez.entity.Log_Bonus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Transactional
public interface LogRepository extends JpaRepository<Log_Bonus, Integer> {

    @Query(value = "SELECT * FROM log_bonus ORDER BY date DESC", nativeQuery = true)
    public List<Log_Bonus> findAllOrderByDate();
}
