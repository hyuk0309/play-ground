package com.example.internallibrary;

import java.util.Objects;

/**
 * 내부 유틸리티 라이브러리 예시 클래스.
 */
public final class StringUtils {

    private StringUtils() {}

    /**
     * 주어진 문자열의 첫 글자를 대문자로 변환합니다. null 또는 빈 문자열이면 그대로 반환합니다.
     */
    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        if (input.length() == 1) return input.toUpperCase();
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }

    /**
     * 공백을 안전하게 트림합니다. null이면 빈 문자열을 반환합니다.
     */
    public static String safeTrim(String input) {
        return Objects.toString(input, "").trim();
    }
}
