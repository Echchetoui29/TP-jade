import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.core.Runtime;

public class Main {
    public static void main(String[] args) {
        // Créer le runtime JADE
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.GUI, "true"); // Active RMA

        // Créer le conteneur principal
        AgentContainer mainContainer = rt.createMainContainer(p);

        try {
            // Créer et démarrer les agents
            AgentController tempAgent = mainContainer.createNewAgent(
                    "CapteurTemp",
                    "agents.CapteurTemperatureAgent",
                    null
            );
            tempAgent.start();

            // TODO: Créer et démarrer les 3 autres agents
            AgentController mouvAgent = mainContainer.createNewAgent(
                    "CapteurMouv",
                    "agents.CapteurMouvementAgent",
                    null
            );
            mouvAgent.start();

            AgentController persAgent = mainContainer.createNewAgent(
                    "CompteurPers",
                    "agents.CompteurPersonnesAgent",
                    null
            );
            persAgent.start();

            AgentController enerAgent = mainContainer.createNewAgent(
                    "AnalyseurEner",
                    "agents.AnalyseurEnergieAgent",
                    null
            );
            enerAgent.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}