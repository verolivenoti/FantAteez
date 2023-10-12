package com.capitaneriadiporto.fantateez;

import com.capitaneriadiporto.fantateez.entity.Bonuses;
import com.capitaneriadiporto.fantateez.entity.Members;
import com.capitaneriadiporto.fantateez.entity.Teams;
import com.capitaneriadiporto.fantateez.entity.Users;
import com.capitaneriadiporto.fantateez.repository.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BonusRepository bonusRepository;

    @GetMapping("")
    public String viewHomepage(RedirectAttributes redirectAttrs, HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie c: cookies){
                if(c.getName().equals("JSESSIONID")){
                    Users users = userRepository.findByToken(c.getValue());

                    if(users == null){
                        return "redirect:/login";
                    }else{
                        /* The user is logged in :) */
                        redirectAttrs.addFlashAttribute("idUser", users.getId());
                        return "redirect:/dis";
                    }

                }
            }
        }

        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("users", new Users());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(Users users, Model model) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(users.getPassword());
        users.setPassword(encodedPassword);
        users.setRole("USER");

        String existingUsername = userRepository.selectUsername(users.getUsername());
        if(existingUsername!=null){
            model.addAttribute("error", "Lo username scelto non è disponibile");
            return "signup_form";
        }
        if(users.getUsername().contains(" ")){
            model.addAttribute("error", "Lo username non può contenere spazi bianchi");
            return "signup_form";
        }
        String existingMail = userRepository.selectEmail(users.getEmail());
        if(existingMail!=null){
            model.addAttribute("error", "Lo mail inserita è già stata usata");
            return "signup_form";
        }

        userRepository.save(users);

        return "redirect:/login";
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

        if(users.getRole().equals("ADMIN")){
            return "redirect:/adminPage";
        }

        redirectAttrs.addFlashAttribute("idUser", users.getId());
        return "redirect:/dis";
    }

    @GetMapping("/adminPage")
    public String adminPage(Model model){
        List<Bonuses> bonuses = bonusRepository.findAll();
        List<Members> members = memberRepository.findAll();
        model.addAttribute("bonuses", bonuses);
        model.addAttribute("members", members);
        return "adminHomepage";
    }

    @GetMapping("/dis")
    public String disambiguation(@ModelAttribute("idUser") int idUser,  Model model, HttpServletRequest request){
        String token = "";

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie c: cookies){
                if(c.getName().equals("JSESSIONID")){
                    token = c.getValue();
                }
            }
        }

        userRepository.saveToken(token, idUser);
        List<Teams> teams = teamRepository.findByIdUser(idUser);
        if(teams.isEmpty()){
            List<Members> members = memberRepository.findAll();
            model.addAttribute("members", members);
            model.addAttribute("teams", new TeamsHelper());
            model.addAttribute("idUser", idUser);
            return "homepage";
        }else {
            return "redirect:/yourTeam";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response){
        Cookie cookie = new Cookie("JSESSIONID", null); // Not necessary, but saves bandwidth.
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }
}
