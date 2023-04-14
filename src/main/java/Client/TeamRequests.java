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
/**
 * Cette classe représente les demandes des ressources api qui concerne les équipes.
 * @author SALAH Ayoub
 *  @author MOUSSAOUI Chakib
 */
public class TeamRequests {
    /**
     * Nom de l'équipe.
     */
    private String name;
    /**
     * Identifiant de l'équipe.
     */
    private Integer id;

    /**
     * Constructeur par défaut.
     */
    public TeamRequests(){
    }

    /**
     * Constructeur avec paramètre.
     * @param name nom de l'équipe.
     */
    public TeamRequests(String name) {
        this.name = name;
    }

    /**
     * Récupère le nom de l'équipe.
     * @return nom de l'équipe.
     */
    public String getName() {
        return name;
    }

    /**
     * Récupère l'identifiant de l'équipe.
     * @return identifiant de l'équipe.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Crée une nouvelle équipe à partir de l'entrée utilisateur.
     * @return une nouvelle équipe.
     */
    public static TeamRequests createTeam() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Team name: ");
        String name = scanner.nextLine();

        return new TeamRequests(name);
    }

    /**
     * Convertit l'objet en chaîne JSON.
     * @return la chaîne JSON.
     */
    public String toJson() {
        return "{\"name\": \"" + name + "\"}";
    }

    /**
     * Enregistre l'équipe sur le serveur.
     */
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
    /**
     * Lit l'ID du joueur à partir de l'entrée utilisateur.
     * @return l'ID du joueur.
     */
    public static int readPlayerIdFromInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter player ID: ");
        int playerId = scanner.nextInt();
        scanner.nextLine(); // consommer le caractère de nouvelle ligne
        return playerId;
    }

    /**
     * Lit l'ID de l'équipe à partir de l'entrée utilisateur.
     * @return l'ID de l'équipe.
     */
    public static int readTeamIdFromInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter team ID: ");
        int teamId = scanner.nextInt();
        scanner.nextLine(); // consommer le caractère de nouvelle ligne
        return teamId;
    }

    /**
     * Ajoute un joueur à une équipe sur le serveur.
     */
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
    /**
     * Récupère la liste de toutes les équipes depuis le serveur.
     *
     * @return une liste de TeamRequests contenant toutes les équipes récupérées depuis le serveur.
     *         Si une erreur se produit lors de la récupération, retourne null.
     */
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

    /**
     * Affiche la liste de toutes les équipes dans la console.
     * Si une erreur se produit lors de la récupération des équipes depuis le serveur,
     * un message d'erreur sera affiché à la place.
     */
    public static void displayAllTeams() {
        List<TeamRequests> teams = TeamRequests.getAllTeams();
        if (teams == null) {
            System.out.println("Failed to retrieve teams from server.");
            return;
        }
        for (TeamRequests team : teams) {
            System.out.println("ID: " + team.getId() + ", Name: " + team.getName());
        }
    }
//--------------------------------------------------------------------------------------------------------------
    /**

     Retrieves a list of players belonging to a specific team from the web service.
     @param teamId the ID of the team to retrieve the players for.
     @return a List of PlayerRequests objects representing the players belonging to the specified team.
     */
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

    /**

     Displays a list of players belonging to a team based on user input.
     */
    public static void displayTeamPlayersFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter team ID: ");
        int teamId = scanner.nextInt();
        List<PlayerRequests> players = getTeamPlayers(teamId);
        String teamName = getTeamName(teamId);
        System.out.println("Team name: " + teamName);
        for (PlayerRequests player : players) {
            System.out.println("ID: " + player.getId() + ", Name: " + player.getName());
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Récupère le nom d'une équipe en fonction de son identifiant.
     *
     * @param teamId l'identifiant de l'équipe
     * @return le nom de l'équipe
     */
    public static String getTeamName(int teamId) {
        String teamName = "";
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target("http://localhost:8080/ws/webapi/teams/" + teamId);
            Response response = target.request(MediaType.APPLICATION_JSON).get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                TeamRequests team = response.readEntity(TeamRequests.class);
                teamName = team.getName();
            } else {
                System.err.println("Error getting team name. Status code: " + response.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teamName;
    }

}
