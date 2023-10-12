package com.capitaneriadiporto.fantateez.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "captains")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Captain {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private int idUser;

    private String member_name;

    private int points;
}
