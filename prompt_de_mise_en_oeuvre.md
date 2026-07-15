PROMPT — Construction complète de l'application LabéStages

Copie tout ce document dans Antigravity comme premier message de la session de développement.


RÔLE

Tu es un développeur full-stack senior. Tu vas construire, de bout en bout et de façon autonome, l'application web LabéStages décrite ci-dessous, en respectant strictement l'architecture, le modèle de données et les contraintes techniques fournis.

Tu travailles par phases, dans l'ordre indiqué en fin de document. Après chaque phase, donne un résumé court (5 lignes max) de ce qui a été fait et des fichiers créés/modifiés, puis enchaîne automatiquement sur la phase suivante sans t'arrêter, sauf si tu rencontres un vrai blocage technique (erreur de compilation non résolue, ambiguïté bloquante). Ne redemande pas de confirmation à chaque étape.


1. CONTEXTE DU PROJET

Application de gestion des étudiants, encadreurs et thèmes de projets/stages de fin de cycle pour le Département Informatique de l'Université de Labé. Remplace un suivi actuellement dispersé dans plusieurs fichiers.

Nom du projet : LabéStages
Type : Application Web SPA (Single Page Application)
Objectif : Une application simple mais fonctionnelle, stable et testée — pas d'ambition inutile, priorité à la maîtrise de l'architecture, du CRUD, de REST, de JSON et de l'intégration Front/Back.


2. STACK TECHNIQUE IMPOSÉE

CoucheTechnologieFront-endAngular (dernière version stable), TypeScript, formulaires réactifsBack-endSpring Boot (Java 17 ou 21), architecture en couches (Controller → Service → Repository)Base de donnéesH2 en développement (fichier ou mémoire), migration facile vers MySQL/PostgreSQLÉchange de donnéesJSON exclusivementTests APIEndpoints testables avec PostmanDocumentation APISpringdoc OpenAPI (Swagger UI)

Dépendances Back-end (pom.xml)


spring-boot-starter-web
spring-boot-starter-data-jpa
spring-boot-starter-validation
com.h2database:h2
springdoc-openapi-starter-webmvc-ui
org.projectlombok:lombok
spring-boot-starter-test


Dépendances Front-end (package.json)


@angular/core, @angular/router, @angular/forms, @angular/common/http
rxjs
bootstrap (mise en forme rapide)
ngx-toastr (messages utilisateurs succès/erreur)
typescript



3. MODÈLE DE DONNÉES

Entité Étudiant

ChampTypeRègleidLongClé primaire auto-généréematriculeStringUnique, obligatoirenomStringObligatoireprenomStringObligatoireemailStringFormat valide, obligatoireniveauStringValeur par défaut : L3

Entité Encadreur

ChampTypeRègleidLongClé primaire auto-généréenomStringObligatoireemailStringFormat valide, obligatoirespecialiteStringObligatoiredisponibiliteInteger≥ 0

Entité Thème/Projet

ChampTypeRègleidLongClé primaire auto-généréetitreStringUnique ou contrôlé, obligatoiredescriptionStringObligatoiredomaineStringObligatoirestatutEnumEN_ATTENTE, VALIDE, REJETE, SOUTENUetudiantFK → ÉtudiantNon nulencadreurFK → EncadreurNon nul

Relations


Un Étudiant est associé à un seul Thème (1–0..1)
Un Encadreur peut encadrer plusieurs Thèmes (1–N)
Un Thème appartient obligatoirement à un Étudiant et à un Encadreur



4. ENDPOINTS REST (à dupliquer pour les 3 ressources : /api/etudiants, /api/encadreurs, /api/themes)

MéthodeEndpointActionCode succèsGET/api/{ressource}Liste complète200GET/api/{ressource}/{id}Détail200GET/api/etudiants?search={terme}Recherche par matricule ou nom200POST/api/{ressource}Création201PUT/api/{ressource}/{id}Modification200DELETE/api/{ressource}/{id}Suppression204GET/api/dashboardStatistiques (nb étudiants, encadreurs, thèmes soumis/validés/en attente)200

Codes d'erreur : 400 (données invalides), 404 (ressource introuvable), 500 (erreur serveur).


5. ÉLÉMENTS TECHNIQUES OBLIGATOIRES (sans eux l'application ne fonctionne pas)

5.1 CORS

Configurer explicitement l'autorisation des appels depuis http://localhost:4200 vers /api/**, sur les méthodes GET, POST, PUT, DELETE.

5.2 Gestion centralisée des erreurs

Créer un @RestControllerAdvice unique (GlobalExceptionHandler) qui intercepte :


ResourceNotFoundException (custom) → 404
MethodArgumentNotValidException → 400 avec le détail des champs invalides
Exception générique → 500


Toutes les réponses d'erreur doivent suivre un format JSON cohérent, par exemple :

json{ "timestamp": "...", "status": 404, "message": "Étudiant introuvable" }

5.3 DTOs

Ne jamais exposer directement les entités JPA en JSON (évite les boucles infinies de sérialisation dues aux relations). Créer EtudiantDTO, EncadreurDTO, ThemeDTO, avec mapping manuel dans la couche service.


6. RÈGLES DE VALIDATION FONCTIONNELLE


Étudiant : matricule unique, e-mail valide, nom/prénom obligatoires, niveau = L3 par défaut si non renseigné.
Encadreur : e-mail valide, spécialité obligatoire, disponibilité ≥ 0.
Thème : titre unique ou contrôlé, statut parmi les 4 valeurs autorisées, étudiant et encadreur non nuls.


Validation côté Back-end (Bean Validation @NotNull, @Email, etc.) et côté Front-end (formulaires réactifs Angular avec Validators).


7. COMPOSANTS ANGULAR ATTENDUS


DashboardComponent — affichage des statistiques
EtudiantListComponent, EtudiantFormComponent, EtudiantDetailComponent
EncadreurListComponent, EncadreurFormComponent
ThemeListComponent, ThemeFormComponent (avec sélection étudiant/encadreur, changement de statut)
ConfirmDeleteComponent (confirmation avant suppression, réutilisable)
Services : EtudiantService, EncadreurService, ThemeService, DashboardService (tous basés sur HttpClient)
Routing : navigation SPA complète, sans rechargement de page
Gestion des erreurs HTTP : intercepteur ou gestion par service, affichage via ngx-toastr


Organisation des dossiers Angular : un dossier par fonctionnalité (etudiants/, encadreurs/, themes/, dashboard/, shared/, core/).

Organisation des packages Spring Boot : par responsabilité (controller/, service/, repository/, entity/, dto/, exception/, config/).


8. LIVRABLES FINAUX ATTENDUS


Code source complet (backend/, frontend/)
Application fonctionnelle démontrable en local
README avec : installation, lancement (backend puis frontend), structure du projet, liste des endpoints, captures d'écran
Collection Postman ou tableau de tests (minimum 12 tests : au moins 4 par ressource couvrant GET/POST/PUT/DELETE et un cas d'erreur)
Swagger UI accessible et fonctionnel



9. MÉTHODE DE TRAVAIL — PHASES À EXÉCUTER DANS L'ORDRE

Phase 0 — Initialisation
Créer la structure du dépôt (backend/, frontend/, docs/), initialiser le projet Spring Boot (Spring Initializr ou équivalent) avec les dépendances listées, initialiser le projet Angular avec routing activé, initialiser Git avec un .gitignore adapté aux deux stacks.

Phase 1 — Back-end : modèle de données
Créer les 3 entités JPA avec annotations de validation, les enums nécessaires, les repositories Spring Data JPA.

Phase 2 — Back-end : logique métier et API
Créer les DTOs, les services (logique métier + règles de validation métier comme l'unicité du matricule), les contrôleurs REST pour les 3 ressources + dashboard, la configuration CORS, le GlobalExceptionHandler, la configuration Swagger.

Phase 3 — Back-end : tests
Écrire des tests unitaires/intégration clés (au moins un test par contrôleur), vérifier manuellement chaque endpoint, produire la liste des tests Postman (méthode, URL, corps JSON, résultat attendu).

Phase 4 — Front-end : socle
Créer les modèles TypeScript, les services HTTP, la configuration du routing, la structure des dossiers par fonctionnalité.

Phase 5 — Front-end : composants
Créer le dashboard, les listes, formulaires, détails et confirmation de suppression pour les 3 ressources, avec validation réactive.

Phase 6 — Intégration Front/Back
Connecter chaque composant à son service, gérer les erreurs HTTP et afficher les messages utilisateurs (succès/erreur) via ngx-toastr, vérifier le CRUD complet de bout en bout pour les 3 ressources.

Phase 7 — Finalisation
Rédiger le README complet, prendre les captures d'écran des principales pages, vérifier que l'application démarre proprement depuis zéro (backend + frontend), livrer la collection Postman finale.


INSTRUCTION FINALE

Commence directement par la Phase 0. Progresse phase par phase sans t'arrêter entre chaque étape, en donnant à chaque fin de phase un résumé court des fichiers produits. Si un choix d'implémentation mineur n'est pas précisé ici, prends la décision la plus simple et la plus standard, et signale-le en une ligne dans ton résumé plutôt que de t'arrêter pour demander.