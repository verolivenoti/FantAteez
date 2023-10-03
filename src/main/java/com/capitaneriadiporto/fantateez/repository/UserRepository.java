package com.capitaneriadiporto.fantateez.repository;

import com.capitaneriadiporto.fantateez.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {

    public Users findByUsername(String username);

    @Query(value = "select * from users where username = ?1", nativeQuery = true)
    Optional<Users> findUserByName(String name);
}
