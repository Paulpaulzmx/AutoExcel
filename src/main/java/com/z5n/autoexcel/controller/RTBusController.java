package com.z5n.autoexcel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RTBusController {
    @RequestMapping(value = "/bus", method = RequestMethod.GET)
    public String BusIndex(){
        return "RTBusTemplate/index";
    }

}
