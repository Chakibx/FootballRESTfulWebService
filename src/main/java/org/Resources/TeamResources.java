package org.Resources;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Chakib MOUSSAOUI
 * @author Ayoub SALAH
 *  Cette classe représente une ressource pour les équipes.
 *  Elle permet de récupérer et modifier toutes les équipes depuis une base de données MySQL.
 *  TeamResources
 */
@Path("/teams")
public class TeamResources {
    private String host = "mysql-globalhumanressources.alwaysdata.net";
    private String base = "globalhumanressources_sports_prediction";
    private String user = "300818";
    private String password = "GHR-2023";
    private String url = "jdbc:mysql://" + host + "/" + base;

    /**
     * Cette méthode permet de récupérer toutes les équipes depuis la base de données.
     *
     * @return une réponse HTTP contenant la liste des équipes au format JSON.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTeams() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM teams");
            ResultSet rs = statement.executeQuery();

            // Créer une liste d'équipes pour stocker les résultats de la requête
            List<Team> teams = new ArrayList<>();

            // Parcourir les résultats de la requête et ajouter chaque équipe à la liste
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println("id: " + id + " name: " + name);
                Team team = new Team(id, name);
                teams.add(team);
            }

            // Retourner la liste d'équipes en tant que réponse
            GenericEntity<List<Team>> entity = new GenericEntity<>(teams) {
            };
            return Response.ok(entity).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cette méthode permet de récupérer une équipe spécifique depuis la base de données en utilisant son ID.
     *
     * @param teamId l'ID de l'équipe à récupérer.
     * @return une réponse HTTP contenant les informations de l'équipe au format JSON.
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeam(@PathParam("id") int teamId) {
        try {
            // Charger le driver JDBC pour MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Établir une connexion à la base de données
            Connection connection = DriverManager.getConnection(url, user, password);
            // Préparer une requête SQL pour récupérer l'équipe avec l'ID spécifié
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM teams WHERE id = ?");
            statement.setInt(1, teamId);
            // Exécuter la requête SQL et récupérer le résultat
            ResultSet resultSet = statement.executeQuery();

            // Si l'équipe est trouvée, créer un objet JSON contenant son nom et le retourner en tant que réponse HTTP
            if (resultSet.next()) {
                String teamName = resultSet.getString("name");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", teamName);
                return Response.ok(jsonObject.toString()).build();
            } else {
                // Si l'équipe n'est pas trouvée, retourner une réponse HTTP avec un statut NOT_FOUND
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (SQLException e) {
            // Si une exception SQL est levée, imprimer la trace de la pile et retourner une réponse HTTP avec un statut INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (ClassNotFoundException e) {
            // Si le driver JDBC n'est pas trouvé, lancer une RuntimeException
            throw new RuntimeException(e);
        }
    }

    /**
     * Cette méthode permet de récupérer les joueurs d'une équipe depuis la base de données en utilisant l'ID de l'équipe.
     *
     * @param teamId l'ID de l'équipe dont on veut récupérer les joueurs.
     * @return une réponse HTTP contenant la liste des joueurs de l'équipe au format JSON.
     */
    @Path("/{id}/players")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeamPlayers(@PathParam("id") int teamId) {
        try {
            // Charger le driver JDBC pour MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Établir une connexion à la base de données
            Connection connection = DriverManager.getConnection(url, user, password);
            // Préparer une requête SQL pour récupérer les joueurs de l'équipe avec l'ID spécifié
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE team_id = ?");
            statement.setInt(1, teamId);
            // Exécuter la requête SQL et récupérer le résultat
            ResultSet resultSet = statement.executeQuery();

            // Créer une liste de joueurs pour stocker les résultats de la requête
            List<Player> players = new ArrayList<>();
            // Parcourir les résultats de la requête et ajouter chaque joueur à la liste
            while (resultSet.next()) {
                int playerId = resultSet.getInt("id");
                String playerName = resultSet.getString("name");
                teamId = resultSet.getInt("team_id");
                int rating = resultSet.getInt("rating");

                players.add(new Player(playerId, playerName, teamId, rating));
            }

            // Retourner la liste de joueurs en tant que réponse
            return Response.ok(players).build();
        } catch (SQLException e) {
            // Si une exception SQL est levée, imprimer la trace de la pile et retourner une réponse HTTP avec un statut INTERNAL_SERVER_ERROR
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (ClassNotFoundException e) {
            // Si le driver JDBC n'est pas trouvé, lancer une RuntimeException
            throw new RuntimeException(e);
        }
    }

    /**
     * Cette méthode permet d'ajouter une équipe à la base de données.
     *
     * @param json une chaîne de caractères JSON contenant les informations de l'équipe à ajouter.
     * @return une réponse HTTP contenant l'équipe ajoutée au format JSON.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTeam(String json) {
        try {
            Gson gson = new Gson();
            Team team = gson.fromJson(json, Team.class);

            // Charger le driver JDBC pour MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Créer une connexion à la base de données
            Connection conn = DriverManager.getConnection(url, user, password);

            // Préparer la requête d'insertion de l'équipe
            String sql = "INSERT INTO teams (name) VALUES (?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, team.getName());

            // Exécuter la requête d'insertion
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                // Récupérer l'ID de l'équipe insérée
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int teamId = rs.getInt(1);
                    team.setId(teamId);
                    return Response.status(Response.Status.CREATED).entity(team).build();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * Cette méthode permet d'affecter un joueur à une équipe.
     *
     * @param teamId   l'ID de l'équipe à laquelle affecter le joueur.
     * @param playerId l'ID du joueur à affecter à l'équipe.
     * @return une réponse HTTP contenant un message indiquant si l'opération a réussi ou non.
     */
    @Path("/{teamId}/players/{playerId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlayerToTeam(@PathParam("teamId") int teamId, @PathParam("playerId") int playerId) {
        try {
            // Charger le driver JDBC pour MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Créer une connexion à la base de données
            Connection conn = DriverManager.getConnection(url, user, password);

            // Vérifier si le joueur existe
            String playerQuery = "SELECT * FROM players WHERE id = ?";
            PreparedStatement playerStmt = conn.prepareStatement(playerQuery);
            playerStmt.setInt(1, playerId);
            ResultSet playerResult = playerStmt.executeQuery();

            if (!playerResult.next()) {
                // Si le joueur n'existe pas, renvoyer une réponse HTTP avec un code d'erreur 404 (NOT_FOUND)
                return Response.status(Response.Status.NOT_FOUND).entity("Le joueur avec l'ID " + playerId + " n'existe pas.").build();
            }

            // Vérifier si l'équipe existe
            String teamQuery = "SELECT * FROM teams WHERE id = ?";
            PreparedStatement teamStmt = conn.prepareStatement(teamQuery);
            teamStmt.setInt(1, teamId);
            ResultSet teamResult = teamStmt.executeQuery();

            if (!teamResult.next()) {
                // Si l'équipe n'existe pas, renvoyer une réponse HTTP avec un code d'erreur 404 (NOT_FOUND)
                return Response.status(Response.Status.NOT_FOUND).entity("L'équipe avec l'ID " + teamId + " n'existe pas.").build();
            }

            // Mettre à jour l'équipe du joueur
            String updateQuery = "UPDATE players SET team_id = ? WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, teamId);
            updateStmt.setInt(2, playerId);
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                // Si la mise à jour a réussi, renvoyer une réponse HTTP avec un code 200 (OK) et un message indiquant que l'opération a réussi
                return Response.status(Response.Status.OK).entity("Le joueur avec l'ID " + playerId + " a été affecté à l'équipe avec l'ID " + teamId + ".").build();
            } else {
                // Si la mise à jour a échoué, renvoyer une réponse HTTP avec un code d'erreur 500 (INTERNAL_SERVER_ERROR)
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Impossible de mettre à jour le joueur avec l'ID " + playerId + ".").build();
            }
        } catch (ClassNotFoundException e) {
            // Si le driver JDBC n'est pas trouvé, lancer une RuntimeException
            throw new RuntimeException(e);
        } catch (SQLException e) {
            // Si une exception SQL est levée, renvoyer une réponse HTTP avec un code d'erreur 500 (INTERNAL_SERVER_ERROR)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}