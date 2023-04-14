package Client;

import java.util.List;
import java.util.Scanner;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class PlayerRequests {
    private String name;
    private int teamId;
    private int id;
    private int rating;

    @Override
    public String toString() {
        return "PlayerRequests{" +
                "name='" + name + '\'' +
                ", teamId=" + teamId +
                ", id=" + id +
                ", rating=" + rating +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getTeamId() {
        return teamId;
    }
    public int getRating() {
        return teamId;
    }

    public PlayerRequests(){

    }
    public PlayerRequests(String name, int teamId, int rating) {
        this.name = name;
        this.teamId = teamId;
        this.rating = rating;
    }

    //------------------------------------------------------------------------------------------------------------------
    public static PlayerRequests createPlayer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter player name: ");
        String name = scanner.nextLine();
        System.out.println("Enter player team ID: ");
        int teamId = scanner.nextInt();
        System.out.println("Enter player rating: ");
        int rating = scanner.nextInt();
        scanner.nextLine(); // to consume the new line character after the integer input

        return new PlayerRequests(name, teamId, rating);
    }

    public String toJson(){
        return "{\"name\": \"" + name + "\", \"teamId\": " + teamId + ", \"rating\": " + rating + "}";
    }

    public void savePlayer() {

        try {
            Client client = ClientBuilder.newClient();

            String json = this.toJson();
            Response response = client.target("http://localhost:8080/ws/webapi/players")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(json));

            String output = response.readEntity(String.class);

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    public static int readPlayerIdFromInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter player ID: ");
        int playerId = scanner.nextInt();
        scanner.nextLine(); // consommer le caractère de nouvelle ligne
        return playerId;
    }
    public static void updatePlayer() {
        try {
            int playerId = readPlayerIdFromInput();
            Client client = ClientBuilder.newClient();

            Response response = client.target("http://localhost:8080/ws/webapi/players/" + playerId)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.json("{\"teamId\": null}"));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                System.out.println("Player updated successfully.");
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                System.out.println("Player not found.");
            } else {
                System.out.println("Failed to update player.");
            }

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public static List<PlayerRequests> getAllPlayers() {
        Client client = ClientBuilder.newClient();

        Response response = client.target("http://localhost:8080/ws/webapi/players")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<PlayerRequests> players = response.readEntity(new GenericType<List<PlayerRequests>>(){});
            client.close();
            return players;
        } else {
            System.out.println("Error getting players");
            client.close();
            return null;
        }
    }
    public static void displayAllPlayers() {
        List<PlayerRequests> players = PlayerRequests.getAllPlayers();
        for (PlayerRequests player : players) {
            System.out.println("ID: " + player.getId() + ", Name: " + player.getName() + ", Team ID: "+player.getTeamId() + ", Rating:" + player.getRating());
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public static void deletePlayerFromConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the id of the player to delete:");
        int playerId = scanner.nextInt();
        scanner.close();

        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target("http://localhost:8080/ws/webapi/players/" + playerId);
            Response response = target.request().delete();
            if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
                System.out.println("Player deleted successfully.");
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                System.out.println("Player not found.");
            } else {
                System.err.println("Error deleting player. Status code: " + response.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
