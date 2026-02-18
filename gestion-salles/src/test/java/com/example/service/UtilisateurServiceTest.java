package com.example.service;

import com.example.model.Utilisateur;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class UtilisateurServiceTest {

    private EntityManagerFactory emf;
    private UtilisateurService service;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("gestion-salles");
        service = new UtilisateurService(emf);

        // Nettoyage avant chaque test (évite les conflits d'email unique)
        List<Utilisateur> all = service.findAll();
        for (Utilisateur u : all) {
            service.delete(u);
        }
    }

    @After
    public void tearDown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    public void testCrudOperations() {
        // CREATE
        Utilisateur u = new Utilisateur("Salma", "Bennani", "salma.bennani@example.com");
        u.setDateNaissance(LocalDate.of(1998, 9, 10));
        u.setTelephone("+212655443322");

        Utilisateur saved = service.save(u);
        assertNotNull(saved.getId());

        // READ
        Optional<Utilisateur> found = service.findById(saved.getId());
        assertTrue(found.isPresent());

        // UPDATE
        Utilisateur toUpdate = found.get();
        toUpdate.setTelephone("+212677889900");
        service.update(toUpdate);

        Optional<Utilisateur> updated = service.findById(saved.getId());
        assertTrue(updated.isPresent());
        assertEquals("+212677889900", updated.get().getTelephone());

        // DELETE
        service.deleteById(saved.getId());
        Optional<Utilisateur> afterDelete = service.findById(saved.getId());
        assertFalse(afterDelete.isPresent());
    }

    @Test
    public void testFindByEmail() {
        Utilisateur u1 = new Utilisateur("Omar", "El Fassi", "omar.fassi@example.com");
        u1.setDateNaissance(LocalDate.of(1996, 2, 5));
        u1.setTelephone("+212611223344");
        service.save(u1);

        Optional<Utilisateur> found = service.findByEmail("omar.fassi@example.com");
        assertTrue(found.isPresent());
        assertEquals("Omar", found.get().getNom());

        Optional<Utilisateur> notFound = service.findByEmail("no.user@example.com");
        assertFalse(notFound.isPresent());
    }
}
