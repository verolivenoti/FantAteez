package com.capitaneriadiporto.fantateez;

import com.capitaneriadiporto.fantateez.entity.*;
import com.capitaneriadiporto.fantateez.repository.TeamRepository;
import com.capitaneriadiporto.fantateez.repository.TeamRepositoryImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TeamController {

    @Autowired
    private TeamRepositoryImpl teamRepository;

    @Autowired
    private TeamRepository tRepository;

    @PostMapping("/newTeam")
    public String newTeam(TeamsHelper teams, RedirectAttributes redirectAttrs){

        Teams team = new Teams();

        for(String member: teams.getMemberName()){
            int idUser = teams.getIdUser();
            team.setIdUser(teams.getIdUser());
            team.setTeamName(teams.getTeamName());
            team.setMemberName(member);
            if(teams.getCaptain().equals(member)) {
                team.setCaptain(true);
            }else{
                team.setCaptain(false);
            }

            redirectAttrs.addFlashAttribute("idUser", idUser);
            teamRepository.insertTeams(team);
        }

        return "redirect:/yourTeam";
    }

    @GetMapping("/yourTeam")
    public String yourTeam(Model model, HttpServletRequest request){
        String token = "";

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie c: cookies){
                if(c.getName().equals("JSESSIONID")){
                    token = c.getValue();
                }
            }
        }
        List<Scores> scores = teamRepository.teamWithMembersNameAndScores(token);
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
