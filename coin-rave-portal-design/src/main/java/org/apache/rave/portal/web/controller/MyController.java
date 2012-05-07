package org.apache.rave.portal.web.controller;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MyController {

    @RequestMapping(value = {"/landingpage"}, method = RequestMethod.GET)
    public String landingpage(Model model, ServletRequest request) {
        model.addAttribute("request", request);
        return "landingpage";
    }
}
