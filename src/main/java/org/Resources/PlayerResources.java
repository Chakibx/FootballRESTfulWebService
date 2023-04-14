package org.Resources;
import java.sql.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Path("/players")
public class PlayerResources {
    private String host = "mysql-globalhumanressources.alwaysdata.net";
    private String base = "globalhumanressources_sports_prediction";
    private String user = "300818_admin";
    private String password = "GHR-2023";
    private String url = "jdbc:mysql://" + host + "/" + base;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPlayer() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT * FROM players";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            List<Player> players = new ArrayList<>();

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int teamId = resultSet.getInt("team_id");
                Player player = new Player(id, name, teamId);
                players.add(player);
            }
            // Retourner la liste d'équipes en tant que réponse
            GenericEntity<List<Player>> entity = new GenericEntity<>(players) {};
            return Response.ok(entity).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving players").build();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    //methode pour recuperer un joueur
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayer(@PathParam("id") int playerId) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT * FROM players WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, playerId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                String name = resultSet.getString("name");
                int teamId = resultSet.getInt("team_id");
                Player player = new Player(playerId, name, teamId);
                return Response.ok(player).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Player not found").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving player").build();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Méthode pour ajouter un joueur
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPlayer(String json) {
        Gson gson = new Gson();
        try {
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

            if (rowsInserted > 0) {
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (JsonSyntaxException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON format").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add player to database").build();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

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

            if (rowsUpdated > 0) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Player not found").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update player in database").build();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Méthode pour supprimer un joueur
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
                return Response.status(Response.Status.NOT_FOUND).entity("Player not found").build();
            }
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting player").build();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}

