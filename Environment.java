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
    public Color getColorForOxygen(double oxygenLevel) {
        // okay if its super high its like a vibrant green for life
        if (oxygenLevel > 0.8) return Color.GREEN;
        // mid level is fine maybe yellow for caution
        else if (oxygenLevel > 0.4) return Color.YELLOW;
        // low oxygen is like super stressful and red for danger
        else return Color.RED;
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