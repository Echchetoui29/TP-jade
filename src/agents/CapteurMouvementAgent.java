package agents;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CapteurMouvementAgent extends Agent {
    private int nombreDetections = 0;
    private static final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");

    protected void setup() {
        System.out.println("Agent " + getLocalName() + " démarré.");

        // TODO: Ajouter un CyclicBehaviour ou TickerBehaviour
        // TickerBehaviour car il est périodique (toutes les 3 secondes)
        addBehaviour(new DetectionMouvementBehaviour(this, 3000));
    }

    protected void takeDown() {
        System.out.println("Agent " + getLocalName() + " terminé. Total détections: " + nombreDetections);
    }

    // Méthode pour simuler la détection
    private boolean detecterMouvement() {
        return Math.random() < 0.4; // 40% de chance
    }

    private class DetectionMouvementBehaviour extends TickerBehaviour {
        public DetectionMouvementBehaviour(Agent a, long period) {
            super(a, period);
        }

        protected void onTick() {
            String heure = LocalTime.now().format(tf);

            if (detecterMouvement()) {
                // Mouvement détecté
                nombreDetections++;
                System.out.println("[CapteurMouv] " + heure + " - MOUVEMENT DÉTECTÉ ! (Total: " + nombreDetections + ")");
            } else {
                // Aucun mouvement
                System.out.println("[CapteurMouv] " + heure + " - Aucun mouvement");
            }
        }
    }
}