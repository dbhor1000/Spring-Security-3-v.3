package JC.Spring.Security3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home"; // Return the name of your home view
    }

    @GetMapping("/")
    public String start() {
        return "start"; // Return the name of your home view
    }
}