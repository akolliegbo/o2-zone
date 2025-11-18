// cancer cell class stuff
// this cell is immortal and hates the rules
public class CancerCell extends Cell {
    // new cancer specific parameters (using Greek symbols for formal description)
    // we'll call them 'cancer_threshold' and 'cancer_prob'
    private static final double THETA_D_CANCER = 0.45; // new, lower division threshold (now includes yellow region)
    private static final double P_DIV_CANCER = 0.75; // 75% chance to divide (higher than 50% normal)

    // constructor
    public CancerCell(int x, int y, Environment env) {
        super(x, y, env); // call the parent cell constructor
        // important: we set the counter to a value that signifies unlimited growth
        // by making it large or setting a flag, but for simplicity, we just ignore it in decideWhatToDo()
        this.color = java.awt.Color.MAGENTA; // cancer cells are magenta for visibility
    }

    // this is the new cancer brain where the immortal decisions are made
    @Override
    public void decideWhatToDo() {
        if (!isAlive()) return; // check if already dead

        double o2 = getEnvironment().getOxygenAt(getX(), getY());
        
        // 1. Lethality Check (Same as normal cell - cancer still dies in deep hypoxia)
        // this keeps the model realistic, as cancer still needs some energy
        if (o2 < getEnvironment().MIN_O2 + 0.05) { 
            die();
            return;
        }
        // 2. Abnormal Proliferation Check (No Counter + Lower O2 Threshold)
        // the normal cell needs o2 > 0.6 AND counter > 0
        // the cancer cell only needs o2 > 0.45 (THETA_D_CANCER) and ignores the counter
        if (o2 >= THETA_D_CANCER) {
            // green region (o2 >= 0.6) is optimal, yellow region (0.45 <= o2 < 0.6) is suboptimal growth
            
            // set probability based on region: 
            double current_p_div = P_DIV_CANCER; // 75% chance in green (o2 >= 0.6)
            if (o2 < getEnvironment().O2_DIVIDE_THRESHOLD) {
                // if we are in the yellow region (0.45 <= o2 < 0.6), reduce the probability slightly
                current_p_div = 0.60; // still higher than normal 50%, but less aggressive
            }

            if (Math.random() < current_p_div) {
                // since we skip the division counter check, this is now a malignant division
                divide();
            } else {
                move();
            }
        } else {
            // 3. Quiescence/Movement (if 0.15 < o2 < 0.45)
            // if o2 is below the cancer threshold, it still moves to find a better spot
            move();
        }
    }

    // the divide method must be overridden to NOT decrease the division counter, 
    // modeling telomerase activation and immortality
    @Override
    public void divide() {
        // find an empty spot next to me like a little baby cell nursery
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int spawnX = getX() + i;
                int spawnY = getY() + j;

                if (i == 0 && j == 0) continue; // dont spawn on top of myself obviously

                if (getEnvironment().isLocationEmpty(spawnX, spawnY)) {
                    // found a spot! make a baby cancer cell
                    // crucial: we create a new CancerCell, not a new Cell
                    CancerCell newCell = new CancerCell(spawnX, spawnY, getEnvironment()); 
                    // crucial: do NOT call divisionCounter--
                    getEnvironment().placeCell(newCell, spawnX, spawnY);
                    return; 
                }
            }
        }
    }
}