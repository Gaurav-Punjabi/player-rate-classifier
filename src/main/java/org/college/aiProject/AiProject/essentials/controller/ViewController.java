package org.college.aiProject.AiProject.essentials.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {
    @RequestMapping("/index")
    public String welcome() {
        return "index";
    }
}
