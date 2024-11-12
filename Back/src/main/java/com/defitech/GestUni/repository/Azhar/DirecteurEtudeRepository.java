package com.defitech.GestUni.repository.Azhar;

import com.defitech.GestUni.models.Bases.DirecteurEtude;
import com.defitech.GestUni.models.Bases.Parcours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirecteurEtudeRepository extends JpaRepository<DirecteurEtude, Long> {
    DirecteurEtude findByParcours(Parcours parcours);
}
