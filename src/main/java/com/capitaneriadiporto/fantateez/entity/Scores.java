package com.capitaneriadiporto.fantateez.entity;

public class Scores {
    public String team_name;

    public String member_name;

    public int score;

    public Scores(){}

    public Scores(String team_name, String member_name, int score) {
        this.team_name = team_name;
        this.member_name = member_name;
        this.score = score;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
