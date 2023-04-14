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
}
