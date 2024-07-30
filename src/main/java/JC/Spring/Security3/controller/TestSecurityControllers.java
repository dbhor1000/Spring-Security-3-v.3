package JC.Spring.Security3.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestSecurityControllers {

    @GetMapping("/adminOnly")
    public String adminOnly() {
        return "adminOnly"; // Return the name of your home view
    }

    @GetMapping("/highSecurity")
    public String highSecurity() {
        return "highSecurity"; // Return the name of your home view
    }

    @GetMapping("/lowSecurity")
    public String lowSecurity() {
        return "lowSecurity"; // Return the name of your home view
    }
}