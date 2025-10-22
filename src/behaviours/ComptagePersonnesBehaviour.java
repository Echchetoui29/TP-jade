package behaviours;

import agents.CompteurPersonnesAgent;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ComptagePersonnesBehaviour extends TickerBehaviour {
    private int personnesPresentes = 0;
    private int totalEntrees = 0;
    private int totalSorties = 0;
    private int operations = 0;
    private static final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public ComptagePersonnesBehaviour(Agent a, long period) {
        super(a, period);
    }

    private void afficherStatistiques() {
        System.out.println("\n=== STATISTIQUES (" + myAgent.getLocalName() + ") ===");
        System.out.println("Personnes actuelles: " + personnesPresentes);
        System.out.println("Total entrées: " + totalEntrees);
        System.out.println("Total sorties: " + totalSorties);
        System.out.println("===========================\n");
    }

    @Override
    protected void onTick() {
        String heure = LocalTime.now().format(tf);
        int changement = (Math.random() < 0.5) ? 1 : -1; // 50/50 pour entrée (+1) ou sortie (-1)

        if (changement == 1) {
            // Entrée
            personnesPresentes++;
            totalEntrees++;
            System.out.println("[CompteurPersonnes] " + heure + " - Personnes présentes: " + personnesPresentes + " (+1 entrée)");
        } else {
            // Sortie - NE JAMAIS descendre en dessous de 0
            if (personnesPresentes > 0) {
                personnesPresentes--;
                totalSorties++;
                System.out.println("[CompteurPersonnes] " + heure + " - Personnes présentes: " + personnesPresentes + " (-1 sortie)");
            } else {
                // Tente de sortir, mais compteur déjà à 0
                System.out.println("[CompteurPersonnes] " + heure + " - Personnes présentes: 0 (Tentative de sortie ignorée)");
            }
        }

        operations++;

        // Afficher des statistiques toutes les 10 opérations
        if (operations % 10 == 0) {
            afficherStatistiques();
        }
    }
}
// Note: Dans cette version, j'ai déplacé la logique et l'état (personnesPresentes, etc.)
// dans le comportement, car c'est la façon la plus simple de l'externaliser sans
// surcharger l'Agent de méthodes d'accès publiques.