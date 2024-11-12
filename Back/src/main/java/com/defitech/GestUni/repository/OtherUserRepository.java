package com.defitech.GestUni.repository;

import com.defitech.GestUni.models.Bases.Etudiant;
import com.defitech.GestUni.models.Bases.OtherUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OtherUserRepository extends JpaRepository<OtherUser, Long> {
    @Query("SELECT e FROM OtherUser e WHERE e.utilisateur IS NULL")
    List<OtherUser> findOUserSansUtilisateur();
}
