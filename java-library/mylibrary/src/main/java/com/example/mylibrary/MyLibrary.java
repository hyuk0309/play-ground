package com.example.mylibrary;

import com.example.internallibrary.InternalLibrary;

/**
 * 외부에 제공되는 라이브러리의 간단한 서비스 클래스.
 */
public class MyLibrary {

    /**
     * 이름을 받아 간단한 인사말을 생성합니다.
     */
    public String greet(String name) {
        String cleaned = InternalLibrary.safeTrim(name);
        String capitalized = InternalLibrary.capitalize(cleaned);
        if (capitalized == null || capitalized.isEmpty()) {
            return "Hello, World!";
        }
        return "Hello, " + capitalized + "!";
    }

	public InternalLibrary.Name greetV2(String name) {
		return new InternalLibrary.Name(greet(name));
	}
}
