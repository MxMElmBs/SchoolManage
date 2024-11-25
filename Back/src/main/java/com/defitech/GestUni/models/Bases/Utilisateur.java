package com.defitech.GestUni.models.Bases;

import com.defitech.GestUni.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
public class Utilisateur implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long idUser;
  private String email;
  private String password;
  private String username;
  private String actif;
  private String send;
  private String print;

//  private String username;
  @Enumerated(EnumType.STRING)
  private UserRole role;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "directeurEtude_Id")
  @JsonIgnore
  private DirecteurEtude directeurEtude;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "professeur_Id")
  @JsonIgnore
  private Professeur professeur;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "etudiant_Id")
  @JsonIgnore
  private Etudiant etudiant;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "otheruser_Id")
  @JsonIgnore
  private OtherUser otherUser;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of( new SimpleGrantedAuthority(role.name()));
  }

}
