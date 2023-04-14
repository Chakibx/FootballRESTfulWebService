package org.Resources;

import com.google.gson.Gson;
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

@Path("/teams")
public class TeamResources {
    private String host = "mysql-globalhumanressources.alwaysdata.net";
    private String base = "globalhumanressources_sports_prediction";
    private String user = "300818";
    private String password = "GHR-2023";
    private String url = "jdbc:mysql://" + host + "/" + base;

    //recuperer toutes les equipes
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTeams() {
       try{
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
            GenericEntity<List<Team>> entity = new GenericEntity<>(teams) {};
            return Response.ok(entity).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (ClassNotFoundException e) {
           throw new RuntimeException(e);
       }
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeam(@PathParam("id") int teamId) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM teams WHERE team_id = ?");
            statement.setInt(1, teamId);
            ResultSet resultSet = statement.executeQuery();

            //List<Player> players = new ArrayList<>();
            //while (resultSet.next()) {
                //int playerId = resultSet.getInt("id");
                String teamName = resultSet.getString("name");
                //teamId = resultSet.getInt("team_id");
                //players.add(new Player(playerId, playerName, teamId));
           // }

            return Response.ok(teamName).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //methode pour recuperer les joueurs d'une équipe
    @Path("/{id}/players")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeamPlayers(@PathParam("id") int teamId) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE team_id = ?");
            statement.setInt(1, teamId);
            ResultSet resultSet = statement.executeQuery();

            List<Player> players = new ArrayList<>();
            while (resultSet.next()) {
                int playerId = resultSet.getInt("id");
                String playerName = resultSet.getString("name");
                teamId = resultSet.getInt("team_id");
                players.add(new Player(playerId, playerName, teamId));
            }

            return Response.ok(players).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //methode pour ajouter une équipe
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

    //methode pour affecter un joueur a une equipe
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
                return Response.status(Response.Status.NOT_FOUND).entity("Le joueur avec l'ID " + playerId + " n'existe pas.").build();
            }

            // Vérifier si l'équipe existe
            String teamQuery = "SELECT * FROM teams WHERE id = ?";
            PreparedStatement teamStmt = conn.prepareStatement(teamQuery);
            teamStmt.setInt(1, teamId);
            ResultSet teamResult = teamStmt.executeQuery();

            if (!teamResult.next()) {
                return Response.status(Response.Status.NOT_FOUND).entity("L'équipe avec l'ID " + teamId + " n'existe pas.").build();
            }

            // Mettre à jour l'équipe du joueur
            String updateQuery = "UPDATE players SET team_id = ? WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, teamId);
            updateStmt.setInt(2, playerId);
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                return Response.status(Response.Status.OK).entity("Le joueur avec l'ID " + playerId + " a été affecté à l'équipe avec l'ID " + teamId + ".").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Impossible de mettre à jour le joueur avec l'ID " + playerId + ".").build();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
