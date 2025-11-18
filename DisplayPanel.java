import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

// this is where the magic visuals happen like a little art canvas
public class DisplayPanel extends JPanel {
    private final Environment environment;
    private final int cellSize = 8; // how big each cell square is like pixels

    // constructor
    public DisplayPanel(Environment env) {
        this.environment = env;
        // gotta set the size or it will be tiny and sad
        setPreferredSize(new Dimension(environment.getWidth() * cellSize, environment.getHeight() * cellSize));
    }

    // this method gets called whenever the window needs to redraw itself
    // like refreshing the screen every time step
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // always call super first like a good child

        // first draw the oxygen gradient background like the world's natural colors
        for (int y = 0; y < environment.getHeight(); y++) {
            for (int x = 0; x < environment.getWidth(); x++) {
                double o2 = environment.getOxygenAt(x, y);
                g.setColor(environment.getColorForOxygen(o2)); // use the new color method
                g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize); // draw a square for each grid spot
            }
        }

        // then draw the actual cells on top of the oxygen background
        for (int y = 0; y < environment.getHeight(); y++) {
            for (int x = 0; x < environment.getWidth(); x++) {
                Cell cell = environment.getCellAt(x, y);
                if (cell != null) {
                        java.awt.Color drawColor;
                    
                    // --- CRUCIAL: COLOR PRIORITY CHECK ---
                    if (cell.getLineageColor() != null) {
                        // Task 5/6: Use the distinct lineage color (Pink or Purple)
                        drawColor = cell.getLineageColor();
                    } else {
                        // Default: Use the cell's base color (White or Cyan)
                        drawColor = cell.getColor();
                    }
                    // --- END COLOR PRIORITY CHECK ---

                    g.setColor(drawColor);
                    g.fillOval(x
                     * cellSize, y * cellSize, cellSize, cellSize);
                    // if you wanted a more detailed cell (like a circle) you'd use fillOval
                    
                }
            }
        }
    }
}