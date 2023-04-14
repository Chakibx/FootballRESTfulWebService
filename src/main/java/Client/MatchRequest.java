package Client;

import java.util.List;
import java.util.Scanner;

/**

 * La classe MatchRequest permet de jouer un match en demandant à l'utilisateur les identifiants des équipes à opposer
 et prédire qui gagnera le match selon le rating des joueurs.
 * @author SALAH Ayoub
 * @author MOUSSAOUI Chakib
 */
public class MatchRequest {

    /**

     * Joue un match en demandant les identifiants des équipes à opposer.

     * Affiche les noms des équipes et les joueurs de chaque équipe avec leur rating.

     * Calcule le rating global de chaque équipe.

     * Affiche le gagnant du match avec son rating global.
     */
    public static void playMatch() {

        Scanner scanner = new Scanner(System.in);

        // Demander l'identifiant de la première équipe
        System.out.println("Enter team 1 ID: ");
        int team1Id = scanner.nextInt();
        scanner.nextLine();

        // Afficher le nom de la première équipe
        System.out.println("TEAM 1 :" + TeamRequests.getTeamName(team1Id));

        // Récupérer les joueurs de la première équipe et afficher leur nom et rating
        List<PlayerRequests> team1_players = TeamRequests.getTeamPlayers(team1Id);
        System.out.println("TEAM 1 PLAYERS :");
        for (PlayerRequests e : team1_players) {
            System.out.println(e.getName() +" "+ e.getRating());
        }

        // Demander l'identifiant de la deuxième équipe
        System.out.println("Enter team 2 ID: ");
        int team2Id = scanner.nextInt();
        scanner.nextLine();

        // Afficher le nom de la deuxième équipe
        System.out.println("TEAM 2 :" + TeamRequests.getTeamName(team2Id));

        // Récupérer les joueurs de la deuxième équipe et afficher leur nom et rating
        List<PlayerRequests> team2_players = TeamRequests.getTeamPlayers(team2Id);
        System.out.println("TEAM 2 PLAYERS :");
        for (PlayerRequests e : team2_players) {
            System.out.println(e.getName() +" "+ e.getRating());
        }

        // Calculer le rating global de chaque équipe
        double team1_overall_rating = 0;
        double team2_overall_rating = 0;
        int number_players_team1 = 0;
        int number_players_team2 = 0;
        for (PlayerRequests e : team1_players) {
            number_players_team1 ++;
            team1_overall_rating = team1_overall_rating + e.getRating();
        }
        for (PlayerRequests e : team2_players) {
            number_players_team2 ++;
            team2_overall_rating = team2_overall_rating + e.getRating();
        }
        team1_overall_rating = team1_overall_rating / number_players_team1;
        team2_overall_rating = team2_overall_rating / number_players_team2;

        // Afficher le gagnant du match avec son rating global
        if (team1_overall_rating > team2_overall_rating) {
            System.out.println("Le gagnant du match est : " + TeamRequests.getTeamName(team1Id) + ". Avec un rating de: " + team1_overall_rating + "\n");
        } else {
            System.out.println("Le gagnant du match est : " + TeamRequests.getTeamName(team2Id) + ". Avec un rating de: " + team2_overall_rating+ "\n");
        }
    }
}
