package com.surfer.codes.url_shortener;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // This will return the index.html file in the templates directory
    }
}
