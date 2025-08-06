package com.surfer.codes.url_shortener.web.controllers;

import com.surfer.codes.url_shortener.ApplicationProperties;
import com.surfer.codes.url_shortener.domain.services.ShortUrlService;
import com.surfer.codes.url_shortener.dto.PagedResult;
import com.surfer.codes.url_shortener.dto.ShortUrlDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final ShortUrlService shortUrlService;
    private final ApplicationProperties appConf;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(defaultValue = "1") int pageNo, Model model) {
        PagedResult<ShortUrlDto> allUrls = shortUrlService.getAllShortUrls(pageNo, appConf.pageSize());
        model.addAttribute("shortUrls", allUrls);
        model.addAttribute("baseUrl", appConf.baseUrl());
        model.addAttribute("paginationUrl", "/admin/dashboard");
        return "admin-dashboard";
    }
}
