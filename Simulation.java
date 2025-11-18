// this is the god class this is where we start everything
// main function will be here
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import javax.swing.JFrame; // oh my god window stuff now
import javax.swing.SwingUtilities; // for ui thread stuff
import javax.swing.Timer; // THIS IS THE BIG CHANGE
import java.awt.event.ActionEvent; // and we need this
import java.awt.event.ActionListener; // and this

public class Simulation {
    // fields for the environment and maybe max time steps
    private final Environment environment = new Environment();
    // a list or collection to hold all the active Cell agents
    private final List<Cell> cellAgents = new ArrayList<>();
    private final RadiationTherapy therapy = new RadiationTherapy(environment); // <--- INITIALIZED HERE

    // simulation parameters
    private final int MAX_STEPS = 200; 
    private final int DELAY = 100; // milliseconds between steps
    private int stepCount = 0; // to track our progress

    private JFrame frame;
    private DisplayPanel displayPanel;
    private Timer simulationTimer; // our new timer object

    public Simulation() {
        // oh lord i have to start by putting some cells down 
        // lets put like ten cells down in the middleish high o2 area
        for (int i = 0; i < 10; i++) {
            int startX = 5 + i; // spread them out a little
            int startY = environment.getHeight() / 2;
            if (environment.isLocationEmpty(startX, startY)) {
                Cell newCell = new Cell(startX, startY, environment);
                cellAgents.add(newCell);
                environment.placeCell(newCell, startX, startY);
            }
        }

        if (cellAgents.size() >= 5) {
            Cell normalCellToTrack = cellAgents.get(4);
            // Define a bright Pink color for tracking normal lineage
            java.awt.Color pink = new java.awt.Color(255, 105, 180); 
            normalCellToTrack.startLineageTracking(pink);
        }

        // lets put one cancer cell down to disrupt homeostasis
        int cancerX = 15; // slightly away from the starting normal cells
        int cancerY = environment.getHeight() / 2;
        if (environment.isLocationEmpty(cancerX, cancerY)) {
            // create a CancerCell instead of a Cell
            CancerCell patientZero = new CancerCell(cancerX, cancerY, environment);
            //java.awt.Color purple = java.awt.Color.magenta; 
            //patientZero.startLineageTracking(purple);
            cellAgents.add(patientZero); // use the same list, but ensure all agents are updated
            environment.placeCell(patientZero, cancerX, cancerY);
        }
    }

    // setting up the actual window for the simulation
    private void setupWindow() {
        frame = new JFrame("O2 Gradient ABM"); // window title is important
        displayPanel = new DisplayPanel(environment);
        frame.add(displayPanel); // put our drawing panel into the window
        frame.pack(); // size the window to fit the panel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // make it close properly
        frame.setVisible(true); // show it off
    }

    // this method will be called by the timer every "delay" milliseconds
    private void stepSimulation() {
        System.out.println("\n--- time step " + (stepCount + 1) + " --- total cells: " + cellAgents.size() + " ---");

        // we have to iterate over a copy to avoid errors when cells divide or die
        List<Cell> currentCells = new ArrayList<>(cellAgents);
        
        for (Cell cell : currentCells) {
            if (cell.isAlive()) {
                cell.decideWhatToDo();
            }
        }

        // cleanup dead cells and add new cells from the environment
        // this is a simple (but not most efficient) way to sync the list
        cellAgents.clear();
        for (int y = 0; y < environment.getHeight(); y++) {
            for (int x = 0; x < environment.getWidth(); x++) {
                Cell cell = environment.getCellAt(x, y);
                if (cell != null) {
                    cellAgents.add(cell);
                }
            }
        }

        // okay i need to show the results otherwise this is pointless 
        // tell the panel to redraw itself with the new cell positions
        displayPanel.repaint(); 

        // if there are no cells left like im done
        if (cellAgents.isEmpty()) {
            System.out.println("every cell died rip");
            simulationTimer.stop(); // stop the timer
        }
    }

    // this new method sets up and starts the timer
    public void startSimulation() {
        System.out.println("starting the simulation i hope this works and looks pretty");
        
        // this is the timer's action. it runs on the ui thread.
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // what to do on each "tick" of the timer
                
                stepCount++; // advance the step
                stepSimulation(); // run one step of logic

                if (stepCount == 50 || stepCount == 100) {
                // we apply the dose BEFORE running the step logic
                therapy.applyDose(cellAgents, stepCount); // pass the current step count
            }
                // check if we should stop
                if (stepCount >= MAX_STEPS) {
                    System.out.println("simulation finished omg im exhausted");
                    simulationTimer.stop(); // stop the timer
                }
            }
        };

        // create the timer and start it
        simulationTimer = new Timer(DELAY, taskPerformer);
        simulationTimer.start();
    }


    // main method to call runSimulation
    public static void main(String[] args) {
        // okay lets go start the whole thing
        // swing stuff should always run on the event dispatch thread for safety
        SwingUtilities.invokeLater(() -> {
            Simulation sim = new Simulation();
            sim.setupWindow(); // setup the window first
            sim.startSimulation(); // then start the timer
        });
    }
}