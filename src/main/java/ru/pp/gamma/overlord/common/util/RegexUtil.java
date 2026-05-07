package ru.pp.gamma.overlord.common.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    private static final Pattern EXTENSION_URL = Pattern.compile("\\.(\\w+)(?=\\?|$)");

    public static Optional<String> resolveFileExtensionFromUrl(String url) {
        Matcher matcher = EXTENSION_URL.matcher(url);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

}
