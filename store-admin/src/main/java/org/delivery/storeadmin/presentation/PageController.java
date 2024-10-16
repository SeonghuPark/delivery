package org.delivery.storeadmin.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("")
public class PageController {

    @RequestMapping(path = {"/","/main"})
    public ModelAndView main(){
        System.out.println("들어옴");
        return new ModelAndView("main");
    }

    @RequestMapping("/order")
    public ModelAndView order(){
        return new ModelAndView("order/order");
    }
}
