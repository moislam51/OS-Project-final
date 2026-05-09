package views;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GanttPanel extends JPanel {

    private List<String>  processIds = new ArrayList<>();
    private List<Integer> startTimes = new ArrayList<>();
    private List<Integer> endTimes   = new ArrayList<>();

    private static final int BAR_HEIGHT  = 50;
    private static final int BAR_Y       = 30;
    private static final int LABEL_Y     = BAR_Y + BAR_HEIGHT + 20;
    private static final int LEFT_MARGIN = 20;

    // One color per unique process ID (cycles if more than 8)
    private static final Color[] COLORS = {
        new Color(100, 149, 237), // cornflower blue
        new Color(144, 238, 144), // light green
        new Color(255, 179, 71),  // orange
        new Color(255, 105, 97),  // red
        new Color(186, 130, 233), // purple
        new Color(72,  209, 204), // turquoise
        new Color(255, 218, 103), // yellow
        new Color(144, 195, 144)  // muted green
    };

    public GanttPanel() {
        setBackground(Color.WHITE);
    }

    public void setChartData(List<String> ids, List<Integer> starts, List<Integer> ends) {
        this.processIds = ids;
        this.startTimes = starts;
        this.endTimes   = ends;

        // Resize panel width based on content
        int totalTime = ends.isEmpty() ? 0 : ends.get(ends.size() - 1) - starts.get(0);
        int width = LEFT_MARGIN * 2 + totalTime * 40 + 60;
        setPreferredSize(new Dimension(Math.max(width, 600), BAR_Y + BAR_HEIGHT + 60));
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (processIds == null || processIds.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int firstStart = startTimes.get(0);
        int lastEnd    = endTimes.get(endTimes.size() - 1);
        int totalTime  = lastEnd - firstStart;

        // Scale: pixels per time unit
        int availableWidth = getWidth() - LEFT_MARGIN * 2;
        double scale = (double) availableWidth / totalTime;

        for (int i = 0; i < processIds.size(); i++) {
            String id    = processIds.get(i);
            int start    = startTimes.get(i);
            int end      = endTimes.get(i);

            int x      = LEFT_MARGIN + (int) ((start - firstStart) * scale);
            int width  = (int) ((end - start) * scale);

            // Pick color based on process id hash
            Color color = COLORS[Math.abs(id.hashCode()) % COLORS.length];

            // Draw filled bar
            g2.setColor(color);
            g2.fillRect(x, BAR_Y, width, BAR_HEIGHT);

            // Draw border
            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRect(x, BAR_Y, width, BAR_HEIGHT);

            // Draw process ID centered in bar
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 13));
            FontMetrics fm = g2.getFontMetrics();
            int textX = x + (width - fm.stringWidth(id)) / 2;
            int textY = BAR_Y + (BAR_HEIGHT + fm.getAscent()) / 2 - 3;
            if (width > fm.stringWidth(id) + 4)
                g2.drawString(id, textX, textY);

            // Draw start time below bar
            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(String.valueOf(start), x, LABEL_Y);
        }

        // Draw last end time
        int lastX = LEFT_MARGIN + (int) ((lastEnd - firstStart) * scale);
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(String.valueOf(lastEnd), lastX, LABEL_Y);
    }
}