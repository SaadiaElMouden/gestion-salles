package com.example;

import com.example.model.Salle;
import com.example.model.Utilisateur;
import com.example.service.SalleService;
import com.example.service.UtilisateurService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class App {

    public static void main(String[] args) {

        // Démarrage JPA : création de l'EntityManagerFactory à partir de persistence.xml
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion-salles");

        // Services (CRUD générique + requêtes spécifiques)
        UtilisateurService utilisateurService = new UtilisateurService(emf);
        SalleService salleService = new SalleService(emf);

        try {
            System.out.println("\n========== DEMO CRUD : UTILISATEUR ==========");
            testCrudUtilisateur(utilisateurService);

            System.out.println("\n========== DEMO CRUD : SALLE ==========");
            testCrudSalle(salleService);

        } finally {
            // Fermeture propre des ressources
            emf.close();
        }
    }

    private static void testCrudUtilisateur(UtilisateurService service) {

        // --- CREATE ---
        System.out.println(">> Ajout de quelques utilisateurs...");

        Utilisateur u1 = new Utilisateur("El Amrani", "Yassine", "yassine.elamrani@demo.com");
        u1.setDateNaissance(LocalDate.of(1997, 3, 12));
        u1.setTelephone("+212600112233");

        Utilisateur u2 = new Utilisateur("Benslimane", "Khadija", "khadija.benslimane@demo.com");
        u2.setDateNaissance(LocalDate.of(2001, 7, 5));
        u2.setTelephone("+212611445566");

        service.save(u1);
        service.save(u2);

        // --- READ (ALL) ---
        System.out.println("\n>> Liste actuelle des utilisateurs :");
        List<Utilisateur> utilisateurs = service.findAll();
        utilisateurs.forEach(System.out::println);

        // --- READ (BY ID) ---
        System.out.println("\n>> Recherche d'un utilisateur par ID (ID=1) :");
        Optional<Utilisateur> userOpt = service.findById(1L);
        userOpt.ifPresent(System.out::println);

        // --- READ (BY EMAIL) ---
        System.out.println("\n>> Recherche d'un utilisateur par email :");
        Optional<Utilisateur> userByEmail = service.findByEmail("khadija.benslimane@demo.com");
        userByEmail.ifPresent(System.out::println);

        // --- UPDATE ---
        System.out.println("\n>> Mise à jour (téléphone) de l'utilisateur ID=1 :");
        userOpt.ifPresent(user -> {
            user.setTelephone("+212699887766");
            service.update(user);
            System.out.println("Utilisateur mis à jour : " + user);
        });

        // --- DELETE ---
        System.out.println("\n>> Suppression de l'utilisateur ID=2 :");
        service.deleteById(2L);
        System.out.println("Suppression terminée.");

        System.out.println("\n>> Utilisateurs restants après suppression :");
        service.findAll().forEach(System.out::println);
    }

    private static void testCrudSalle(SalleService service) {

        // --- CREATE ---
        System.out.println(">> Création de quelques salles...");

        Salle s1 = new Salle("Salle Réseau R12", 28);
        s1.setDescription("Salle pratique (réseaux) avec switches et routeurs.");
        s1.setEtage(0);

        Salle s2 = new Salle("Amphi Central AC1", 220);
        s2.setDescription("Grand amphithéâtre pour cours magistraux.");
        s2.setEtage(1);

        Salle s3 = new Salle("Salle Projet P05", 14);
        s3.setDescription("Petite salle pour travaux en groupe.");
        s3.setEtage(2);
        s3.setDisponible(false);

        service.save(s1);
        service.save(s2);
        service.save(s3);

        // --- READ (ALL) ---
        System.out.println("\n>> Toutes les salles enregistrées :");
        List<Salle> salles = service.findAll();
        salles.forEach(System.out::println);

        // --- READ (BY ID) ---
        System.out.println("\n>> Recherche d'une salle par ID (ID=2) :");
        Optional<Salle> salleOpt = service.findById(2L);
        salleOpt.ifPresent(System.out::println);

        // --- READ (DISPONIBLE) ---
        System.out.println("\n>> Salles disponibles :");
        List<Salle> dispo = service.findByDisponible(true);
        dispo.forEach(System.out::println);

        // --- READ (CAPACITE MIN) ---
        System.out.println("\n>> Salles avec capacité minimale = 30 :");
        List<Salle> grandes = service.findByCapaciteMinimum(30);
        grandes.forEach(System.out::println);

        // --- UPDATE ---
        System.out.println("\n>> Mise à jour de la capacité de la salle ID=2 :");
        salleOpt.ifPresent(salle -> {
            salle.setCapacite(250);
            service.update(salle);
            System.out.println("Salle mise à jour : " + salle);
        });

        // --- DELETE ---
        System.out.println("\n>> Suppression de la salle ID=3 :");
        service.deleteById(3L);
        System.out.println("Suppression terminée.");

        System.out.println("\n>> Salles restantes après suppression :");
        service.findAll().forEach(System.out::println);
    }
}
