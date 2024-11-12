package com.defitech.GestUni.service.user;


import com.defitech.GestUni.models.Bases.Utilisateur;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService {
    UserDetailsService userDetailsService();

    Optional<Utilisateur> findById(Long id);
}
