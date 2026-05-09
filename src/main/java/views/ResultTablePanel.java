package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import models.Process;
import models.Result;

public class ResultTablePanel extends JPanel {

    private JTable rrTable;
    private DefaultTableModel rrModel;
    private JLabel rrAvgWT, rrAvgTAT, rrAvgRT;

    private JTable prTable;
    private DefaultTableModel prModel;
    private JLabel prAvgWT, prAvgTAT, prAvgRT;

    private Result rrResult;
    private Result prResult;

    public ResultTablePanel() {
        setLayout(new GridLayout(2, 1, 0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        add(createSection("Round Robin", true));
        add(createSection("Priority Scheduling", false));
    }

    private JPanel createSection(String title, boolean isRR) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            title,
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));

        // Table
        String[] columns = {"Process", "Arrival Time", "Burst Time", "Waiting Time", "Turnaround Time", "Response Time"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(173, 216, 230));

        // Center all columns
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columns.length; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);

        if (isRR) { rrModel = model; rrTable = table; }
        else       { prModel = model; prTable = table; }

        // Averages row at the bottom
        JPanel avgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        avgPanel.setBackground(new Color(230, 230, 230));
        avgPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

        JLabel avgWT  = new JLabel("Avg Waiting Time: -");
        JLabel avgTAT = new JLabel("Avg Turnaround Time: -");
        JLabel avgRT  = new JLabel("Avg Response Time: -");

        Font avgFont = new Font("Arial", Font.BOLD, 13);
        avgWT.setFont(avgFont);
        avgTAT.setFont(avgFont);
        avgRT.setFont(avgFont);

        avgPanel.add(avgWT);
        avgPanel.add(avgTAT);
        avgPanel.add(avgRT);

        if (isRR) { rrAvgWT = avgWT; rrAvgTAT = avgTAT; rrAvgRT = avgRT; }
        else      { prAvgWT = avgWT; prAvgTAT = avgTAT; prAvgRT = avgRT; }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(avgPanel, BorderLayout.SOUTH);
        return panel;
    }

    public void updateRR(Result result) {
        this.rrResult = result;
        fillTable(rrModel, result);
        updateAverages(rrAvgWT, rrAvgTAT, rrAvgRT, result);
    }

    public void updatePriority(Result result) {
        this.prResult = result;
        fillTable(prModel, result);
        updateAverages(prAvgWT, prAvgTAT, prAvgRT, result);
    }

    private void fillTable(DefaultTableModel model, Result result) {
        model.setRowCount(0);
        List<models.Process> processes = result.processes;
        for (models.Process p : processes) {
            model.addRow(new Object[]{
                p.getId(),
                p.getArrivalTime(),
                p.getBurstTime(),
                String.format("%.2f", p.getWaitingTime()),
                String.format("%.2f", p.getTurnaroundTime()),
                String.format("%.2f", p.getResponseTime())
            });
        }
    }

    private void updateAverages(JLabel wt, JLabel tat, JLabel rt, Result r) {
        wt.setText("Avg Waiting Time: "     + String.format("%.2f", r.getAvgWT()));
        tat.setText("Avg Turnaround Time: " + String.format("%.2f", r.getAvgTAT()));
        rt.setText("Avg Response Time: "    + String.format("%.2f", r.getAvgRT()));
    }
}