package Client;

public class Main {
    public static void main(String[] args) {
        //TeamRequests.displayAllTeams();
        PlayerRequests.displayAllPlayers();
        //TeamRequests.addPlayerToTeam();
    }
}

/*package Client;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Bienvenue dans l'application de gestion des équipes de football!\n");
            System.out.println("Sélectionnez une option:");
            System.out.println("1. Créer une équipe");
            System.out.println("2. Créer un joueur");
            System.out.println("3. Ajouter un joueur à une équipe");
            System.out.println("4. Supprimer un joueur d'une équipe");
            System.out.println("5. Modifier les statistiques d'un joueur");
            System.out.println("6. Quitter l'application\n");
            System.out.print("Entrez votre choix (1-6): ");

            int choix = scanner.nextInt();
            scanner.nextLine(); // pour consommer le retour à la ligne

            switch (choix) {
                case 1:
                    System.out.print("Entrez le nom de l'équipe: ");
                    String nomEquipe = scanner.nextLine();
                    // Appeler la méthode pour créer une équipe
                    break;
                case 2:
                    System.out.print("Entrez le nom du joueur: ");
                    String nomJoueur = scanner.nextLine();
                    System.out.print("Entrez l'id de l'équipe du joueur: ");
                    int ageJoueur = scanner.nextInt();
                    PlayerRequests player = PlayerRequests.createPlayer();
                    player.savePlayer();
                    break;
                case 3:
                    System.out.print("Entrez le nom de l'équipe: ");
                     nomEquipe = scanner.nextLine();
                    System.out.print("Entrez le nom du joueur: ");
                     nomJoueur = scanner.nextLine();
                    //TeamRequests.addPlayerToTeam();
                    break;
                case 4:
                    System.out.print("Entrez le nom de l'équipe: ");
                     nomEquipe = scanner.nextLine();
                    System.out.print("Entrez le nom du joueur: ");
                     nomJoueur = scanner.nextLine();
                    // Appeler la méthode pour supprimer un joueur d'une équipe
                    break;
                case 5:
                    System.out.print("Entrez le nom du joueur: ");
                     nomJoueur = scanner.nextLine();
                    System.out.print("Entrez la statistique à modifier (ex: 'buts', 'passes décisives'): ");
                    String statistique = scanner.nextLine();
                    System.out.print("Entrez la nouvelle valeur: ");
                    int valeur = scanner.nextInt();
                    // Appeler la méthode pour modifier les statistiques d'un joueur
                    break;
                case 6:
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
*/