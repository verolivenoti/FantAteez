package com.capitaneriadiporto.fantateez;

import com.capitaneriadiporto.fantateez.entity.Bonuses;
import com.capitaneriadiporto.fantateez.repository.BonusRepository;
import com.capitaneriadiporto.fantateez.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BonusController {

    @Autowired
    private BonusRepository bonusRepository;

    @Autowired
    private TeamRepository teamRepository;

    @PostMapping("/addPoints")
    public String addPoints(@RequestParam("member") String member, @RequestParam("bonus") String bonus, Model model){
        int rowsUpdated = bonusRepository.updatePoints(member, bonus);
        if(rowsUpdated > 0){
            model.addAttribute("message", "Punti aggiornati correttamente");
        }else{
            model.addAttribute("error", "Errore nell'aggiornare i punti");
        }

        return "adminHomepage";
    }

    @PostMapping("/newBonus")
    public String newBonus(@RequestParam("bonus") String bonus, @RequestParam("points") int points, Model model){
        Bonuses bonuses = new Bonuses();
        bonuses.setBonus(bonus);
        bonuses.setPoints(points);
        bonusRepository.save(bonuses);

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
        return "adminHomepage";
    }

    @PostMapping("/resetTeams")
    public String resetTeams(Model model){
        teamRepository.deleteAll();
        model.addAttribute("message", "Team resettati correttamente");
        return "adminHomepage";
    }
}
