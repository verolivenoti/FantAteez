package com.capitaneriadiporto.fantateez.repository;

import com.capitaneriadiporto.fantateez.entity.Teams;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
public interface TeamRepository extends JpaRepository<Teams, Integer> {

    public List<Teams> findByIdUser(int idUser);

    @Query(value = "SELECT team_name FROM teams WHERE team_name=?1 GROUP BY team_name",  nativeQuery = true)
    public String selectTeamName(String teamName);

}
