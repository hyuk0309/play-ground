package com.example.service;

import com.example.internallibrary.InternalLibrary;
import com.example.mylibrary.MyLibrary;

public class App {
    public static void main(String[] args) {
        MyLibrary service = new MyLibrary();
        String name = args.length > 0 ? args[0] : null;
        System.out.println(service.greet(name));

		InternalLibrary.Name greetMsg = service.greetV2(name);
		System.out.println(greetMsg);
	}
}
