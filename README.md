# FaceReco


## Installation base de données

Développé sous Postgree 12.1 et pgAdmin 4.16.

Ordre d'utilisation des scripts dans `src/main/sql` :

1. database_creation.sql
2. trigger.sql
3. right.sql
4. role_account.sql
5. data.sql

## Installation du projet du projet (sous Eclipse IDE)

Importer le projet `File > Import...` en tant que `Existing Maven Projects`.

###Installation dépendances OpenCV_contrib

Dans la console à la racine du projet (besoin de maven):

```
mvn install:install-file -Dfile=src\main\resources\opencv_contrib\opencv_contrib-4.2.jar -DgroupId=org -DartifactId=opencv_contrib -Dversion=4.2 -Dpackaging=jar
mvn install:install-file -Dfile=src\main\resources\opencv_contrib\opencv_contrib-4.2.dll -DgroupId=org -DartifactId=opencv_contrib -Dversion=4.2 -Dpackaging=dll -DgeneratePom=true
```

Clic-droit sur le projet `Build Path > Configure Build Path...`

Sélectionner l'onglet `Libraries`.

Ouvrir l'option `JRE System library` et sélectionner `Native library location`.

Cliquer sur le bouton `Edit...`.

Cliquer sur le bouton `External Folder...` et sélectionner le dossier `opencv_contrib` disponible dans `src\main\resources\opencv_contrib`.


## Configuration du projet

Dans le fichier `application.properties` dans le répertoire `src/main/resources` :

Modifier vos identifiants de connexion :

```
spring.datasource.username=postgres
spring.datasource.password=root
```

## Lancer le projet

Lancer la classe main `FaceRecoApplication.java` dans `src/main/java/Group1/FaceReco`.


## Utilisateur de l'application

Un utilisateur administrateur est créé avec comme identifiant pour vous connecter à l'API :

```
username : admin
mdp : admin
```

## Documentation API

`localhost:8080`

## Compiler le projet

Dans la console à la racine du projet (besoin de maven):

```
mvn compile
mvn package
```

## Lancer le projet compiler

Dans la console à la racine du projet (besoin de java):

`java -Djava.library.path=target\native -jar target\FaceReco-1.0.0-SNAPSHOT.jar`

