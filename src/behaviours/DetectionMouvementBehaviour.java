package behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DetectionMouvementBehaviour extends TickerBehaviour {
    private int nombreDetections = 0;
    private static final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public DetectionMouvementBehaviour(Agent a, long period) {
        super(a, period);
    }

    private boolean detecterMouvement() {
        return Math.random() < 0.4; // 40% de chance
    }

    @Override
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