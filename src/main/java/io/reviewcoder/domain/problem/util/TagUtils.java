package io.reviewcoder.domain.problem.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TagUtils {

    public static String normalize(String tag) {
        if (tag == null) return null;
        String t = tag.trim();
        return t.isEmpty() ? null : t;
    }
}