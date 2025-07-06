package com.surfer.codes.url_shortener.web.controllers;

import com.surfer.codes.url_shortener.ApplicationProperties;
import com.surfer.codes.url_shortener.domain.services.ShortUrlService;
import com.surfer.codes.url_shortener.dto.CreateShortUrlCmd;
import com.surfer.codes.url_shortener.dto.CreateShortUrlForm;
import com.surfer.codes.url_shortener.dto.ShortUrlDto;
import com.surfer.codes.url_shortener.web.exceptions.ShortUrlNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final ShortUrlService shortUrlService;
    private final ApplicationProperties appConf;

    public HomeController(ShortUrlService shortUrlService, ApplicationProperties appConf) {
        this.shortUrlService = shortUrlService;
        this.appConf = appConf;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<ShortUrlDto> shortUrls = shortUrlService.getAllPublicShortUrls();
        model.addAttribute("shortUrls", shortUrls);
        model.addAttribute("baseUrl", appConf.baseUrl());
        model.addAttribute("createShortUrlForm", new CreateShortUrlForm(""));
        return "index";
    }

    @PostMapping("/short-urls")
    public String createShortUrl(@ModelAttribute("createShortUrlForm") @Valid CreateShortUrlForm form,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        // Check for validation errors
        if(bindingResult.hasErrors()) {
            List<ShortUrlDto> shortUrls = shortUrlService.getAllPublicShortUrls();
            model.addAttribute("shortUrls", shortUrls);
            model.addAttribute("baseUrl", appConf.baseUrl());
            model.addAttribute("createShortUrlForm", form);
            return "index";
            // the validation-errors will be passed to the view automatically.
        }

        try{
            CreateShortUrlCmd cmd = new CreateShortUrlCmd(form.originalUrl());
            ShortUrlDto shortUrlDto = shortUrlService.createShortUrl(cmd);
            redirectAttributes.addFlashAttribute("successMessage", "Short URL created successfully " +
                    appConf.baseUrl() + "/s/" + shortUrlDto.shortKey());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create short URL");
        }

        return "redirect:/";
    }

    @GetMapping("/s/{shortKey}")
    public String redirectToOriginalUrl(@PathVariable String shortKey, HttpServletResponse response) {
        Optional<ShortUrlDto> shortUrlOpt = shortUrlService.accessShortUrl(shortKey);
        if(shortUrlOpt.isEmpty()){
            throw new ShortUrlNotFoundException("short-key: " + shortKey);
        }
        response.setHeader("Cache-Control", "no-store"); // Prevent caching
        return "redirect:" + shortUrlOpt.get().originalUrl();
    }

}
