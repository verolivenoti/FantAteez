package com.capitaneriadiporto.fantateez;

import com.capitaneriadiporto.fantateez.Utils.Counter;
import com.capitaneriadiporto.fantateez.entity.*;
import com.capitaneriadiporto.fantateez.repository.CaptainRepository;
import com.capitaneriadiporto.fantateez.repository.TeamRepositoryImpl;
import com.capitaneriadiporto.fantateez.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;

@Controller
public class TeamController {

    @Autowired
    private TeamRepositoryImpl teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CaptainRepository captainRepository;

    public boolean checkUser(String token){
        boolean userLogged = false;

        List<Users> users = userRepository.findAll();

        for(Users u: users){
            if (token.equals(u.getToken())) {
                userLogged = true;
                break;
            }
        }

        return userLogged;
    }

    @PostMapping("/newTeam")
    public String newTeam(TeamsHelper teams, RedirectAttributes redirectAttrs, HttpServletRequest request){

        String token = "";

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie c: cookies){
                if(c.getName().equals("JSESSIONID")){
                    token = c.getValue();
                }
            }
        }

        boolean isUserLogged = checkUser(token);

        if(!isUserLogged){
            return "redirect:/login";
        }

        Teams team = new Teams();

        for(String member: teams.getMemberName()){
            int idUser = teams.getIdUser();
            team.setIdUser(teams.getIdUser());
            team.setTeamName(teams.getTeamName());
            team.setMemberName(member);
            if(teams.getCaptain().equals(member)) {
                team.setCaptain(true);
                teamRepository.insertCaptain(team.getMemberName(), idUser);
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

        boolean isUserLogged = checkUser(token);

        if(!isUserLogged){
            return "redirect:/login";
        }

        List<Scores> scores = teamRepository.teamWithMembersNameAndScores(token);
        int totalScore = 0;
        String teamName = "";
        for (Scores s : scores) {
            teamName = s.getTeam_name();
            totalScore += s.getScore();
        }
        model.addAttribute("header", teamName + " punteggio totale: " + totalScore);
        model.addAttribute("scores", scores);
        return "yourTeamPage";
    }

    @GetMapping("/bonusList")
    public String bonusList(Model model, HttpServletRequest request){
        String token = "";

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie c: cookies){
                if(c.getName().equals("JSESSIONID")){
                    token = c.getValue();
                }
            }
        }

        boolean isUserLogged = checkUser(token);

        if(!isUserLogged){
            return "redirect:/login";
        }

        List<Bonuses> bonuses = teamRepository.findAllBonuses();
        model.addAttribute("bonuses", bonuses);
        return "bonusList";
    }

    @GetMapping("/classifica")
    public String classifica(Model model, HttpServletRequest request){
        String token = "";

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie c: cookies){
                if(c.getName().equals("JSESSIONID")){
                    token = c.getValue();
                }
            }
        }

        boolean isUserLogged = checkUser(token);

        if(!isUserLogged){
            return "redirect:/login";
        }

        List<UserPlacing> userPlacing = teamRepository.selectUserPlacing();
        List<Captain> captains = captainRepository.findAll();
        for(UserPlacing up: userPlacing){
            for(Captain c: captains){
                if(up.getId() == c.getIdUser()){
                    up.setScore(up.getScore()+c.getPoints());
                }
            }
        }
        userPlacing.sort(Comparator.comparing(UserPlacing::getScore).reversed());
        List<Members> membersPlacing = teamRepository.selectAllOrderByScore();
        model.addAttribute("counter2", new Counter());
        model.addAttribute("counter", new Counter());
        model.addAttribute("membersPlacing", membersPlacing);
        model.addAttribute("userPlacing", userPlacing);
        return "placings";
    }

}
