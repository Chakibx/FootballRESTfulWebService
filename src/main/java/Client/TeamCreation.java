package Client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Scanner;

public class TeamCreation {
    private String name;

    public TeamCreation(){

    }
    public TeamCreation(String name) {
        this.name = name;
    }

    public static TeamCreation createTeam() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Team name: ");
        String name = scanner.nextLine();

        return new TeamCreation(name);
    }

    public String toJson() {
        return "{\"name\": \"" + name + "\"}";
    }

    public void saveTeam() {

        try {
            Client client = ClientBuilder.newClient();

            String json = this.toJson();
            Response response = client.target("http://localhost:8080/ws/webapi/teams")
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

        public static int readTeamIdFromInput() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter team ID: ");
            int teamId = scanner.nextInt();
            scanner.nextLine(); // consommer le caractère de nouvelle ligne
            return teamId;
        }



    public static void addPlayerToTeam() {
        try {
            int playerId = readPlayerIdFromInput();
            int teamId = readTeamIdFromInput();
            Client client = ClientBuilder.newClient();

            String json = "{\"playerId\": " + playerId + ", \"teamId\": " + teamId + "}";
            Response response = client.target("http://localhost:8080/ws/webapi/teams/" + teamId + "/players/" +playerId)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.json(json));

            String output = response.readEntity(String.class);
            System.out.println("Output from Server .... \n" + output);

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
