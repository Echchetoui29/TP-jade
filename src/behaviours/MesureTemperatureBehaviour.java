package behaviours;

import agents.CapteurTemperatureAgent; // Nécessaire si on veut accéder aux champs privés/méthodes de l'agent
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MesureTemperatureBehaviour extends TickerBehaviour {
    private int nombreMesures = 0;
    private final int MAX_MESURES = 20;
    private static final DecimalFormat df = new DecimalFormat("0.0");
    private static final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public MesureTemperatureBehaviour (Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        // Générer température aléatoire (Entre 15°C et 30°C)
        double temperature = 15.0 + Math.random() * 15.0;

        // Afficher la mesure
        String heure = LocalTime.now().format(tf);
        System.out.println("[CapteurTemp] " + heure + " - Température: " + df.format(temperature) + "°C");

        // Vérifier alerte si > 28°C
        if (temperature > 28.0) {
            System.out.println(">>> ALERTE CapteurTemp: Température > 28°C détectée.");
        }

        // Incrémenter compteur
        nombreMesures++;

        // Arrêter l'agent si MAX_MESURES atteint
        if (nombreMesures >= MAX_MESURES) {
            System.out.println("CapteurTemp: Nombre maximum de mesures (" + MAX_MESURES + ") atteint.");
            myAgent.doDelete(); // Arrêter l'agent
        }
    }
}
// Note: Dans cette version externalisée, j'ai mis les compteurs dans le behaviour
// pour simplifier l'accès. Si on voulait les laisser dans l'Agent, il faudrait
// que l'AgentCapteurTemperatureAgent ait des méthodes publiques pour l'accès.