package com.example.service;

import com.example.model.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Service spécifique pour l'entité Utilisateur.
 * On ajoute ici des recherches "métier" en plus du CRUD générique.
 */
public class UtilisateurService extends AbstractCrudService<Utilisateur, Long> {

    public UtilisateurService(EntityManagerFactory emf) {
        super(emf);
    }

    /** Recherche un utilisateur à partir de son email */
    public Optional<Utilisateur> findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Utilisateur> query = em.createQuery(
                    "SELECT u FROM Utilisateur u WHERE u.email = :email",
                    Utilisateur.class
            );
            query.setParameter("email", email);

            List<Utilisateur> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            em.close();
        }
    }
}
