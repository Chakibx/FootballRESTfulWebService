package Client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeamRequests {
    public String getName() {
        return name;
    }
    public Integer getId() {
        return id;
    }
    private String name;
    private Integer id;

    public TeamRequests(){
    }
    public TeamRequests(String name) {
        this.name = name;
    }
    //------------------------------------------------------------------------------------------------------------------
    public static TeamRequests createTeam() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Team name: ");
        String name = scanner.nextLine();

        return new TeamRequests(name);
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
    //------------------------------------------------------------------------------------------------------------------
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
    //------------------------------------------------------------------------------------------------------------------
    public static List<TeamRequests> getAllTeams() {
        Client client = ClientBuilder.newClient();

        Response response = client.target("http://localhost:8080/ws/webapi/teams")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<TeamRequests> teams = response.readEntity(new GenericType<List<TeamRequests>>(){});
            client.close();
            return teams;
        } else {
            System.out.println("Error getting teams");
            client.close();
            return null;
        }
    }
    public static void displayAllTeams() {
        List<TeamRequests> teams = TeamRequests.getAllTeams();
        for (TeamRequests team : teams) {
            System.out.println("ID: " + team.getId() + ", Name: " + team.getName());
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public static List<PlayerRequests> getTeamPlayers(int teamId) {
        List<PlayerRequests> players = new ArrayList<>();
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target("http://localhost:8080/ws/webapi/teams/" + teamId + "/players");
            Response response = target.request(MediaType.APPLICATION_JSON).get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                players = response.readEntity(new GenericType<List<PlayerRequests>>() {});
            } else {
                System.err.println("Error getting team players. Status code: " + response.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

    public static void displayTeamPlayersFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter team ID: ");
        int teamId = scanner.nextInt();
        List<PlayerRequests> players = getTeamPlayers(teamId);
        for (PlayerRequests player : players) {
            //System.out.println(player.toString());
            System.out.println("ID: " + player.getId() + ", Name: " + player.getName());
        }
    }
}
