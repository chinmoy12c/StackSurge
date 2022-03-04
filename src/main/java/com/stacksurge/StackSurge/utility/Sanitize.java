package com.stacksurge.StackSurge.utility;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class Sanitize {
    public String makeSafe(String inp) {
        inp = Jsoup.clean(inp, Safelist.basic());
        // TODO: add sql injection protection
        return inp;
    }
}
