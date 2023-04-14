package Client;

import java.util.List;
import java.util.Scanner;

public class MatchRequest {
    public static void playMatch() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter team 1 ID: ");
        int team1Id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("TEAM 1 :" + TeamRequests.getTeamName(team1Id));
        List<PlayerRequests> team1_players = TeamRequests.getTeamPlayers(team1Id);
        System.out.println("TEAM 1 PLAYERS :");
        for (PlayerRequests e : team1_players) {
            System.out.println(e.getName() +" "+ e.getRating());
        }
        System.out.println("Enter team 2 ID: ");
        int team2Id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("TEAM 2 :" + TeamRequests.getTeamName(team2Id));
        List<PlayerRequests> team2_players = TeamRequests.getTeamPlayers(team2Id);
        System.out.println("TEAM 2 PLAYERS :");
        for (PlayerRequests e : team2_players) {
            System.out.println(e.getName() +" "+ e.getRating());
        }

        double team1_overall_rating = 0;
        double team2_overall_rating = 0;

        int number_players_team1 = 0;
        int number_players_team2 = 0 ;
        for (PlayerRequests e : team1_players) {
            number_players_team1 ++;
            team1_overall_rating = team1_overall_rating +  e.getRating();
        }
        for (PlayerRequests e : team2_players) {
            number_players_team2 ++;
            team2_overall_rating = team2_overall_rating + e.getRating();
        }
        team1_overall_rating = team1_overall_rating / number_players_team1;
        team2_overall_rating = team2_overall_rating / number_players_team2;

        if (team1_overall_rating > team2_overall_rating) {
            System.out.println("Le gagnant du match est : " + TeamRequests.getTeamName(team1Id) + ". Avec un rating de: " + team1_overall_rating + "\n");
        }else {
            System.out.println("Le gagnant du match est : " + TeamRequests.getTeamName(team2Id) + ". Avec un rating de: " + team2_overall_rating+  "\n");
        }
    }
}
