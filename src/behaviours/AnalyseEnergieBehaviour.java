package behaviours;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AnalyseEnergieBehaviour extends SimpleBehaviour {
    private List<Double> historique = new ArrayList<>();
    private final int TAILLE_HISTORIQUE = 5;
    private int nombreAnalyses = 0;
    private final int MAX_ANALYSES = 15;
    private static final DecimalFormat df = new DecimalFormat("0.0");
    private static final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm:ss");
    private boolean isFinished = false;

    public AnalyseEnergieBehaviour(Agent a) {
        super(a);
    }

    private double calculerMoyenne() {
        if (historique.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (double cons : historique) {
            sum += cons;
        }
        return sum / historique.size();
    }

    private void suggererEconomies (double consommation) {
        if (consommation > 150) {
            System.out.println("[AnalyseurEnergie] RECOMMANDATION: Réduire la consommation (Actuel: " + df.format(consommation) + " kW)");
        }
    }

    @Override
    public void action() {
        // 1. Générer une valeur entre 50 kW et 200 kW
        double consommation = 50.0 + Math.random() * 150.0;

        // 2. Maintenir une moyenne mobile sur les 5 dernières mesures
        historique.add(consommation);
        if (historique.size() > TAILLE_HISTORIQUE) {
            historique.remove(0); // Supprimer la plus ancienne
        }

        // 3. Calculer la moyenne
        double moyenne = calculerMoyenne();

        // 4. Afficher les résultats
        String heure = LocalTime.now().format(tf);
        System.out.println("[AnalyseurEnergie] " + heure +
                " - Conso: " + df.format(consommation) + " kW. " +
                "Moyenne mobile (" + TAILLE_HISTORIQUE + "): " + df.format(moyenne) + " kW.");

        // 5. Suggérer des économies
        suggererEconomies(consommation);

        // 6. Incrémenter le compteur et vérifier l'arrêt
        nombreAnalyses++;
        if (nombreAnalyses >= MAX_ANALYSES) {
            isFinished = true;
            myAgent.doDelete(); // Arrêter l'agent
        } else {
            // 7. Attendre 7 secondes (7000 ms) pour la prochaine analyse
            block(7000);
        }
    }

    @Override
    public boolean done() {
        return isFinished;
    }
}
// Note: Comme pour les autres, l'état (historique, nombreAnalyses) est déplacé
// dans le comportement pour l'externalisation.