package com.capitaneriadiporto.fantateez;

import com.capitaneriadiporto.fantateez.entity.*;
import com.capitaneriadiporto.fantateez.repository.TeamRepository;
import com.capitaneriadiporto.fantateez.repository.TeamRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TeamController {

    @Autowired
    private TeamRepositoryImpl teamRepository;

    @Autowired
    private TeamRepository tRepository;

    @PostMapping("/newTeam")
    public String newTeam(TeamsHelper teams){

        Teams team = new Teams();

        for(String member: teams.getMemberName()){
            team.setIdUser(teams.getIdUser());
            team.setTeamName(teams.getTeamName());
            team.setMemberName(member);
            if(teams.getCaptain().equals(member)) {
                team.setCaptain(true);
            }else{
                team.setCaptain(false);
            }

            teamRepository.insertTeams(team);
        }

        return "redirect:/yourTeam";
    }

    @GetMapping("/yourTeam")
    public String yourTeam(@ModelAttribute("idUser") int idUser, Model model){
        List<Scores> scores = teamRepository.teamWithMembersNameAndScores(idUser);
        int totalScore = 0;
        String teamName = "";
        for (Scores s : scores) {
            teamName = s.getTeam_name();
            totalScore += s.getScore();
        }
        model.addAttribute("header", teamName + " Punteggio totale: " + totalScore);
        model.addAttribute("scores", scores);
        return "yourTeamPage";
    }

    @GetMapping("/bonusList")
    public String bonusList(Model model){
        List<Bonuses> bonuses = teamRepository.findAllBonuses();
        model.addAttribute("bonuses", bonuses);
        return "bonusList";
    }

    @GetMapping("/classifica")
    public String classifica(Model model){
        List<UserPlacing> userPlacing = teamRepository.selectUserPlacing();
        List<Members> membersPlacing = teamRepository.selectAllOrderByScore();
        model.addAttribute("membersPlacing", membersPlacing);
        model.addAttribute("userPlacing", userPlacing);
        return "placings";
    }

    @GetMapping("/FAQ")
    public String FAQ(){
        return "";
    }
}
