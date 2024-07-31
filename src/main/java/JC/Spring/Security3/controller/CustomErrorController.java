package JC.Spring.Security3.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/roleInsufficient")
    public String roleInsufficient() {
        return "roleInsufficient"; // Return the name of your generic error view
    }

    @RequestMapping("/error")
    public String handleError() {
        return "error"; // Return the name of your generic error view
    }
}