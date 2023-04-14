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

/**
 * Cette classe représente les demandes des ressources api qui concerne les joueurs.
 * @author SALAH Ayoub
 * @author MOUSSAOUI Chakib
 */
public class PlayerRequests {

    /**
     * Le nom du joueur.
     */
    private String name;

    /**
     * L'identifiant de l'équipe du joueur.
     */
    private int teamId;

    /**
     * L'identifiant du joueur.
     */
    private int id;

    /**
     * Le niveau de compétence du joueur (note sur 100).
     */
    private int rating;

    /**
     * Constructeur vide par défaut.
     */
    public PlayerRequests(){

    }

    /**
     * Constructeur prenant les informations du joueur.
     * @param name le nom du joueur.
     * @param teamId l'identifiant de l'équipe du joueur.
     * @param rating le niveau de compétence du joueur (note sur 100).
     */
    public PlayerRequests(String name, int teamId, int rating) {
        this.name = name;
        this.teamId = teamId;
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    /**
     * Retourne une chaîne de caractères représentant le joueur.
     * @return une chaîne de caractères représentant le joueur.
     */
    @Override
    public String toString() {
        return "PlayerRequests{" +
                "name='" + name + '\'' +
                ", teamId=" + teamId +
                ", id=" + id +
                ", rating=" + rating +
                '}';
    }

    /**
     * Retourne le nom du joueur.
     * @return le nom du joueur.
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne l'identifiant du joueur.
     * @return l'identifiant du joueur.
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne l'identifiant de l'équipe du joueur.
     * @return l'identifiant de l'équipe du joueur.
     */
    public int getTeamId() {
        return teamId;
    }


    //------------------------------------------------------------------------------------------------------------------
    /**
     * Cette méthode permet de créer un nouvel objet PlayerRequests en demandant les informations nécessaires à l'utilisateur via le terminal.
     * @return un nouvel objet PlayerRequests initialisé avec les valeurs entrées par l'utilisateur.
     */
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

    /**
     * Cette méthode permet de convertir l'objet courant en une chaîne de caractères JSON.
     * @return une chaîne de caractères JSON représentant l'objet courant.
     */
    public String toJson(){
        return "{\"name\": \"" + name + "\", \"teamId\": " + teamId + ", \"rating\": " + rating + "}";
    }

    /**
     * Cette méthode permet de sauvegarder l'objet courant dans la base de données en utilisant l'API RESTful exposée par le serveur.
     */
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
    /**
     * Lit l'ID du joueur à partir de la saisie utilisateur dans la console.
     *
     * @return l'ID du joueur saisi par l'utilisateur
     */
    public static int readPlayerIdFromInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter player ID: ");
        int playerId = scanner.nextInt();
        scanner.nextLine(); // consommer le caractère de nouvelle ligne
        return playerId;
    }

    /**
     * Met à jour le joueur dont l'ID est saisi par l'utilisateur en définissant l'ID de l'équipe à null.
     *
     * @throws RuntimeException si la mise à jour du joueur échoue
     */
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
            throw new RuntimeException("Failed to update player.", e);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    /**

     Récupère tous les joueurs à partir de l'API REST et les retourne sous forme de liste.

     @return la liste des joueurs.
     */
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

    /**

     Affiche tous les joueurs en appelant la méthode getAllPlayers() et parcourant la liste retournée.
     */
    public static void displayAllPlayers() {
        List<PlayerRequests> players = PlayerRequests.getAllPlayers();
        for (PlayerRequests player : players) {
            System.out.println("ID: " + player.getId() + ", Name: " + player.getName() + ", Team ID: "+player.getTeamId() + ", Rating:" + player.getRating());
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    /**

     Deletes a player from the server based on user input.
     */
    public static void deletePlayerFromConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the id of the player to delete:");
        int playerId = scanner.nextInt();

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

    //------------------------------------------------------------------------------------------------------------------
    /**

     Récupère un joueur à partir de son identifiant saisi au clavier.

     Affiche les informations du joueur s'il est trouvé, sinon affiche un message indiquant que le joueur n'a pas été trouvé.
     */
    public static void getPlayerById() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter player ID: ");
        int playerId = scanner.nextInt();
        scanner.nextLine(); // consomme la fin de ligne après la saisie du nombre

        Client client = ClientBuilder.newClient();
        Response response = client.target("http://localhost:8080/api/players/" + playerId)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            PlayerRequests player = response.readEntity(PlayerRequests.class);
            System.out.println(player);
        } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            System.out.println("Player not found");
        } else {
            System.out.println("Error retrieving player");
        }
    }

}
