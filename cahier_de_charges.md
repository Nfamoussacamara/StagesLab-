UNIVERSITÉ DE LABÉ
DÉPARTEMENT INFORMATIQUE — LICENCE 3
CAHIER DES CHARGES
LabéStages
Application Web de gestion des projets et stages de fin de cycle 2
Architecture Angular · Spring Boot · REST · JSON
Version 1.0 — Document technique destiné au développement (spécification pour Antigravity / IA de développement)
Juillet 2026
LabéStages — Cahier des charges Université de Labé
Sommaire
Page 3 / 19
LabéStages — Cahier des charges Université de Labé
1. Présentation du projet
Nom du projet : LabéStages
Porteur : Université de Labé — Département Informatique
Type de projet : Application Web SPA (Single Page Application)
Public cible : Département informatique — gestion des étudiants de Licence 3, encadreurs et thèmes de fin
de cycle
L'objectif du projet est de remplacer le suivi actuellement dispersé entre plusieurs fichiers par une application
Web centralisée, simple et fiable, permettant au département de gérer efficacement les étudiants, les
encadreurs et les thèmes de projets de fin de cycle.
2. Analyse du besoin
2.1 Constat actuel
● Informations éclatées entre plusieurs fichiers (étudiants, encadreurs, thèmes).
● Absence de vue d'ensemble sur l'état d'avancement des projets.
● Suivi manuel difficile à maintenir, à mettre à jour et à exploiter.
2.2 Besoin exprimé
Une application permettant au département de :
● Enregistrer et suivre les étudiants de Licence 3.
● Gérer les encadreurs disponibles et leurs spécialités.
● Suivre les thèmes de projets, leur affectation et l'évolution de leur statut.
● Visualiser l'état global de l'activité via un tableau de bord synthétique.
3. Acteurs et cas d'utilisation
3.1 Acteurs
Acteur Rôle
Gestionnaire (secrétariat /
département)
Gère les étudiants, les encadreurs et les thèmes ; consulte le tableau de
bord.
Encadreur (donnée gérée) Associé à des thèmes selon sa spécialité et sa disponibilité.
Étudiant (donnée gérée) Associé à un thème et à un encadreur.
Page 4 / 19
LabéStages — Cahier des charges Université de Labé
3.2 Cas d'utilisation principaux
● Ajouter, consulter, modifier, supprimer un étudiant.
● Ajouter, consulter, modifier, supprimer un encadreur.
● Soumettre un thème, l'affecter à un étudiant et à un encadreur.
● Changer le statut d'un thème.
● Rechercher un étudiant par matricule ou par nom.
● Consulter le tableau de bord synthétique.
Page 5 / 19
LabéStages — Cahier des charges Université de Labé
4. Modèle de données (entités et relations)
4.1 Entité Étudiant
Champ Type Règle
id Long Clé primaire, auto-générée
matricule String Unique, obligatoire
nom String Obligatoire
prenom String Obligatoire
email String Format valide, obligatoire
niveau String Valeur par défaut : L3
4.2 Entité Encadreur
Champ Type Règle
id Long Clé primaire, auto-générée
nom String Obligatoire
email String Format valide, obligatoire
specialite String Obligatoire
disponibilite Integer Nombre ≥ 0 (projets pouvant être suivis)
4.3 Entité Thème / Projet
Champ Type Règle
id Long Clé primaire, auto-générée
titre String Unique ou contrôlé, obligatoire
description String Obligatoire
domaine String Obligatoire
statut Enum EN_ATTENTE, VALIDE, REJETE, SOUTENU
etudiant_id FK → Étudiant Non nul
encadreur_id FK → Encadreur Non nul
Page 6 / 19
LabéStages — Cahier des charges Université de Labé
4.4 Relations entre entités
Le cahier des charges impose uniquement qu'un thème soit lié à un étudiant et à un encadreur (section 2.3 et
3.4). Les cardinalités ci-dessous sont une proposition d'implémentation cohérente, à ajuster si besoin :
● Un Étudiant est associé à un seul Thème (relation 1–1 ou 1–0..1) — proposition.
● Un Encadreur peut encadrer plusieurs Thèmes (relation 1–N) — proposition.
● Un Thème appartient obligatoirement à un Étudiant et à un Encadreur — exigé (3.4).
Page 7 / 19
LabéStages — Cahier des charges Université de Labé
5. Architecture technique
5.1 Schéma client / serveur
┌───────────────────────┐ HTTP / JSON ┌────────────────────────┐
JPA / SQL ┌───────────────┐
│ Angular (SPA) │ ───────────────────▶ │ Spring Boot (REST) │
─────────────────▶ │ Base de │
│ Composants / Services │ ◀─────────────────── │ Contrôleurs / Services │
◀───────────────── │ données │
└───────────────────────┘ └────────────────────────┘
└───────────────┘
5.2 Rôle de chaque couche
Couche Technologie Responsabilité
Front-end Angular (TypeScript) Interface utilisateur, routage, formulaires,
appels HTTP
Back-end Spring Boot (Java) API REST, logique métier, validation des
données
Persistance Base de données relationnelle (H2 /
MySQL / PostgreSQL) Stockage durable des données
Échange JSON Format unique de communication entre
les couches
Tests API Postman Validation fonctionnelle des endpoints
Page 8 / 19
LabéStages — Cahier des charges Université de Labé
6. Spécifications fonctionnelles
6.1 Tableau de bord
● Nombre total d'étudiants.
● Nombre total d'encadreurs.
● Nombre de thèmes soumis, validés et en attente.
6.2 Module Étudiants (CRUD complet)
● Ajouter, consulter, modifier, supprimer un étudiant.
● Recherche par matricule ou par nom.
● Validation : matricule unique, e-mail au format valide.
6.3 Module Encadreurs (CRUD complet)
● Ajouter, consulter, modifier, supprimer un encadreur.
● Gestion de la spécialité et de la disponibilité.
6.4 Module Thèmes / Projets (CRUD complet)
● Soumission d'un thème et affectation à un étudiant et à un encadreur.
● Changement de statut du thème.
● Liens obligatoires vers l'étudiant et l'encadreur concernés.
Page 9 / 19
LabéStages — Cahier des charges Université de Labé
7. Spécifications techniques — API REST
Le cahier des charges exige des « endpoints cohérents » avec les méthodes HTTP adaptées (2.4), sans imposer
de nommage précis. Le tableau ci-dessous est une proposition de nommage cohérente pour la ressource
Étudiants, à dupliquer pour Encadreurs et Thèmes.
7.1 Endpoints proposés (ressource Étudiants — à dupliquer pour Encadreurs et Thèmes)
Méthode Endpoint Action
GET /api/etudiants Liste des étudiants
GET /api/etudiants/{id} Détail d'un étudiant
POST /api/etudiants Création d'un étudiant
PUT /api/etudiants/{id} Modification d'un étudiant
DELETE /api/etudiants/{id} Suppression d'un étudiant
Les mêmes conventions s'appliquent aux ressources /api/encadreurs et /api/themes.
7.2 Codes HTTP attendus
Code Signification
200 Succès (lecture, modification)
201 Ressource créée avec succès
204 Suppression réussie, pas de contenu à retourner
400 Données invalides envoyées par le client
404 Ressource introuvable
500 Erreur interne du serveur
Page 10 / 19
LabéStages — Cahier des charges Université de Labé
8. Règles de validation fonctionnelle
Ressource Champs minimaux Règles attendues
Étudiant matricule, nom, prenom, email,
niveau
Matricule unique ; e-mail valide ; nom et
prénom obligatoires ; niveau = L3 par défaut
Encadreur nom, email, specialite E-mail valide ; spécialité obligatoire ;
disponibilité numérique ≥ 0
Thème/Projet titre, description, domaine, statut,
etudiant, encadreur
Titre unique ou contrôlé ; statut parmi
EN_ATTENTE, VALIDE, REJETE, SOUTENU ;
liens non nuls
Page 11 / 19
LabéStages — Cahier des charges Université de Labé
9. Exigences non fonctionnelles
● Simplicité : privilégier une application fonctionnelle et stable plutôt qu'ambitieuse et instable.
● Séparation des responsabilités : organisation par package côté Spring Boot, par fonctionnalité côté
Angular.
● Navigation SPA : aucune page ne doit être intégralement rechargée.
● Gestion des erreurs : messages utilisateurs clairs côté Front-end (données invalides, ressource
introuvable, erreur serveur).
● Documentation : README complet couvrant installation, lancement, endpoints et captures d'écran.
10. Livrables attendus
Code Livrable Contenu attendu
L1 Code source Dépôt Git ou archive ZIP contenant backend, frontend et
documentation
L2 Application fonctionnelle Démonstration locale de la SPA Angular connectée à l'API
Spring Boot
L3 Documentation technique README : installation, lancement, structure du projet,
endpoints, captures d'écran
L4 Tests API Collection Postman ou tableau de tests (méthode, URL,
corps JSON, résultat attendu/obtenu)
L5 Note de conception 2 à 4 pages : architecture, modèle de données, choix
techniques, difficultés rencontrées
L6 Présentation orale Démonstration courte du parcours utilisateur et des
opérations CRUD
Page 12 / 19
LabéStages — Cahier des charges Université de Labé
11. Planning indicatif de réalisation
Phase Durée Contenu
Analyse & conception 4 h Entités, cas d'usage, architecture, définition des endpoints
Back-end & API 4 h Entités, repositories, services, contrôleurs, tests Postman
Front-end & intégration 4 h Composants Angular, services, connexion à l'API
Finalisation Travail
personnel
Nettoyage du code, rédaction du README, préparation de la
soutenance
Page 13 / 19
LabéStages — Cahier des charges Université de Labé
12. Dépendances techniques du projet
Cette section liste les dépendances nécessaires pour construire l'application dans les règles de l'art, en
cohérence avec les objectifs pédagogiques du module (architecture REST, validation, documentation d'API,
tests). Chaque choix reste simple, conforme à l'esprit « application simple mais fonctionnelle » du cahier des
charges.
12.1 Back-end — Spring Boot (Maven, pom.xml)
Dépendance Groupe Maven Rôle dans l'application
Spring Web spring-boot-starter-web Construction de l'API REST (contrôleurs,
sérialisation JSON)
Spring Data JPA spring-boot-starter-data-jpa Entités, repositories, accès à la base de
données
Validation spring-boot-startervalidation
Validation des champs (matricule
unique, e-mail, statut)
Base de données (H2 en dev,
MySQL/PostgreSQL en prod)
com.h2database:h2 / mysqlconnector-j /
org.postgresql:postgresql
Persistance des données
Springdoc OpenAPI (Swagger) springdoc-openapi-starterwebmvc-ui
Documentation interactive de l'API,
complète la collection Postman
Lombok org.projectlombok:lombok Réduit le code répétitif des entités et
DTOs
Spring Boot Test spring-boot-starter-test Tests unitaires et d'intégration des
contrôleurs/services
Extrait pom.xml :
<dependencies>
 <dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-web</artifactId>
 </dependency>
 <dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-data-jpa</artifactId>
 </dependency>
 <dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-validation</artifactId>
 </dependency>
 <dependency>
 <groupId>com.h2database</groupId>
 <artifactId>h2</artifactId>
Page 14 / 19
LabéStages — Cahier des charges Université de Labé
 <scope>runtime</scope>
 </dependency>
 <dependency>
 <groupId>org.springdoc</groupId>
 <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
 </dependency>
 <dependency>
 <groupId>org.projectlombok</groupId>
 <artifactId>lombok</artifactId>
 <optional>true</optional>
 </dependency>
 <dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-test</artifactId>
 <scope>test</scope>
 </dependency>
</dependencies>
12.2 Front-end — Angular (npm, package.json)
Dépendance Paquet npm Rôle dans l'application
Angular Core / CLI @angular/core,
@angular/cli
Cœur du framework et outillage de
génération
Angular Router @angular/router Routage de la SPA (dashboard, listes,
formulaires)
Angular Forms @angular/forms Formulaires réactifs (ajout/modification
étudiant, encadreur, thème)
Angular Common HTTP @angular/common/http
(HttpClient)
Consommation de l'API REST Spring
Boot
RxJS rxjs Gestion des flux asynchrones
(Observables) liés à HttpClient
Bootstrap bootstrap Mise en forme rapide du dashboard et
des formulaires
ngx-toastr ngx-toastr Affichage des messages utilisateurs
(succès, erreurs) exigés en 2.3
TypeScript typescript Typage statique des modèles (Étudiant,
Encadreur, Thème)
Karma / Jasmine karma, jasmine-core Tests unitaires des composants et
services
Extrait package.json :
Page 15 / 19
LabéStages — Cahier des charges Université de Labé
"dependencies": {
 "@angular/common": "^18.0.0",
 "@angular/compiler": "^18.0.0",
 "@angular/core": "^18.0.0",
 "@angular/forms": "^18.0.0",
 "@angular/platform-browser": "^18.0.0",
 "@angular/platform-browser-dynamic": "^18.0.0",
 "@angular/router": "^18.0.0",
 "bootstrap": "^5.3.0",
 "ngx-toastr": "^19.0.0",
 "rxjs": "~7.8.0",
 "tslib": "^2.6.0",
 "zone.js": "~0.14.0"
},
"devDependencies": {
 "@angular-devkit/build-angular": "^18.0.0",
 "@angular/cli": "^18.0.0",
 "@angular/compiler-cli": "^18.0.0",
 "typescript": "~5.4.0",
 "karma": "^6.4.0",
 "jasmine-core": "^5.1.0"
}
12.3 Environnement de développement
Outil Usage
Java 17 ou 21 (JDK) Exécution du Back-end Spring Boot
Node.js 18 LTS ou 20 LTS Exécution des outils Angular CLI et npm
Maven 3.9+ Gestion des dépendances et build du Back-end
Postman Test et documentation des endpoints REST — exigé en 2.3
Git Versionnement — exigé en séance 1 (« préparer le dépôt Git »)
Page 16 / 19
LabéStages — Cahier des charges Université de Labé
13. Éléments techniques indispensables au bon fonctionnement
Au-delà des dépendances, trois mécanismes sont indispensables pour que l'application fonctionne réellement
comme décrit dans le cahier des charges (sécurité élémentaire, gestion des erreurs, communication
Front/Back). Sans eux, l'application ne peut pas satisfaire les exigences fonctionnelles même avec les bonnes
dépendances installées.
13.1 Configuration CORS (obligatoire techniquement)
Angular (port 4200 en développement) et Spring Boot (port 8080) tournent sur des origines différentes. Sans
autoriser explicitement cette communication côté serveur, le navigateur bloque tous les appels de l'API — le
CRUD ne peut tout simplement pas fonctionner. Cette configuration est requise, quelle que soit
l'implémentation choisie.
@Configuration
public class CorsConfig implements WebMvcConfigurer {
 @Override
 public void addCorsMappings(CorsRegistry registry) {
 registry.addMapping("/api/**")
 .allowedOrigins("http://localhost:4200")
 .allowedMethods("GET", "POST", "PUT", "DELETE")
 .allowedHeaders("*");
 }
}
13.2 Gestion centralisée des erreurs (@RestControllerAdvice)
Le cahier des charges exige que les erreurs fréquentes soient traitées (données invalides, ressource introuvable,
erreur serveur — section 3.3) avec des codes HTTP adaptés et des réponses JSON (2.4). Un gestionnaire
d'exceptions global permet de centraliser cette logique plutôt que de la répéter dans chaque contrôleur, et
garantit une réponse JSON cohérente sur toute l'API.
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(ResourceNotFoundException.class)
 public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException
ex) {
 return ResponseEntity.status(404).body(new
ErrorResponse(ex.getMessage()));
 }
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<ErrorResponse>
handleValidation(MethodArgumentNotValidException ex) {
Page 17 / 19
LabéStages — Cahier des charges Université de Labé
 return ResponseEntity.status(400).body(new ErrorResponse("Données
invalides"));
 }
 @ExceptionHandler(Exception.class)
 public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
 return ResponseEntity.status(500).body(new ErrorResponse("Erreur
serveur"));
 }
}
13.3 Utilisation de DTOs plutôt que d'exposer les entités JPA
Les entités Étudiant, Encadreur et Thème sont liées entre elles (relations JPA). Renvoyer directement les entités
en JSON provoque des boucles infinies de sérialisation (Étudiant → Thème → Étudiant → …) et expose des
champs internes inutiles. Utiliser des DTOs (Data Transfer Objects) dédiés aux échanges API répond à la fois à
l'exigence de « structuration du code » et de « sécurité élémentaire » formulée en 1.2, et évite ce blocage
technique concret.
● EtudiantDTO, EncadreurDTO, ThemeDTO : structures dédiées aux réponses JSON de l'API.
● Les entités JPA restent internes à la couche persistance (repository/service).
● Un mapping simple (manuel ou via MapStruct) convertit entité ↔ DTO dans la couche service.
Page 18 / 19
LabéStages — Cahier des charges Université de Labé
Fin du cahier des charges — LabéStages
Page 19 / 19