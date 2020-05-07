# FaceReco


## Installation base de données

Développé sous Postgree 12.1 et pgAdmin 4.16.

Ordre d'utilisation des scripts dans `src/main/sql` :

1. database_creation.sql
2. trigger.sql
3. right.sql
4. role_account.sql


## Configuration du projet

Dans le fichier `application.properties` dans `src/main/resources` :

Modifier vos identifiants :
`spring.datasource.username=postgres`
`spring.datasource.password=root`

## Lancer le projet

Lancer la classe main dans `src/main/java/Group1/FaceReco`.