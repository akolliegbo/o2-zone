// this is the agent this is the actual cell
// it needs to know where it is and if it's dead or alive
public class Cell {
    // fields for position (x, y), isAlive, and a reference to the environment
    private int x;
    private int y;
    private boolean isAlive = true;
    private final Environment environment;
    protected java.awt.Color color = java.awt.Color.BLACK;

    // internal counter to limit divisions for homeostasis like a hayflick limit thing
    private int divisionCounter = 5; 

    public Environment getEnvironment() {
        return environment;
    }
    // getter for the cell's color
    public java.awt.Color getColor() {
        return this.color;
    }
    
    // constructor
    public Cell(int x, int y, Environment env) {
        this.x = x;
        this.y = y;
        this.environment = env;
    }
    
    // this is where the magic happens this is the cell brain
    // it checks the oxygen and decides if it wants to move divide or die like a whole drama
    public void decideWhatToDo() {
        if (!isAlive) return; // like oh my god stop trying to do stuff if youre dead

        double o2 = environment.getOxygenAt(x, y);

        // okay survival first if the oxygen is way too low you just die
        if (o2 < Environment.MIN_O2 + 0.05) { // a tiny buffer
            die();
        // if the oxygen is good and we still have divisions left lets multiply
        } else if (o2 > Environment.O2_DIVIDE_THRESHOLD && divisionCounter > 0) {
            // maybe a 50 50 chance to move or divide to keep things from getting too packed 
            if (Math.random() < 0.5) {
                divide();
            } else {
                move();
            }
        // otherwise just move around a little to find a better spot 
        } else {
            move();
        }
    } 
    
    // okay moving is important it cant just sit there
    public void move() {
        // pick a random adjacent spot like a dork
        int newX = x + (int)(Math.random() * 3) - 1; // -1, 0, or 1
        int newY = y + (int)(Math.random() * 3) - 1; 

        // check if the new spot is valid and empty because chaos is bad
        if (environment.isLocationEmpty(newX, newY)) {
            // its empty yay remove from old spot place in new spot 
            environment.removeCell(x, y);
            x = newX;
            y = newY;
            environment.placeCell(this, x, y);
        }
    }
    
    // gotta make more cells to fill up the tissue unless the counter is up
    public void divide() {
        // find an empty spot next to me like a little baby cell nursery
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int spawnX = x + i;
                int spawnY = y + j;

                if (i == 0 && j == 0) continue; // dont spawn on top of myself obviously

                if (environment.isLocationEmpty(spawnX, spawnY)) {
                    // okay found a spot decrement my counter and make the baby
                    divisionCounter--;
                    Cell newCell = new Cell(spawnX, spawnY, environment);
                    // the new cell gets the same counter as the parent at time of division
                    newCell.divisionCounter = this.divisionCounter; 
                    environment.placeCell(newCell, spawnX, spawnY);
                    // im done im too tired now
                    return; 
                }
            }
        }
    }
    
    // when the oxygen is too low or its time is up the cell dies sad face
    public void die() {
        // set the flag and remove it from the grid its gone
        isAlive = false;
        environment.removeCell(x, y);
        // this is so depressing 
    }

    // getters for simulation and debugging 
    public boolean isAlive() { return isAlive; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getDivisionCounter() { return divisionCounter; }
}