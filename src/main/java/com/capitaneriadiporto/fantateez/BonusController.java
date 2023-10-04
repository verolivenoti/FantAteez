package com.capitaneriadiporto.fantateez;

import com.capitaneriadiporto.fantateez.repository.BonusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BonusController {

    @Autowired
    private BonusRepository bonusRepository;

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
}
