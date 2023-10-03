package com.capitaneriadiporto.fantateez;

import com.capitaneriadiporto.fantateez.entity.Members;
import com.capitaneriadiporto.fantateez.entity.Teams;
import com.capitaneriadiporto.fantateez.entity.Users;
import com.capitaneriadiporto.fantateez.repository.MemberRepository;
import com.capitaneriadiporto.fantateez.repository.TeamRepository;
import com.capitaneriadiporto.fantateez.repository.TeamRepositoryImpl;
import com.capitaneriadiporto.fantateez.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public String viewHomepage(RedirectAttributes redirectAttrs, HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie c: cookies){
                if(c.getName().equals("JSESSIONID")){
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    Users users = userRepository.findByUsername(auth.getName());

                    /* The user is logged in :) */
                    redirectAttrs.addFlashAttribute("idUser", users.getId());
                    return "redirect:/dis";
                }
            }
        }

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (!(auth instanceof AnonymousAuthenticationToken)) {
//
//            Users users = userRepository.findByUsername(auth.getName());
//
//            /* The user is logged in :) */
//            redirectAttrs.addFlashAttribute("idUser", users.getId());
//            return "redirect:/dis";
//        }

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

        redirectAttrs.addFlashAttribute("idUser", users.getId());
        return "redirect:/dis";
    }

    @GetMapping("/dis")
    public String disambiguation(@ModelAttribute("idUser") int idUser,  Model model, RedirectAttributes redirectAttrs){
        List<Teams> teams = teamRepository.findByIdUser(idUser);
        if(teams.isEmpty()){
            List<Members> members = memberRepository.findAll();
            model.addAttribute("members", members);
            model.addAttribute("teams", new TeamsHelper());
            model.addAttribute("idUser", idUser);
            return "homepage";
        }else {
            redirectAttrs.addFlashAttribute("idUser", idUser);
            return "redirect:/yourTeam";
        }
    }
}
