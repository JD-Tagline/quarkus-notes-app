package com.quarkus.notes.service;

public interface IUserService {

    String authenticate(String username, String password);
}
