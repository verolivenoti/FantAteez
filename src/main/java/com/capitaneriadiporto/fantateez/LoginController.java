package com.capitaneriadiporto.fantateez;

import com.capitaneriadiporto.fantateez.entity.Members;
import com.capitaneriadiporto.fantateez.entity.Teams;
import com.capitaneriadiporto.fantateez.entity.Users;
import com.capitaneriadiporto.fantateez.repository.MemberRepository;
import com.capitaneriadiporto.fantateez.repository.TeamRepository;
import com.capitaneriadiporto.fantateez.repository.TeamRepositoryImpl;
import com.capitaneriadiporto.fantateez.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamRepositoryImpl teamImpRepository;

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("")
    public String viewHomepage(){
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("users", new Users());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(Users users) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(users.getPassword());
        users.setPassword(encodedPassword);

        userRepository.save(users);

        return "register_success";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("users", new Users());

        return "login";
    }

    @PostMapping("/process_login")
    public String processLogin(String username, String password, ModelMap model, RedirectAttributes redirectAttrs) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Users users = userRepository.findByUsername(username);
        if(users == null){
            model.addAttribute("error", "Utente non ancora registrato.");
            model.addAttribute("users", new Users());
            return "login";
        }else{
            if(!passwordEncoder.matches(password, users.getPassword())){
                model.addAttribute("credentialError", "Username o password errati");
                model.addAttribute("users", new Users());
                return "login";
            }
        }

        List<Teams> teams = teamRepository.findByIdUser(users.getId());
        if(teams.isEmpty()){
            List<Members> members = memberRepository.findAll();
            model.addAttribute("members", members);
            model.addAttribute("teams", new TeamsHelper());
            model.addAttribute("idUser", users.getId());
            return "homepage";
        }else {
            redirectAttrs.addFlashAttribute("idUser", users.getId());
            return "redirect:/yourTeam";
        }
    }
}
