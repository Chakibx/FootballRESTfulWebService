/**
 * @file Team.java
 * @brief Définition de la classe Team
 */

package org.Resources;

/**
 * @author Chakib MOUSSAOUI
 * @author Ayoub SALAH
 * @class Team class
 * @brief Représente une équipe
 */
public class Team {

    /** @brief Identifiant de l'équipe */
    private int id;

    /** @brief Nom de l'équipe */
    private String name;

    /**
     * @brief Constructeur de la classe Team
     * @param id Identifiant de l'équipe
     * @param name Nom de l'équipe
     */
    public Team(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters

    /**
     * @brief Retourne l'identifiant de l'équipe
     * @return Identifiant de l'équipe
     */
    public int getId() {
        return id;
    }

    /**
     * @brief Modifie l'identifiant de l'équipe
     * @param id Nouvel identifiant de l'équipe
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @brief Retourne le nom de l'équipe
     * @return Nom de l'équipe
     */
    public String getName() {
        return name;
    }

    /**
     * @brief Modifie le nom de l'équipe
     * @param name Nouveau nom de l'équipe
     */
    public void setName(String name) {
        this.name = name;
    }
}
