package com.capitaneriadiporto.fantateez.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bonuses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bonuses {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String bonus;

    private int points;


}
