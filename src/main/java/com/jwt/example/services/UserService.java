package com.jwt.example.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jwt.example.models.User;

@Service
public class UserService {
    private List<User> store = new ArrayList<>();

    public UserService() {
        store.add(new User(UUID.randomUUID().toString(),"Navneet","navneet@gmail.com"));
        store.add(new User(UUID.randomUUID().toString(),"Raj","raj@gmail.com"));
        store.add(new User(UUID.randomUUID().toString(),"Sneha","sneha@gmail.com"));

    }

    public List<User> getUsers(){
        return this.store;
    }


}   
