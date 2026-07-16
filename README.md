# 🎓 LabéStages — Application de gestion des Stages et Thèmes

**LabéStages** est une application web moderne (SPA) développée pour l'Université de Labé (Département Informatique). Elle permet la gestion, la validation et l'attribution des sujets de projets (thèmes) de fin de cycle pour les étudiants de Licence 3.

---

## 🛠️ Stack Technique

### Backend (API REST)
* **Framework :** Spring Boot 3.2.5 (Java 19)
* **Persistance :** Spring Data JPA / Base de données en mémoire **H2**
* **Validation :** Jakarta Bean Validation (`@NotBlank`, `@Email`, `@Min`, etc.)
* **Gestion d'erreurs :** RestControllerAdvice global avec retours JSON standardisés
* **Documentation :** Swagger UI / OpenAPI 3

### Frontend (SPA)
* **Framework :** Angular 21
* **Design & UI :** Bootstrap 5 et Bootstrap Icons (`ngx-toastr` pour les notifications)
* **Gestion des formulaires :** Reactive Forms (Formulaires réactifs complets)
* **Performance :** Lazy Loading des modules composants (Dashboard, Étudiants, Thèmes)

---

## 🧱 Architecture du Projet

Le dépôt est organisé en deux dossiers indépendants d'architecture propre :

```
LabéStages/
├── backend/                  # Partie serveur Java / Spring Boot
│   ├── src/main/java         # Code source (controller, service, repository, entity, dto, exception)
│   ├── src/main/resources    # Configuration globale (application.properties)
│   └── src/test/java         # Tests unitaires des contrôleurs REST
├── frontend/                 # Application SPA Angular
│   ├── src/app/core          # Services HTTP de communication REST et modèles TS
│   ├── src/app/dashboard     # Statuts des projets et raccourcis rapides
│   ├── src/app/etudiants     # Création, modification et liste des étudiants
│   ├── src/app/encadreurs    # Visualisation et quotas libres des enseignants
│   ├── src/app/themes        # Attribution et validation des projets
│   └── src/app/shared        # Composants réutilisables (Modale de suppression, etc.)
└── docs/                     # Documentation et ressources API (Postman Collection)
```

---

## 🚀 Lancement Rapide de l'Application

### 1. Démarrer le Serveur Backend (Spring Boot)

* **Depuis IntelliJ IDEA (Recommandé) :**
  1. Ouvrez IntelliJ IDEA et importez le projet situé dans le répertoire `/backend/`.
  2. Acceptez l'activation de l'annotation processing pour **Lombok** (notification IntelliJ ou sous `Settings > Build > Compiler > Annotation Processors > Enable`).
  3. Lancez la classe principale `LabeStagesApplication.java` (clic droit -> Run).

* **En ligne de commande (Maven Wrapper) :**
  ```bash
  cd backend
  ./mvnw spring-boot:run
  ```

Le serveur sera actif sur : **`http://localhost:8080`**

* **Accès OpenAPI & Swagger :** Ouvrez `http://localhost:8080/swagger-ui.html` dans un navigateur.
* **Accès Console Database H2 :** Ouvrez `http://localhost:8080/h2-console`
  * *JDBC URL :* `jdbc:h2:file:./data/labestagesdb`
  * *Username :* `sa` (mot de passe vide)

---

### 2. Démarrer l'Application Frontend (Angular)

1. Ouvrez un nouveau terminal.
2. Naviguez vers le dossier front-end :
   ```bash
   cd frontend
   ```
3. Lancez le serveur de développement :
   ```bash
   npm start
   ```

L’application web sera accessible via le navigateur sur l'adresse : **`http://localhost:4200`**

---

## 🧪 Tests & Validation de l'API

### Tests Unitaires (JUnit 5 & MockMvc)
Les tests unitaires couvrent l'intégralité des endpoints REST et s'assurent du bon mapping HTTP, des codes de statut de réponse (`200 OK`, `201 Created`, `400 Bad Request`, `404 Not Found`) et de la validation des données d'entrée.

Pour les lancer :
```bash
cd backend
./mvnw test
```

### Collection de requêtes Postman
Un jeu complet de tests API (19 cas de test, dont les cas d'erreur de règles métier) est disponible dans le fichier :
👉 `docs/labestages_postman_collection.json`

Pour l'exécuter :
1. Ouvrez Postman.
2. Cliquez sur **Import** (en haut à gauche) -> importez ce fichier JSON.
3. Testez directement les requêtes ordonnées par ressource.
