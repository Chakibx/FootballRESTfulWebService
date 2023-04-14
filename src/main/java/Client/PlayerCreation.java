package Client;

import java.util.Scanner;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class PlayerCreation {
    private String name;
    private int teamId;

    public PlayerCreation(){

    }
    public PlayerCreation(String name, int teamId) {
        this.name = name;
        this.teamId = teamId;
    }

    public static PlayerCreation createPlayer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter player name: ");
        String name = scanner.nextLine();
        System.out.println("Enter player team ID: ");
        int teamId = scanner.nextInt();
        scanner.nextLine(); // to consume the new line character after the integer input

        return new PlayerCreation(name, teamId);
    }

    public String toJson() {
        return "{\"name\": \"" + name + "\", \"teamId\": " + teamId + "}";
    }

    public void savePlayer() {

        try {
            Client client = ClientBuilder.newClient();

            String json = this.toJson();
            Response response = client.target("http://localhost:8080/ws/webapi/players")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(json));

            String output = response.readEntity(String.class);
            System.out.println("Output from Server .... \n" + output);

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



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
}