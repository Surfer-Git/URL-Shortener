package com.surfer.codes.url_shortener;

import com.surfer.codes.url_shortener.config.CustomPasswordEncoder;
import org.junit.jupiter.api.Test;

public class PasswordEncoderTest {

    @Test
    public void testEncodeWithSalt() {
         CustomPasswordEncoder encoder = new CustomPasswordEncoder();
         String encoded = encoder.encodeWithSalt("user", "randomSalt456");
         System.out.println("Encoded password: " + encoded);
    }
}
