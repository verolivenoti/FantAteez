package com.capitaneriadiporto.fantateez.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "log_bonus")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log_Bonus {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String bonus;

    @CurrentTimestamp
    private Timestamp date;

    private String member;
}
