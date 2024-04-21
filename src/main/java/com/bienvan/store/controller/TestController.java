package com.bienvan.store.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.bienvan.store.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bienvan.store.model.Brand;
import com.bienvan.store.model.Color;
import com.bienvan.store.service.ColorService;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    IBrandService brandServiceImpl;

    @Autowired
    ColorService colorService;

    @GetMapping("")
    public String showAdmin(HttpSession session) {
        List<Brand> brands = brandServiceImpl.findAll();
        List<Color> colors = colorService.getColors();

        session.setAttribute("brands", brands);
        return "test";
    }

    @GetMapping("/user")
    public String showUserPage() {
        return "index";
    }
}
