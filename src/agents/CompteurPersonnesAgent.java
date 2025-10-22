package agents;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CompteurPersonnesAgent extends Agent {
    private int personnesPresentes = 0;
    private int totalEntrees = 0;
    private int totalSorties = 0;
    private int operations = 0;
    private static final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");

    protected void setup() {
        System.out.println("Agent " + getLocalName() + " démarré.");

        // TODO: Comportement périodique (simulation entrées/sorties toutes les 4 secondes)
        addBehaviour(new ComptagePersonnesBehaviour(this, 4000));

        // Le comportement d'affichage des statistiques est géré à l'intérieur du TickerBehaviour
    }

    protected void takeDown() {
        System.out.println("Agent " + getLocalName() + " terminé.");
        afficherStatistiques(); // Afficher les stats finales
    }

    private void afficherStatistiques() {
        // TODO: Comportement d'affichage statistiques
        System.out.println("\n=== STATISTIQUES (" + getLocalName() + ") ===");
        System.out.println("Personnes actuelles: " + personnesPresentes);
        System.out.println("Total entrées: " + totalEntrees);
        System.out.println("Total sorties: " + totalSorties);
        System.out.println("===========================\n");
    }

    private class ComptagePersonnesBehaviour extends TickerBehaviour {
        public ComptagePersonnesBehaviour(Agent a, long period) {
            super(a, period);
        }

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
}