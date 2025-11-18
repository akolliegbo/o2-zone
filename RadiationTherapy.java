import java.util.List;
import java.util.Iterator;
import java.util.Random;

// radiation therapy class stuff
// this is the treatment that we apply to every single cell regardless of if its normal or cancer
// radiation therapy is most effective where there is high oxygen which is so annoying
public class RadiationTherapy {
    // fields for the therapy's overall strength and the environment reference
    private final Environment environment;
    private final Random random = new Random();
    
    // parameters for radiosensitivity based on O2 zones
    // omg these are like the greek letters from the exam
    private static final double P_KILL_GREEN = 0.85; // 585 chance to kill in high O2
    private static final double P_KILL_YELLOW = 0.5; // 50% chance to kill in medium O2
    private static final double P_KILL_RED = 0.15; // 5% chance to kill in low O2 (radioresistance)

    // constructor
    public RadiationTherapy(Environment env) {
        this.environment = env;
    }

    // this is the main function that applies the therapy dose to the whole population
    public void applyDose(List<Cell> cellAgents, int stepCount) {
        System.out.println("OMG radiation dose applied this is gonna hurt");

        // gotta use an iterator again because cells might die and ruin the list
        Iterator<Cell> iterator = cellAgents.iterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            
            // if it's already dead or removed (somehow) just skip it
            if (!cell.isAlive()) {
                iterator.remove();
                continue;
            }

            double o2 = environment.getOxygenAt(cell.getX(), cell.getY());
            double p_kill = getKillProbability(o2); // figure out the chance to die
            
            // check if the cell is unlucky and gets killed by the radiation
            if (random.nextDouble() < p_kill) {
                // rip cell is instantly killed by the therapy
                cell.die(); // this removes it from the grid
                iterator.remove(); // this removes it from the cell list
            }

            //  ACQUIRE WARBURG RESISTANCE AFTER SECOND DOSE
            else if (stepCount == 100 && cell instanceof CancerCell) {
                // if the cell is a CancerCell AND it survived the second dose, it evolves!
                ((CancerCell) cell).acquireWarburgResistance();
            }
        }
    }

    // helper function to determine the kill probability based on oxygen
    private double getKillProbability(double o2) {
        // use the environment's thresholds for simplicity and consistency
        if (o2 >= Environment.O2_DIVIDE_THRESHOLD) {
            // green region (high oxygen) - most sensitive
            return P_KILL_GREEN;
        } else if (o2 > Environment.MIN_O2 + 0.05) {
            // yellow region (medium oxygen) - less sensitive
            return P_KILL_YELLOW;
        } else {
            // red region (hypoxic) - highly resistant
            // note: if a cell is in the lethal zone (o2 < 0.15) it would have died anyway, 
            // but this covers the slightly less hypoxic red zone too
            return P_KILL_RED;
        }
    }
}