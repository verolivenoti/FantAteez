package com.capitaneriadiporto.fantateez.repository;

import com.capitaneriadiporto.fantateez.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<Users, Integer> {

    public Users findByUsername(String username);

    public Users findByToken(String token);

    @Query(value = "select * from users where username = ?1", nativeQuery = true)
    Optional<Users> findUserByName(String name);

    @Modifying
    @Query(value = "UPDATE users SET token=:token WHERE id=:id", nativeQuery = true)
    void saveToken(@Param("token")String token, @Param("id") int idUser);
}
