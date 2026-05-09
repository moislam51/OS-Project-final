package views;
import utils.InputValidator;

import models.Process;

public class InputPanel extends javax.swing.JFrame {
    private java.util.List<models.Process> processList = new java.util.ArrayList<>();
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(InputPanel.class.getName());

    
    public InputPanel() {
        initComponents();
  add.addActionListener(e3 -> {
    try {
        String id = processID.getText();
        
        for (Process p : processList) {
            if (p.getId().equalsIgnoreCase(id)) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Process ID '" + id + "' already exists! Please use a unique ID.");
                return; // Stop the action here
            }
        }
        int at = Integer.parseInt(arrivalTime.getText());
        int bt = Integer.parseInt(burstTime.getText());
        int pr = Integer.parseInt(priority.getText());
        int q = Integer.parseInt(quantum.getText());
        
        if (InputValidator.isValid(id, at, bt, pr, q)) {
            
            //Create and add the process to our list
            Process p = new Process(id, at, bt, pr);
            processList.add(p);
            quantum.setEditable(false); 
            quantum.setEnabled(false);
            
            processCounterLabel.setText("Processes Added: " + processList.size());
            
            // 3. Clear inputs for the next entry (keeping quantum as it's usually the same)
            processID.setText("");
            arrivalTime.setText("");
            burstTime.setText("");
            priority.setText("");
            
            System.out.println("Process " + id + " added. Total: " + processList.size());
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Invalid input values or not filled!!");
        }
    } catch (NumberFormatException ex) {
        javax.swing.JOptionPane.showMessageDialog(this, "Invalid input values or not filled!!");
    }
});
    reset.addActionListener(e -> {
        // Clear the List
        processList.clear();
        
        quantum.setEditable(true);
        quantum.setEnabled(true);
        
        processID.setText("");
        arrivalTime.setText("");
        burstTime.setText("");
        priority.setText("");
        quantum.setText("");

        
        processCounterLabel.setText("Processes Added: 0");

        System.out.println("All processes cleared.");
});

run.addActionListener(e -> {
    if (processList.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Please add at least one process before running.");
        return;
    }

    int q;
    try {
        q = Integer.parseInt(quantum.getText());
    } catch (NumberFormatException ex) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Please enter a valid quantum value.");
        return;
    }

    controllers.SchedulerController schedulerController = new controllers.SchedulerController();
    models.Result[] results = schedulerController.runScheduling(processList, q);

    views.ComparisonPanel compPanel = new views.ComparisonPanel();
    compPanel.displayResults(results[1], results[0]);

    views.ResultTablePanel resultTable = new views.ResultTablePanel();
    resultTable.updateRR(results[1]);
    resultTable.updatePriority(results[0]);

    views.GanttPanel ganttPanelRR = new views.GanttPanel();
    views.GanttPanel ganttPanelPR = new views.GanttPanel();

    java.util.List<String> rrIds = new java.util.ArrayList<>();
    java.util.List<Integer> rrStarts = new java.util.ArrayList<>();
    java.util.List<Integer> rrEnds = new java.util.ArrayList<>();
    for (String entry : results[1].getGanttChart()) {
        String[] parts = entry.split(" \\[");
        String id = parts[0];
        String[] times = parts[1].replace("]", "").split(" - ");
        rrIds.add(id);
        rrStarts.add(Integer.parseInt(times[0].trim()));
        rrEnds.add(Integer.parseInt(times[1].trim()));
    }
    ganttPanelRR.setChartData(rrIds, rrStarts, rrEnds);

    java.util.List<String> prIds = new java.util.ArrayList<>();
    java.util.List<Integer> prStarts = new java.util.ArrayList<>();
    java.util.List<Integer> prEnds = new java.util.ArrayList<>();
    for (String entry : results[0].getGanttChart()) {
        String[] parts = entry.split(" \\[");
        String id = parts[0];
        String[] times = parts[1].replace("]", "").split(" - ");
        prIds.add(id);
        prStarts.add(Integer.parseInt(times[0].trim()));
        prEnds.add(Integer.parseInt(times[1].trim()));
    }
    ganttPanelPR.setChartData(prIds, prStarts, prEnds);

    javax.swing.JFrame resultsFrame = new javax.swing.JFrame("Scheduling Results");
    resultsFrame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

    javax.swing.JTabbedPane tabs = new javax.swing.JTabbedPane();

    tabs.addTab("Comparison", compPanel);
    tabs.addTab("Result Table", resultTable);

   
    javax.swing.JPanel ganttTab = new javax.swing.JPanel(new java.awt.GridLayout(2, 1, 0, 10));

    javax.swing.JPanel rrSection = new javax.swing.JPanel(new java.awt.BorderLayout());
    javax.swing.JLabel rrTitle = new javax.swing.JLabel("Round Robin Gantt Chart", javax.swing.SwingConstants.CENTER);
    rrTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
    rrSection.add(rrTitle, java.awt.BorderLayout.NORTH);
    javax.swing.JScrollPane rrScroll = new javax.swing.JScrollPane(ganttPanelRR);
    rrScroll.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    rrSection.add(rrScroll, java.awt.BorderLayout.CENTER);

    javax.swing.JPanel prSection = new javax.swing.JPanel(new java.awt.BorderLayout());
    javax.swing.JLabel prTitle = new javax.swing.JLabel("Priority Gantt Chart", javax.swing.SwingConstants.CENTER);
    prTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
    prSection.add(prTitle, java.awt.BorderLayout.NORTH);
    javax.swing.JScrollPane prScroll = new javax.swing.JScrollPane(ganttPanelPR);
    prScroll.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    prSection.add(prScroll, java.awt.BorderLayout.CENTER);

    ganttTab.add(rrSection);
    ganttTab.add(prSection);

    tabs.addTab("Gantt Charts", ganttTab);
   

    resultsFrame.add(tabs);
    resultsFrame.setSize(800, 500);
    resultsFrame.setLocationRelativeTo(this);
    resultsFrame.setVisible(true);
});
                }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        burstTime = new javax.swing.JTextField();
        priority = new javax.swing.JTextField();
        reset = new javax.swing.JButton();
        quantum = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        processID = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        arrivalTime = new javax.swing.JTextField();
        add = new javax.swing.JButton();
        processCounterLabel = new javax.swing.JLabel();
        run = new javax.swing.JButton();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        burstTime.setBackground(new java.awt.Color(153, 153, 153));
        burstTime.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        burstTime.setForeground(new java.awt.Color(0, 0, 0));

        priority.setBackground(new java.awt.Color(153, 153, 153));
        priority.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        priority.setForeground(new java.awt.Color(0, 0, 0));
        priority.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priorityActionPerformed(evt);
            }
        });

        reset.setBackground(new java.awt.Color(255, 0, 0));
        reset.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        reset.setForeground(new java.awt.Color(0, 0, 0));
        reset.setText("Reset");
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });

        quantum.setBackground(new java.awt.Color(153, 153, 153));
        quantum.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        quantum.setForeground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Process ID");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Arrival time");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Burst time");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Priority");

        processID.setBackground(new java.awt.Color(153, 153, 153));
        processID.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        processID.setForeground(new java.awt.Color(0, 0, 0));
        processID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processIDActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Quantum");

        arrivalTime.setBackground(new java.awt.Color(153, 153, 153));
        arrivalTime.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        arrivalTime.setForeground(new java.awt.Color(0, 0, 0));
        arrivalTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrivalTimeActionPerformed(evt);
            }
        });

        add.setBackground(new java.awt.Color(102, 255, 51));
        add.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        add.setForeground(new java.awt.Color(0, 0, 0));
        add.setText("Add");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        run.setBackground(new java.awt.Color(51, 102, 255));
        run.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        run.setForeground(new java.awt.Color(0, 0, 0));
        run.setText("Run");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(processCounterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel2))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(15, 15, 15)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addComponent(add)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reset)
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(processID, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(arrivalTime)
                            .addComponent(burstTime)
                            .addComponent(priority)
                            .addComponent(quantum))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(run)
                        .addGap(69, 69, 69))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(processID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(arrivalTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(burstTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(priority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(quantum)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(processCounterLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(add)
                        .addComponent(reset)))
                .addGap(18, 18, 18)
                .addComponent(run)
                .addGap(52, 52, 52))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void processIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_processIDActionPerformed

    private void arrivalTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrivalTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_arrivalTimeActionPerformed

    private void priorityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priorityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priorityActionPerformed

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resetActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        
    }//GEN-LAST:event_addActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add;
    private javax.swing.JTextField arrivalTime;
    private javax.swing.JTextField burstTime;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField priority;
    private javax.swing.JLabel processCounterLabel;
    private javax.swing.JTextField processID;
    private javax.swing.JTextField quantum;
    private javax.swing.JButton reset;
    private javax.swing.JButton run;
    // End of variables declaration//GEN-END:variables
}
