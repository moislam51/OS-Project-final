package views;

import models.Result;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import utils.Metrics;

public class ComparisonPanel extends JPanel {

    private JTable comparisonTable;
    private JTextArea analysisArea;

    public ComparisonPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
            "Comparison Summary",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        setBackground(Color.WHITE);

        // TOP: comparison table (smaller)
        add(buildTablePanel(), BorderLayout.NORTH);
        // CENTER: analysis (takes all remaining space)
        add(buildAnalysisPanel(), BorderLayout.CENTER);
    }

    private JPanel buildTablePanel() {
        String[] columns = {"Metric", "Round Robin", "Priority"};
        String[][] data = {
            {"Avg Waiting Time",    "-", "-"},
            {"Avg Turnaround Time", "-", "-"},
            {"Avg Response Time",   "-", "-"},
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        comparisonTable = new JTable(model);
        comparisonTable.setRowHeight(30);
        comparisonTable.setFont(new Font("Arial", Font.PLAIN, 13));
        comparisonTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        comparisonTable.getTableHeader().setBackground(new Color(70, 130, 180));
        comparisonTable.getTableHeader().setForeground(Color.WHITE);
        comparisonTable.setGridColor(Color.LIGHT_GRAY);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 3; i++)
            comparisonTable.getColumnModel().getColumn(i).setCellRenderer(center);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(comparisonTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildAnalysisPanel() {
        analysisArea = new JTextArea();
        analysisArea.setEditable(false);
        analysisArea.setFont(new Font("Arial", Font.PLAIN, 13));
        analysisArea.setBackground(new Color(245, 245, 245));
        analysisArea.setLineWrap(true);
        analysisArea.setWrapStyleWord(true);
        analysisArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        analysisArea.setText("Run the simulation to see the analysis...");

        JScrollPane scroll = new JScrollPane(analysisArea);
        scroll.setPreferredSize(new Dimension(0, 300)); // tall analysis area

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            "Analysis",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 13)
        ));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    public void displayResults(Result rrResult, Result priorityResult) {
        DefaultTableModel model = (DefaultTableModel) comparisonTable.getModel();

        model.setValueAt(String.format("%.2f", rrResult.getAvgWT()),  0, 1);
        model.setValueAt(String.format("%.2f", rrResult.getAvgTAT()), 1, 1);
        model.setValueAt(String.format("%.2f", rrResult.getAvgRT()),  2, 1);

        model.setValueAt(String.format("%.2f", priorityResult.getAvgWT()),  0, 2);
        model.setValueAt(String.format("%.2f", priorityResult.getAvgTAT()), 1, 2);
        model.setValueAt(String.format("%.2f", priorityResult.getAvgRT()),  2, 2);

        highlightBetterValues(model, rrResult, priorityResult);
        analysisArea.setText(generateAnalysis(rrResult, priorityResult));
        analysisArea.setCaretPosition(0); // scroll to top
    }

    private void highlightBetterValues(DefaultTableModel model, Result rr, Result pr) {
        double[] rrVals = {rr.getAvgWT(), rr.getAvgTAT(), rr.getAvgRT()};
        double[] prVals = {pr.getAvgWT(), pr.getAvgTAT(), pr.getAvgRT()};

        comparisonTable.setDefaultRenderer(Object.class,
            new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(
                        JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int col) {
                    Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, col);
                    setHorizontalAlignment(JLabel.CENTER);
                    if (col == 1 && rrVals[row] < prVals[row])
                        c.setBackground(new Color(144, 238, 144));
                    else if (col == 2 && prVals[row] < rrVals[row])
                        c.setBackground(new Color(144, 238, 144));
                    else if (col == 0)
                        c.setBackground(new Color(230, 230, 230));
                    else
                        c.setBackground(Color.WHITE);
                    return c;
                }
            });
        comparisonTable.repaint();
    }

    private String generateAnalysis(Result rr, Result pr) {
        return Metrics.compare(rr, pr);
    }
}