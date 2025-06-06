# Movie Ticket Booking System

## Description
Cette application Java permet aux clients de réserver des billets de cinéma et aux employés de gérer les films et les réservations. Elle utilise Java Swing pour l'interface graphique et des fichiers texte pour le stockage des données.

## Fonctionnalités

### Pour les clients
- Connexion avec nom d'utilisateur et mot de passe
- Consultation des films disponibles
- Réservation de billets
- Réduction automatique pour les étudiants
- Visualisation des réservations

### Pour les employés
- Consultation de toutes les réservations
- Gestion des films (ajout, modification, suppression)
- Gestion des clients

## Installation
1. Clonez ce dépôt
2. Ouvrez le projet dans IntelliJ IDEA ou votre IDE Java préféré
3. Exécutez la classe `Main.java`

## Fichiers de données
L'application utilise quatre fichiers texte pour stocker ses données :
- `users.txt` : contient les informations d'authentification des utilisateurs
- `customers.txt` : contient les informations des clients, y compris leur statut étudiant
- `movies.txt` : contient les informations sur les films
- `bookings.txt` : contient les informations sur les réservations

## Comptes de test

### Clients
- Nom d'utilisateur : alice, Mot de passe : pass123
- Nom d'utilisateur : bob, Mot de passe : secure
- Nom d'utilisateur : david, Mot de passe : movieLover
- Nom d'utilisateur : frank, Mot de passe : guest

### Employés
- Nom d'utilisateur : charlie, Mot de passe : cinema
- Nom d'utilisateur : eve, Mot de passe : adminPass

## Structure du projet
- `model/` : contient les classes de modèle (User, Customer, Movie, Booking)
- `service/` : contient les classes de service pour gérer les données
- `gui/` : contient les classes d'interface utilisateur
