package com.example.jwt.service;

import com.example.jwt.entity.Token;

public interface TokenService {
    Token createToken(Token token);

    Token findByToken(String token);
}
