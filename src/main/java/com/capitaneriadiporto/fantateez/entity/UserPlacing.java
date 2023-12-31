package com.capitaneriadiporto.fantateez.entity;


public class UserPlacing {

    private String username;

    private String team_name;

    private Long score;

    private int id;

    public UserPlacing(){}

    public UserPlacing(String username, String team_name, Long score) {
        this.username = username;
        this.team_name = team_name;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTeamName() {
        return team_name;
    }

    public void setTeamName(String team_name) {
        this.team_name = team_name;
    }

    public Long getPoints() {
        return score;
    }

    public void setPoints(Long score) {
        this.score = score;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
