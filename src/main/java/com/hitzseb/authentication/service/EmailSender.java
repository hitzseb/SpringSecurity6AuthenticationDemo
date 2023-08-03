package com.hitzseb.authentication.service;

public interface EmailSender {
    void send(String to, String email);
}
