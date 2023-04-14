/**
 * @file Player.java
 * @brief Définition de la classe Player
 */

package org.Resources;

/**
 * @class Player
 * @brief Classe représentant un joueur
 */
public class Player {
 private int rating; /**< Niveau de compétence du joueur */
 private int id; /**< Identifiant du joueur */
 private String name; /**< Nom du joueur */
 private int teamId; /**< Identifiant de l'équipe du joueur */

 /**
  * @brief Constructeur de la classe Player
  * @param id Identifiant du joueur
  * @param name Nom du joueur
  * @param teamId Identifiant de l'équipe du joueur
  * @param rating Niveau de compétence du joueur
  */
 public Player(int id, String name, int teamId, int rating) {
  this.id = id;
  this.name = name;
  this.teamId = teamId;
  this.rating = rating;
 }

 /**
  * @brief Convertit l'objet en chaîne de caractères
  * @return La chaîne de caractères représentant l'objet
  */
 @Override
 public String toString() {
  return "Player{" +
          "rating=" + rating +
          ", id=" + id +
          ", name='" + name + '\'' +
          ", teamId=" + teamId +
          '}';
 }

 /**
  * @brief Accesseur pour le niveau de compétence du joueur
  * @return Le niveau de compétence du joueur
  */
 public int getRating() {
  return rating;
 }

 /**
  * @brief Mutateur pour le niveau de compétence du joueur
  * @param rating Le nouveau niveau de compétence du joueur
  */
 public void setRating(int rating) {
  this.rating = rating;
 }

 /**
  * @brief Accesseur pour l'identifiant du joueur
  * @return L'identifiant du joueur
  */
 public int getId() {
  return id;
 }

 /**
  * @brief Mutateur pour l'identifiant du joueur
  * @param id Le nouvel identifiant du joueur
  */
 public void setId(int id) {
  this.id = id;
 }

 /**
  * @brief Accesseur pour le nom du joueur
  * @return Le nom du joueur
  */
 public String getName() {
  return name;
 }

 /**
  * @brief Mutateur pour le nom du joueur
  * @param name Le nouveau nom du joueur
  */
 public void setName(String name) {
  this.name = name;
 }

 /**
  * @brief Accesseur pour l'identifiant de l'équipe du joueur
  * @return L'identifiant de l'équipe du joueur
  */
 public int getTeamId() {
  return teamId;
 }

 /**
  * @brief Mutateur pour l'identifiant de l'équipe du joueur
  * @param teamId Le nouvel identifiant de l'équipe du joueur
  */
 public void setTeamId(int teamId) {
  this.teamId = teamId;
 }
}
