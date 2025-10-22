package agents;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CapteurTemperatureAgent extends Agent {
    private int nombreMesures = 0;
    private final int MAX_MESURES = 20;
    private static final DecimalFormat df = new DecimalFormat("0.0");
    private static final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");

    protected void setup() {
        System.out.println("Agent " + getLocalName() + " démarré.");

        // TODO: Ajouter un TickerBehaviour pour mesurer toutes les 5 secondes
        addBehaviour(new MesureTemperatureBehaviour(this, 5000));
    }

    protected void takeDown() {
        System.out.println("Agent " + getLocalName() + " terminé. Total mesures: " + nombreMesures);
    }

    // Classe interne pour le comportement
    private class MesureTemperatureBehaviour extends TickerBehaviour {
        public MesureTemperatureBehaviour (Agent a, long period) {
            super(a, period);
        }

        protected void onTick() {
            // TODO: Générer température aléatoire (Entre 15°C et 30°C)
            double temperature = 15.0 + Math.random() * 15.0; // 15.0 à 30.0

            // TODO: Afficher la mesure
            String heure = LocalTime.now().format(tf);
            System.out.println("[CapteurTemp] " + heure + " - Température: " + df.format(temperature) + "°C");

            // TODO: Vérifier alerte si > 28°C
            if (temperature > 28.0) {
                System.out.println(">>> ALERTE CapteurTemp: Température > 28°C détectée.");
            }

            // TODO: Incrémenter compteur
            nombreMesures++;

            // TODO: Arrêter l'agent si MAX_MESURES atteint
            if (nombreMesures >= MAX_MESURES) {
                System.out.println("CapteurTemp: Nombre maximum de mesures (" + MAX_MESURES + ") atteint.");
                myAgent.doDelete(); // Arrêter l'agent
            }
        }
    }
}