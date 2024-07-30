package JC.Spring.Security3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginRequiredController {

    @GetMapping("/loginRequired")
    public String loginRequired() {
        return "loginRequired"; // Return the name of your custom login required view
    }
}