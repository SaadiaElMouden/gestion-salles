package com.example.service;

import com.example.model.Salle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class SalleServiceTest {

    private EntityManagerFactory emf;
    private SalleService service;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("gestion-salles");
        service = new SalleService(emf);

        // Nettoyage avant chaque test
        List<Salle> all = service.findAll();
        for (Salle s : all) {
            service.delete(s);
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
        Salle s = new Salle("Salle Informatique", 35);
        s.setDescription("Salle équipée de PC");
        s.setEtage(1);
        s.setDisponible(true);

        Salle saved = service.save(s);
        assertNotNull(saved.getId());

        // READ
        Optional<Salle> found = service.findById(saved.getId());
        assertTrue(found.isPresent());

        // UPDATE
        Salle toUpdate = found.get();
        toUpdate.setCapacite(45);
        service.update(toUpdate);

        Optional<Salle> updated = service.findById(saved.getId());
        assertTrue(updated.isPresent());
        assertEquals(Integer.valueOf(45), updated.get().getCapacite());

        // DELETE
        service.deleteById(saved.getId());
        Optional<Salle> afterDelete = service.findById(saved.getId());
        assertFalse(afterDelete.isPresent());
    }

    @Test
    public void testFindByDisponible() {
        Salle s1 = new Salle("Salle Réunion", 20);
        s1.setDisponible(true);
        service.save(s1);

        Salle s2 = new Salle("Salle Fermée", 15);
        s2.setDisponible(false);
        service.save(s2);

        List<Salle> dispo = service.findByDisponible(true);
        assertTrue(dispo.stream().anyMatch(s -> "Salle Réunion".equals(s.getNom())));

        List<Salle> nonDispo = service.findByDisponible(false);
        assertTrue(nonDispo.stream().anyMatch(s -> "Salle Fermée".equals(s.getNom())));
    }

    @Test
    public void testFindByCapaciteMinimum() {
        service.save(new Salle("Petite Salle", 10));
        service.save(new Salle("Salle Moyenne", 60));
        service.save(new Salle("Grand Amphi", 200));

        List<Salle> min50 = service.findByCapaciteMinimum(50);
        assertEquals(2, min50.size());

        List<Salle> min150 = service.findByCapaciteMinimum(150);
        assertEquals(1, min150.size());
        assertEquals("Grand Amphi", min150.get(0).getNom());
    }
}
