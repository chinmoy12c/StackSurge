package com.stacksurge.StackSurge.utility;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.stacksurge.StackSurge.Models.ResponseBody;

import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    public String createToken(String email) {
        return JWT.create()
        .withSubject(email)
        .withExpiresAt(new Date(System.currentTimeMillis() + Constants.JWT_EXPIRE_TIME))
        .sign(Algorithm.HMAC512(Constants.JWT_SECRET_KEY.getBytes()));
    }

    public ResponseBody verifyToken(String jwt) {
        ResponseBody response = new ResponseBody();
        try {
            response.setResponse(JWT.require(Algorithm.HMAC512(Constants.JWT_SECRET_KEY))
            .build()
            .verify(jwt)
            .getSubject());
            response.setStatusCode(200);
            response.setSuccess(true);
        }
        catch (JWTVerificationException e) {
            response.setSuccess(false);
            response.setStatusCode(401);
            response.setError(e.getMessage());
        }
        return response;
    }
}
