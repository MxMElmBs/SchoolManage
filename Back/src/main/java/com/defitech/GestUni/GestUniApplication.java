package com.defitech.GestUni;

import com.defitech.GestUni.enums.UserRole;
import com.defitech.GestUni.models.Bases.Utilisateur;
import com.defitech.GestUni.repository.UtilisateurRepository;
import com.defitech.GestUni.service.Max.AdminMailServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootApplication
public class GestUniApplication implements CommandLineRunner {

	@Autowired
	private UtilisateurRepository userRepository;
	@Autowired
	private AdminMailServices adminMailServices;

	public static void main(String[] args) {

		SpringApplication.run(GestUniApplication.class, args);


	}


	@Override
	public void run(String... args) throws Exception {

		List<Utilisateur> adminAccount = userRepository.findByRole(UserRole.ADMIN);
		if (adminAccount.isEmpty()) {
			Utilisateur user = new Utilisateur();
			user.setUsername("admin@defitech.com");
			user.setRole(UserRole.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(user);
		}

	}

}
