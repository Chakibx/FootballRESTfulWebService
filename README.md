**Projet RESTful Web Services**

* Description: 

    Ce projet consiste en la mise en place d'un service Web RESTful pour la gestion d'équipes sportives. Il permet de créer, lire, mettre à jour et supprimer des équipes à partir d'une base de données.

* Technologies utilisées 
  * Java
  * Jakarta EE
  * Jersey Framework
  * MySQL
  * Gson
* Installation : 
  * Clonez le dépôt Git sur votre machine locale :
  bash Copy code git clone https://github.com/votre-nom/projet-restful.gitImportez le projet dans votre IDE préféré.

  * Démarrez le serveur d'application et accédez à l'URL suivante : http://localhost:8080/ws/webapi/teams.

* Utilisation
Le service Web RESTful permet les opérations CRUD (Create, Read, Update, Delete) suivantes :

  * Créer une équipe : envoyez une requête POST à l'URL http://localhost:8080/ws/webapi/teams avec les données de l'équipe au format JSON.
  * Lire une équipe : envoyez une requête GET à l'URL http://localhost:8080/ws/webapi/teams/{id} où {id} est l'identifiant de l'équipe.
  * Mettre à jour une équipe : envoyez une requête PUT à l'URL http://localhost:8080/ws/webapi/teams/{id} avec les nouvelles données de l'équipe au format JSON.
  * Supprimer une équipe : envoyez une requête DELETE à l'URL http://localhost:8080/ws/webapi/teams/{id} où {id} est l'identifiant de l'équipe.
* Auteurs
  * Chakib Moussaoui - chakib_moussaoui@outlook.fr
  * Ayoub Salah - ayoubsalah.c@gmail.com