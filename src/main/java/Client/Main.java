

package Client;
import org.Resources.Player;
import org.Resources.Team;
import java.util.Scanner;

/**
 * Classe principale de l'application de gestion des équipes de football.
 * @author SALAH Ayoub
 * @author MOUSSAOUI Chakib
 */
public class Main {

    /**

     Fonction principale qui affiche le menu principal et effectue les actions demandées par l'utilisateur.

     @param args les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        System.out.println("Bienvenue dans l'application de gestion des équipes de football!\n");

        while (true) {
            System.out.println("*******************************************************************************\n");
            System.out.println("Voici le menu principal: \n");
            System.out.println("Bienvenue dans l'application de gestion des équipes de football!\n");
            // Affichage des options disponibles
            System.out.println("Sélectionnez une option:");
            System.out.println("0. Commencer un match");
            System.out.println("1. Afficher toutes les équipe");
            System.out.println("2. Afficher tous les joueurs");
            System.out.println("3. Afficher tous les joueurs d'une équipe");
            System.out.println("4. Créer une équipe");
            System.out.println("5. Créer un joueur");
            System.out.println("6. Ajouter un joueur à une équipe");
            System.out.println("7. Supprimer un joueur d'une équipe");
            System.out.println("8. Modifier les statistiques d'un joueur");
            System.out.println("9. Quitter l'application");
            System.out.println("10. Supprimer un joueur");
            System.out.println("11. Afficher les informations d'un joueur");

            // Lecture du choix de l'utilisateur
            System.out.print("Entrez votre choix (0-11): ");
            Scanner scanner = new Scanner(System.in);
            int choix = scanner.nextInt();

            // Exécution de l'option choisie
            switch (choix) {
                case 0:
                    MatchRequest.playMatch();
                    break;
                case 1:
                    TeamRequests.displayAllTeams();
                    break;
                case 2:
                    PlayerRequests.displayAllPlayers();
                    break;
                case 3:
                    TeamRequests.displayTeamPlayersFromUserInput();
                    break;
                case 4:
                    TeamRequests teamRequests = TeamRequests.createTeam();
                    teamRequests.saveTeam();
                    break;
                case 5:
                    PlayerRequests playerRequests = PlayerRequests.createPlayer();
                    playerRequests.savePlayer();
                    break;
                case 6:
                    PlayerRequests.deletePlayerFromConsole();
                    break;
                case 7:
                    TeamRequests.addPlayerToTeam();
                    break;
                case 8:
                    PlayerRequests.updatePlayer();
                    break;
                case 9:
                    System.out.println("Merci d'avoir utilisé l'application!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
                    break;
            }

        }
    }
}