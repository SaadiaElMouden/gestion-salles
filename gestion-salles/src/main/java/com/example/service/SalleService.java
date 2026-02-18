package com.example.service;

import com.example.model.Salle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Service spécifique pour l'entité Salle.
 * Permet des recherches pratiques (disponibilité, capacité...).
 */
public class SalleService extends AbstractCrudService<Salle, Long> {

    public SalleService(EntityManagerFactory emf) {
        super(emf);
    }

    /** Retourne les salles selon leur disponibilité (true/false) */
    public List<Salle> findByDisponible(boolean disponible) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Salle> query = em.createQuery(
                    "SELECT s FROM Salle s WHERE s.disponible = :disponible",
                    Salle.class
            );
            query.setParameter("disponible", disponible);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /** Retourne les salles dont la capacité est >= capaciteMin */
    public List<Salle> findByCapaciteMinimum(int capaciteMin) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Salle> query = em.createQuery(
                    "SELECT s FROM Salle s WHERE s.capacite >= :capaciteMin",
                    Salle.class
            );
            query.setParameter("capaciteMin", capaciteMin);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
