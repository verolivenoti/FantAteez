package com.capitaneriadiporto.fantateez.entity;

public class Bonuses {

    private String bonus;

    private int points;

    public Bonuses(){}

    public Bonuses(String bonus, int points) {
        this.bonus = bonus;
        this.points = points;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
