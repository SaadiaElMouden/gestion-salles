package com.example.service;

import java.util.List;
import java.util.Optional;

/**
 * Interface générique CRUD :
 * - Centralise les opérations de base (Créer/Lire/Modifier/Supprimer)
 * - Réutilisable pour toutes les entités (Salle, Utilisateur, etc.)
 */
public interface CrudService<T, ID> {

    /** Enregistre une entité dans la base */
    T save(T entity);

    /** Recherche une entité par son identifiant */
    Optional<T> findById(ID id);

    /** Récupère toutes les entités de ce type */
    List<T> findAll();

    /** Met à jour une entité existante */
    void update(T entity);

    /** Supprime une entité */
    void delete(T entity);

    /** Supprime par identifiant (si trouvé) */
    void deleteById(ID id);
}
