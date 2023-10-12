package com.capitaneriadiporto.fantateez.repository;

import com.capitaneriadiporto.fantateez.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TeamRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public void insertTeams(Teams team) {
        entityManager.createNativeQuery("INSERT INTO teams (member_name, id_user, captain, team_name) VALUES (?,?,?,?)")
                .setParameter(1, team.getMemberName())
                .setParameter(2, team.getIdUser())
                .setParameter(3, team.isCaptain())
                .setParameter(4, team.getTeamName())
                .executeUpdate();
    }

    @Transactional
    public void insertCaptain(String member_name, int idUser){
        entityManager.createNativeQuery("INSERT INTO captains (member_name, id_user) VALUES (?,?)")
                .setParameter(1, member_name)
                .setParameter(2, idUser)
                .executeUpdate();
    }

    @Transactional
    public List<Scores> teamWithMembersNameAndScores(String token){
         return entityManager.createNativeQuery("SELECT t.team_name, t.member_name, m.score FROM teams t " +
                "LEFT JOIN members m ON m.name=t.member_name " +
                "LEFT JOIN users u ON u.id=t.id_user " +
                "WHERE u.token=? AND t.captain=false " +
                "UNION " +
                "SELECT t.team_name, c.member_name, c.points FROM captains c " +
                "LEFT JOIN users u ON u.id=c.id_user " +
                "LEFT JOIN teams t ON t.id_user=u.id " +
                "ORDER BY score DESC")
                .setParameter(1, token).unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AliasToBeanResultTransformer(Scores.class))
                .list();
    }

    @Transactional
    public List<Bonuses> findAllBonuses(){
        return entityManager.createNativeQuery("SELECT b.bonus, b.points FROM bonuses b ORDER BY b.points")
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AliasToBeanResultTransformer(Bonuses.class))
                .list();
    }

    @Transactional
    public List<UserPlacing> selectUserPlacing(){
        return entityManager.createNativeQuery("SELECT u.username, t.team_name, sum(m.score) AS score, u.id FROM users u " +
                        "RIGHT JOIN teams t ON t.id_user=u.id " +
                        "LEFT JOIN members m ON m.name=t.member_name " +
                        "WHERE u.role LIKE 'USER' AND t.captain=false " +
                        "GROUP BY u.username, t.team_name, u.id " +
                        "ORDER BY score DESC " +
                        "LIMIT 10")
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AliasToBeanResultTransformer(UserPlacing.class))
                .list();
    }

    @Transactional
    public List<Members> selectAllOrderByScore(){
        return entityManager.createNativeQuery("SELECT m.name, m.score FROM members m ORDER BY m.score DESC")
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AliasToBeanResultTransformer(Members.class))
                .list();
    }
}
