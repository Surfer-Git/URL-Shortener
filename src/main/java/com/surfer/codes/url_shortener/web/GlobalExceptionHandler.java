package com.surfer.codes.url_shortener.web;

import com.surfer.codes.url_shortener.web.exceptions.ShortUrlNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ShortUrlNotFoundException.class)
    public String handleShortUrlNotFoundException(ShortUrlNotFoundException ex) {
        log.error("Short URL not found: {}", ex.getMessage());
        return "error/404"; // Return a 404 error page
    }
}
