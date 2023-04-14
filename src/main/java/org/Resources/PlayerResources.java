package org.Resources;

import java.sql.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Chakib MOUSSAOUI
 * @author Ayoub SALAH
 * Cette classe représente une ressource pour les joueurs.
 * Elle permet de récupérer et modifier tous les joueurs depuis une base de données MySQL et de les retourner sous forme de réponse JSON.
 */
@Path("/players")
public class PlayerResources {
    private String host = "mysql-globalhumanressources.alwaysdata.net";
    private String base = "globalhumanressources_sports_prediction";
    private String user = "300818_admin";
    private String password = "GHR-2023";
    private String url = "jdbc:mysql://" + host + "/" + base;

    /**
     * Cette méthode permet de récupérer tous les joueurs depuis la base de données et de les retourner sous forme de réponse JSON.
     * @return une réponse HTTP contenant la liste des joueurs en format JSON
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPlayer() {
        try {
            // Charger le driver JDBC pour MySQL
            Class.forName("com.mysql.jdbc.Driver");
            // Se connecter à la base de données
            Connection connection = DriverManager.getConnection(url, user, password);
            // Préparer la requête SQL pour récupérer tous les joueurs
            String query = "SELECT * FROM players";
            PreparedStatement statement = connection.prepareStatement(query);
            // Exécuter la requête SQL et récupérer le résultat
            ResultSet resultSet = statement.executeQuery();
            // Créer une liste de joueurs
            List<Player> players = new ArrayList<>();

            // Parcourir le résultat et ajouter chaque joueur à la liste
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int teamId = resultSet.getInt("team_id");
                int rating = resultSet.getInt("rating");

                Player player = new Player(id, name, teamId, rating);
                players.add(player);
            }
            // Retourner la liste des joueurs en tant que réponse HTTP
            GenericEntity<List<Player>> entity = new GenericEntity<>(players) {};
            return Response.ok(entity).build();
        } catch (SQLException e) {
            // En cas d'erreur SQL, retourner une réponse HTTP avec un code d'erreur 500 et un message d'erreur
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving players").build();
        } catch (ClassNotFoundException e) {
            // Si le driver JDBC n'est pas trouvé, lancer une exception RuntimeException
            throw new RuntimeException(e);
        }
    }

    /**
     * Cette méthode permet d'ajouter un joueur à la base de données.
     * Elle prend en entrée une chaîne de caractères JSON représentant le joueur à ajouter.
     * Elle retourne une réponse HTTP indiquant si l'ajout a été effectué avec succès ou non.
     * @param json une chaîne de caractères JSON représentant le joueur à ajouter
     * @return une réponse HTTP indiquant si l'ajout a été effectué avec succès ou non
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPlayer(String json) {
        Gson gson = new Gson();
        try {
            // Convertir la chaîne JSON en objet Player
            Player player = gson.fromJson(json, Player.class);
            System.out.println(player.toString());
            Class.forName("com.mysql.jdbc.Driver");

            // Connexion à la base de données
            Connection conn = DriverManager.getConnection(url, user, password);

            // Requête SQL pour ajouter le joueur
            String sql = "INSERT INTO players (name,team_id) VALUES (?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, player.getName());
            statement.setInt(2, player.getTeamId());
            int rowsInserted = statement.executeUpdate();
            statement.close();
            conn.close();

            // Vérifier si l'ajout a été effectué avec succès et retourner une réponse HTTP appropriée
            if (rowsInserted > 0) {
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (JsonSyntaxException e) {
            // En cas d'erreur de syntaxe JSON, retourner une réponse HTTP avec un code d'erreur 400 et un message d'erreur
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON format").build();
        } catch (SQLException e) {
            // En cas d'erreur SQL, retourner une réponse HTTP avec un code d'erreur 500 et un message d'erreur
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add player to database").build();
        } catch (ClassNotFoundException e) {
            // Si le driver JDBC n'est pas trouvé, lancer une exception RuntimeException
            throw new RuntimeException(e);
        }
    }
    /**
     * Cette méthode permet de mettre à jour un joueur en mettant son teamId à null dans la base de données.
     * Elle prend en entrée l'identifiant du joueur à mettre à jour.
     * Elle retourne une réponse HTTP indiquant si la mise à jour a été effectuée avec succès ou non.
     * @param playerId l'identifiant du joueur à mettre à jour
     * @return une réponse HTTP indiquant si la mise à jour a été effectuée avec succès ou non
     */
    @PUT
    @Path("/{playerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePlayer(@PathParam("playerId") int playerId) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            // Connexion à la base de données
            Connection conn = DriverManager.getConnection(url, user, password);

            // Requête SQL pour mettre à jour le joueur en mettant son teamId à null
            String sql = "UPDATE players SET team_id = NULL WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, playerId);
            int rowsUpdated = statement.executeUpdate();
            statement.close();
            conn.close();

            // Vérifier si la mise à jour a été effectuée avec succès et retourner une réponse HTTP appropriée
            if (rowsUpdated > 0) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Player not found").build();
            }
        } catch (SQLException e) {
            // En cas d'erreur SQL, retourner une réponse HTTP avec un code d'erreur 500 et un message d'erreur
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update player in database").build();
        } catch (ClassNotFoundException e) {
            // Si le driver JDBC n'est pas trouvé, lancer une exception RuntimeException
            throw new RuntimeException(e);
        }
    }
    /**
     * Cette méthode permet de supprimer un joueur de la base de données.
     * Elle prend en entrée l'identifiant du joueur à supprimer.
     * Elle retourne une réponse HTTP indiquant si la suppression a été effectuée avec succès ou non.
     * @param playerId l'identifiant du joueur à supprimer
     * @return une réponse HTTP indiquant si la suppression a été effectuée avec succès ou non
     */
    @Path("/{id}")
    @DELETE
    public Response deletePlayer(@PathParam("id") int playerId) {
        String query = "DELETE FROM players WHERE id = ?";
        try    {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, playerId);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                // Si aucun joueur n'a été supprimé, retourner une réponse HTTP avec un code d'erreur 404 et un message d'erreur
                return Response.status(Response.Status.NOT_FOUND).entity("Player not found").build();
            }
            // Si la suppression a été effectuée avec succès, retourner une réponse HTTP avec un code 204 (No Content)
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (SQLException e) {
            // En cas d'erreur SQL, retourner une réponse HTTP avec un code d'erreur 500 et un message d'erreur
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting player").build();
        } catch (ClassNotFoundException e) {
            // Si le driver JDBC n'est pas trouvé, lancer une exception RuntimeException
            throw new RuntimeException(e);
        }
    }
}