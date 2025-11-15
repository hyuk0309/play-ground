package com.example.service;

import com.example.mylibrary.GreetingService;

public class App {
    public static void main(String[] args) {
        GreetingService service = new GreetingService();
        String name = args.length > 0 ? args[0] : null;
        System.out.println(service.greet(name));
    }
}
