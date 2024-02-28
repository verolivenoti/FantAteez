package com.capitaneriadiporto.fantateez;

import com.capitaneriadiporto.fantateez.Utils.Counter;
import com.capitaneriadiporto.fantateez.entity.*;
import com.capitaneriadiporto.fantateez.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
public class BonusController {

    @Autowired
    private BonusRepository bonusRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamRepositoryImpl teamImplRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private CaptainRepository captainRepository;

    @PostMapping("/addPoints")
    public String addPoints(@RequestParam("member") String member, @RequestParam("bonus") String bonus, Model model){
        int rowsUpdated = bonusRepository.updatePoints(member, bonus);
        int captainUpdate = bonusRepository.updateCaptain(member,bonus);
        Log_Bonus logBonus = new Log_Bonus();
        logBonus.setMember(member);
        logBonus.setBonus(bonus);
        logRepository.save(logBonus);
        if(rowsUpdated > 0){
            model.addAttribute("message", "Punti aggiornati correttamente");
        }else{
            model.addAttribute("error", "Errore nell'aggiornare i punti");
        }
        List<Bonuses> bonuses = bonusRepository.findAll();
        List<Members> members = memberRepository.findAll();
        List<Log_Bonus> logBonuses = logRepository.findAllOrderByDate();
        model.addAttribute("logBonus", logBonuses);
        model.addAttribute("bonuses", bonuses);
        model.addAttribute("members", members);
        return "adminHomepage";
    }

    @PostMapping("/newBonus")
    public String newBonus(@RequestParam("bonus") String bonus, @RequestParam("points") int points, Model model){
        Bonuses bonuses = new Bonuses();
        bonuses.setBonus(bonus);
        bonuses.setPoints(points);
        bonusRepository.save(bonuses);

        List<Bonuses> bonuses2 = bonusRepository.findAll();
        List<Members> members = memberRepository.findAll();
        List<Log_Bonus> logBonuses = logRepository.findAllOrderByDate();
        model.addAttribute("logBonus", logBonuses);
        model.addAttribute("bonuses", bonuses2);
        model.addAttribute("members", members);
        model.addAttribute("message", "Nuovo bonus inserito correttamente");
        return "adminHomepage";
    }

    @PostMapping("/deleteBonus")
    public String deleteBonus(@RequestParam("bonus") String bonus, Model model){
        int rowDelete = bonusRepository.deleteByBonus(bonus);

        if(rowDelete > 0){
            model.addAttribute("message", "Bonus eliminato correttamente");
        }else{
            model.addAttribute("error", "Errore nell'eliminazione del bonus");
        }
        List<Bonuses> bonuses = bonusRepository.findAll();
        List<Members> members = memberRepository.findAll();
        List<Log_Bonus> logBonuses = logRepository.findAllOrderByDate();
        model.addAttribute("logBonus", logBonuses);
        model.addAttribute("bonuses", bonuses);
        model.addAttribute("members", members);
        return "adminHomepage";
    }

    @GetMapping("/resetTeams")
    public String resetTeams(Model model){
        teamRepository.deleteAll();
        captainRepository.deleteAll();
        memberRepository.setScoresToZero();
        List<Bonuses> bonuses = bonusRepository.findAll();
        List<Members> members = memberRepository.findAll();
        List<Log_Bonus> logBonuses = logRepository.findAllOrderByDate();
        model.addAttribute("logBonus", logBonuses);
        model.addAttribute("bonuses", bonuses);
        model.addAttribute("members", members);
        model.addAttribute("message", "Team resettati correttamente");
        return "adminHomepage";
    }

    @RequestMapping("/deleteInsert/{id}/{bonus}/{member}")
    public String deleteInsert(@PathVariable String bonus, @PathVariable int id, @PathVariable String member, Model model){

        logRepository.deleteById(id);

        int updatedRowsMember = memberRepository.updatePointsAfterDeletion(member, bonus);

        if(updatedRowsMember==0){
            model.addAttribute("error", "Errore nell'aggiornamento dei punti");
            return "adminHomepage";
        }

        captainRepository.updateCaptainPointsAfterDeletion(member, bonus);

        List<Bonuses> bonuses = bonusRepository.findAll();
        List<Members> members = memberRepository.findAll();
        List<Log_Bonus> logBonuses = logRepository.findAllOrderByDate();
        model.addAttribute("logBonus", logBonuses);
        model.addAttribute("bonuses", bonuses);
        model.addAttribute("members", members);
        model.addAttribute("message", "Bonus annullato e punti aggiornati correttamente");

        return "adminHomepage";
    }

    @GetMapping("/classificaUtentiMembri")
    public String classificaUtentiEMembri(Model model){
        List<UserPlacing> userPlacing = teamImplRepository.selectUserPlacing();
        List<Captain> captains = captainRepository.findAll();
        for(UserPlacing up: userPlacing){
            for(Captain c: captains){
                if(up.getId() == c.getIdUser()){
                    up.setScore(up.getScore()+c.getPoints());
                }
            }
        }
        userPlacing.sort(Comparator.comparing(UserPlacing::getScore).reversed());
        List<Members> membersPlacing = teamImplRepository.selectAllOrderByScore();
        model.addAttribute("counter2", new Counter());
        model.addAttribute("counter", new Counter());
        model.addAttribute("membersPlacing", membersPlacing);
        model.addAttribute("userPlacing", userPlacing);
        return "placingsAdmin";
    }
}
