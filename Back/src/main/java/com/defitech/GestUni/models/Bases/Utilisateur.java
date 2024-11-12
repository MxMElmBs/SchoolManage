package com.defitech.GestUni.models.Bases;

import com.defitech.GestUni.enums.UserRole;
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

  @OneToOne
  @JoinColumn(name = "directeurEtude_Id")
  private DirecteurEtude directeurEtude;

  @OneToOne
  @JoinColumn(name = "professeur_Id")
  private Professeur professeur;

  @OneToOne
  @JoinColumn(name = "etudiant_Id")
  private Etudiant etudiant;

  @OneToOne
  @JoinColumn(name = "otheruser_Id")
  private OtherUser otherUser;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of( new SimpleGrantedAuthority(role.name()));
  }

}
