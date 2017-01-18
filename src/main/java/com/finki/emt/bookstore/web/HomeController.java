package com.finki.emt.bookstore.web;

import com.finki.emt.bookstore.config.BookStoreProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping(value = "/home")
public class HomeController {

    @GetMapping
    public String home() {
        return "TEst";
    }
}
