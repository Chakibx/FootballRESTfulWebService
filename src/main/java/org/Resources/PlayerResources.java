package org.Resources;
import java.sql.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.PreparedStatement;

@Path("/players")
public class PlayerResources {
    private String url = "mysql-globalhumanressources.alwaysdata.net";
    private String username = "globalhumanressources_sports_prediction";
    private String password = "GHR-2023";

    //methode pour recuperer un joueur
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayer(@PathParam("id") int playerId) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
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
        ObjectMapper mapper = new ObjectMapper();
        try {
            Player player = mapper.readValue(json, Player.class);

            Class.forName("com.mysql.jdbc.Driver");

            // Connexion à la base de données
            Connection conn = DriverManager.getConnection(url, username, password);

            // Requête SQL pour ajouter le joueur
            String sql = "INSERT INTO players (name, team_id) VALUES (?, ?)";
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
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON format").build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add player to database").build();
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
            Connection connection = DriverManager.getConnection(url, username, password);
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

