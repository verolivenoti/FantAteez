package com.capitaneriadiporto.fantateez.repository;

import com.capitaneriadiporto.fantateez.entity.Bonuses;
import com.capitaneriadiporto.fantateez.entity.Log_Bonus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
@Transactional
public interface LogRepository extends JpaRepository<Log_Bonus, Integer> {
}
