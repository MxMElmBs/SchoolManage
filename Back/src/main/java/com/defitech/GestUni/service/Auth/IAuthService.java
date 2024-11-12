package com.defitech.GestUni.service.Auth;


import com.defitech.GestUni.dto.authDto.AuthResponse;
import com.defitech.GestUni.dto.authDto.SigninRequest;
import com.defitech.GestUni.models.Bases.Utilisateur;

import java.util.Optional;

public interface IAuthService {
//    String signupUser(CreateUserRequest signupRequest);
    AuthResponse signin(SigninRequest signinRequest);
    Optional<Utilisateur> findByUtilisateur(String username);
}
