import java.awt.Color; // oh my god gotta import color now

// ugh okay so this is the world map basically
// it needs to handle the grid and where the oxygen is
public class Environment {
    // fields for grid dimensions, the actual grid array, and the oxygen array
    private final int width = 80; // make it a bit wider for better viz
    private final int height = 60; // and a bit taller
    private final Cell[][] grid = new Cell[width][height];
    private final double[][] oxygenLevels = new double[width][height];

    // a constant for the high oxygen value, like max oxygen
    public static final double MAX_O2 = 1.0;
    // a constant for the low oxygen value, like min oxygen
    public static final double MIN_O2 = 0.1;
    // a constant for the critical division threshold
    public static final double O2_DIVIDE_THRESHOLD = 0.6; 
    
    // constructor
    public Environment() {
        // omg gotta set up the whole world first
        setupGrid();
        initializeOxygenGradient();
    }
    
    // function to make the grid
    // just making sure all the spots are null initially like an empty field
    private void setupGrid() {
        // i mean, java initializes it to null but better safe than sorry i guess?
        // whatever, moving on
    }
    
    // this will make the invisible oxygen line across the map
    // maybe it should be high on the left and low on the right like a simple slope
    private void initializeOxygenGradient() {
        // ugh loop through every single tile this is gonna take forever
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // creating a simple linear gradient across the x axis
                // so like if x is 0 its max o2 and if x is 50 its min o2
                double o2 = MAX_O2 - (x / (double)width) * (MAX_O2 - MIN_O2);
                oxygenLevels[x][y] = o2;
            }
        }
    }
    
    // the cell will ask for its oxygen level using this
    public double getOxygenAt(int x, int y) {
        // is this even a valid coordinate? should check boundaries lol
        if (x < 0 || x >= width || y < 0 || y >= height) {
            // omg the cell is off the map return zero oxygen its literally nowhere
            return 0.0;
        }
        return oxygenLevels[x][y];
    }
    
    // i guess we need to visually show the oxygen too so cells know what's up
    // now this returns an actual color object for the display panel
// this returns a smoothly interpolated color object for the display panel
public Color getColorForOxygen(double oxygenLevel) {
    // Normalize O2 level to a 0.0 to 1.0 scale based on the environment's range
    // O2_NORM is 0.0 at MIN_O2 (0.1) and 1.0 at MAX_O2 (1.0)
    double o2Range = MAX_O2 - MIN_O2;
    double o2Norm = Math.max(0.0, Math.min(1.0, (oxygenLevel - MIN_O2) / o2Range));

    int r, g, b;

    // Transition from Red (low O2) -> Yellow (mid O2) -> Green (high O2)
    
    if (o2Norm < 0.5) {
        // Red (o2Norm=0) to Yellow (o2Norm=0.5)
        // Red is 255, Green transitions from 0 to 255
        r = 255;
        g = (int) (o2Norm * 2.0 * 255.0); // 0.0 -> 1.0
        b = 0;
    } else {
        // Yellow (o2Norm=0.5) to Green (o2Norm=1.0)
        // Green is 255, Red transitions from 255 to 0
        r = (int) ((1.0 - o2Norm) * 2.0 * 255.0); // 1.0 -> 0.0
        g = 255;
        b = 0;
    }

    // Slightly tone down the pigments by mixing with gray/black (making it darker/richer)
    int shade = 190; // Scale factor to dim the color (max 255)
    return new Color(r * shade / 230, g * shade / 230, b * shade / 230);
}
    
    // maybe a helper function for the cell to check if the spot it wants to move to is empty
    public boolean isLocationEmpty(int x, int y) {
        // check boundaries first like a boss
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        // if the grid spot is null then its empty woohoo
        return grid[x][y] == null;
    } 

    // probably need a way to put a cell down and remove one
    public void placeCell(Cell cell, int x, int y) {
        // assuming islocationempty was checked first or this will be chaos
        // also checking boundaries again just in case
        if (x >= 0 && x < width && y >= 0 && y < height) {
            grid[x][y] = cell;
        }
    }
    
    public void removeCell(int x, int y) {
        // rip cell
        if (x >= 0 && x < width && y >= 0 && y < height) {
            grid[x][y] = null;
        }
    }

    // getter for the cell at a specific location, useful for the display
    public Cell getCellAt(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null; // outside boundaries no cell there
        }
        return grid[x][y];
    }

    // getters so the simulation can loop through the world
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}