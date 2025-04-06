package com.projectname.controller;

import com.projectname.service.HogeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HogeController {

    private final HogeService hogeService;

    @GetMapping("/")
    public String init() {

        hogeService.createUser("xxx@zzz");

        return "";
    }
}
