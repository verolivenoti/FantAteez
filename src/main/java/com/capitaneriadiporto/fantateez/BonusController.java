package com.capitaneriadiporto.fantateez;

import com.capitaneriadiporto.fantateez.entity.Bonuses;
import com.capitaneriadiporto.fantateez.entity.Members;
import com.capitaneriadiporto.fantateez.repository.BonusRepository;
import com.capitaneriadiporto.fantateez.repository.MemberRepository;
import com.capitaneriadiporto.fantateez.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BonusController {

    @Autowired
    private BonusRepository bonusRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/addPoints")
    public String addPoints(@RequestParam("member") String member, @RequestParam("bonus") String bonus, Model model){
        int rowsUpdated = bonusRepository.updatePoints(member, bonus);
        if(rowsUpdated > 0){
            model.addAttribute("message", "Punti aggiornati correttamente");
        }else{
            model.addAttribute("error", "Errore nell'aggiornare i punti");
        }
        List<Bonuses> bonuses = bonusRepository.findAll();
        List<Members> members = memberRepository.findAll();
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
        model.addAttribute("bonuses", bonuses);
        model.addAttribute("members", members);
        return "adminHomepage";
    }

    @PostMapping("/resetTeams")
    public String resetTeams(Model model){
        teamRepository.deleteAll();
        memberRepository.setScoresToZero();
        List<Bonuses> bonuses = bonusRepository.findAll();
        List<Members> members = memberRepository.findAll();
        model.addAttribute("bonuses", bonuses);
        model.addAttribute("members", members);
        model.addAttribute("message", "Team resettati correttamente");
        return "adminHomepage";
    }
}
