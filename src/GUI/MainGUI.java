package GUI;

import Management.QueueService;
import Management.CourtManager;

import Model.Match;
import Model.Player;
import Model.MatchFormat;
import Model.SkillLevel;

import Collection.OnHoldList;
import Collection.QueueList;
import Model.MatchStatus;
import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import java.util.List;
import javax.swing.JPanel;



public class MainGUI extends javax.swing.JFrame {
private QueueService queueService;
private CourtManager courtManager;
private static MainGUI instance;

private Match court1Match;
private Match court2Match;
private Match court3Match;
private Match court4Match;
private javax.swing.Timer courtRefreshTimer;
private Match selectedQueueMatch;
private final java.util.Set<Player> selectedOnHoldPlayers = new java.util.HashSet<>();
private javax.swing.JLabel court1Timer;
private javax.swing.JLabel court2Timer;
private javax.swing.JLabel court3Timer;
private javax.swing.JLabel court4Timer;
private javax.swing.JButton court1ClearButton;
private javax.swing.JButton court2ClearButton;
private javax.swing.JButton court3ClearButton;
private javax.swing.JButton court4ClearButton;

public MainGUI() {
    instance = this;

    OnHoldList onHoldList = new OnHoldList();
    QueueList queueList = new QueueList();

    Management.MatchMaker matchMaker =
            new Management.MatchMaker();

    courtManager =
            new Management.CourtManager();

    queueService =
            new Management.QueueService(
                    onHoldList,
                    queueList,
                    matchMaker
            );

    initComponents();

    court1Timer = createTimerLabel();
    court2Timer = createTimerLabel();
    court3Timer = createTimerLabel();
    court4Timer = createTimerLabel();

    court1ClearButton = createClearCourtButton(1);
    court2ClearButton = createClearCourtButton(2);
    court3ClearButton = createClearCourtButton(3);
    court4ClearButton = createClearCourtButton(4);

    configureCourtDisplay(jPanel4, jTextField6, court1Timer, lblCourt1Players, jButton13, jButton14, court1ClearButton);
    configureCourtDisplay(jPanel14, jTextField8, court2Timer, lblCourt2Players, jButton17, jButton18, court2ClearButton);
    configureCourtDisplay(jPanel18, jTextField9, court3Timer, lblCourt3Players, jButton19, jButton20, court3ClearButton);
    configureCourtDisplay(jPanel15, jTextField12, court4Timer, lblCourt4Players, jButton25, jButton26, court4ClearButton);

    jButton13.addActionListener(e -> startCourtMatch(1));
    jButton17.addActionListener(e -> startCourtMatch(2));
    jButton19.addActionListener(e -> startCourtMatch(3));
    jButton25.addActionListener(e -> startCourtMatch(4));
    jButton14.addActionListener(e -> endCourtMatch(1));
    jButton18.addActionListener(e -> endCourtMatch(2));
    jButton20.addActionListener(e -> endCourtMatch(3));
    jButton26.addActionListener(e -> endCourtMatch(4));

    pnlPlayerRows.setLayout(
            new BoxLayout(
                    pnlPlayerRows,
                    BoxLayout.Y_AXIS
            )
    );

    pnlQueueRows.setLayout(
            new java.awt.GridLayout(
                    0,
                    3,
                    10,
                    10
            )
    );

            scrollpanePlayers.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollpanePlayers.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPaneOnHold.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPaneOnHold.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPaneQueue.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPaneQueue.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    setResizable(true);
    setMinimumSize(new java.awt.Dimension(900, 600));
    courtRefreshTimer = new javax.swing.Timer(1000, e -> refreshCourtDisplays());
    courtRefreshTimer.start();
}

private void refreshPlayerListLayout() {
    int rowHeight = 50;
    int height = Math.max(30, pnlPlayerRows.getComponentCount() * rowHeight);
    pnlPlayerRows.setPreferredSize(new java.awt.Dimension(340, height));
    pnlPlayerRows.revalidate();
    pnlPlayerRows.repaint();
}
public static MainGUI getInstance() {
return instance;
}

private void configureCourtDisplay(
        javax.swing.JPanel courtPanel,
        javax.swing.JTextField courtLabel,
        javax.swing.JLabel timerLabel,
        javax.swing.JLabel playersLabel,
        javax.swing.JButton startButton,
        javax.swing.JButton endButton,
        javax.swing.JButton clearButton) {
    courtPanel.removeAll();
    courtPanel.setLayout(new java.awt.BorderLayout(8, 8));
    courtLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    courtLabel.setText(courtLabel.getText().replace("00:00", "COURT"));
    courtLabel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 28));
    timerLabel.setText("00:00");
    playersLabel.setText("No Match");
    playersLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    playersLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
    playersLabel.setOpaque(true);
    playersLabel.setBackground(java.awt.Color.WHITE);
    startButton.setBackground(new java.awt.Color(0, 204, 0));
    endButton.setBackground(new java.awt.Color(204, 0, 0));

    javax.swing.JPanel header = new javax.swing.JPanel();
    header.setLayout(new javax.swing.BoxLayout(header, javax.swing.BoxLayout.Y_AXIS));
    header.add(courtLabel);
    header.add(timerLabel);

    javax.swing.JPanel actions = new javax.swing.JPanel(new java.awt.GridLayout(1, 3, 6, 0));
    actions.add(startButton);
    actions.add(endButton);
    actions.add(clearButton);

    courtPanel.add(header, java.awt.BorderLayout.NORTH);
    courtPanel.add(playersLabel, java.awt.BorderLayout.CENTER);
    courtPanel.add(actions, java.awt.BorderLayout.SOUTH);
    courtPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createEtchedBorder(),
            javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)));
}

private javax.swing.JButton createClearCourtButton(int courtNumber) {
    javax.swing.JButton clearButton = new javax.swing.JButton("CLEAR COURT");
    clearButton.setForeground(new java.awt.Color(180, 0, 0));
    clearButton.addActionListener(e -> clearCourtMatch(courtNumber));
    return clearButton;
}

private javax.swing.JLabel createTimerLabel() {
    javax.swing.JLabel timerLabel = new javax.swing.JLabel("00:00");
    timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    timerLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
    return timerLabel;
}

public boolean assignMatchToCourt(Match match) {
    if (match == null) {
        return false;
    }

    Model.Court availableCourt = courtManager.getAvailableCourt();
    if (availableCourt == null) {
        JOptionPane.showMessageDialog(this, "No available court right now.");
        return false;
    }

    int courtNumber = availableCourt.getCourtNum();
    courtManager.assignMatchToCourt(match, courtNumber);
    displayMatchOnCourt(courtNumber, match);
    updateQueueDisplay();
    return true;
}

private void displayMatchOnCourt(int courtNumber, Match match) {
    StringBuilder display = new StringBuilder("<html><center>");
    int half = match.getPlayers().size() / 2;
    for (int index = 0; index < match.getPlayers().size(); index++) {
        if (index == half) {
            display.append("<br><b>VS</b><br><br>");
        }
        display.append(match.getPlayers().get(index).getName()).append("<br>");
    }
    display.append("</center></html>");

    switch (courtNumber) {
        case 1 -> { court1Match = match; lblCourt1Players.setText(display.toString()); }
        case 2 -> { court2Match = match; lblCourt2Players.setText(display.toString()); }
        case 3 -> { court3Match = match; lblCourt3Players.setText(display.toString()); }
        case 4 -> { court4Match = match; lblCourt4Players.setText(display.toString()); }
        default -> throw new IllegalArgumentException("Invalid court number: " + courtNumber);
    }
}

private void startCourtMatch(int courtNumber) {
    Match match = getCourtMatch(courtNumber);
    if (match != null && match.getStatus() == MatchStatus.PENDING) {
        courtManager.startMatch(courtNumber, 15);
        refreshCourtDisplays();
    }
}

private void endCourtMatch(int courtNumber) {
    Match match = getCourtMatch(courtNumber);
    if (match == null) {
        return;
    }
    courtManager.pauseMatch(courtNumber);
    refreshCourtDisplays();
}

private void clearCourtMatch(int courtNumber) {
    Match match = getCourtMatch(courtNumber);
    if (match == null) {
        return;
    }
    courtManager.endMatch(courtNumber);
    queueService.completeMatch(match);
    clearCourt(courtNumber);
    updateOnHoldDisplay();
    updateQueueDisplay();
}

private Match getCourtMatch(int courtNumber) {
    return switch (courtNumber) {
        case 1 -> court1Match;
        case 2 -> court2Match;
        case 3 -> court3Match;
        case 4 -> court4Match;
        default -> throw new IllegalArgumentException("Invalid court number: " + courtNumber);
    };
}

private void clearCourt(int courtNumber) {
    switch (courtNumber) {
        case 1 -> { court1Match = null; lblCourt1Players.setText("No Match"); court1Timer.setText("00:00"); }
        case 2 -> { court2Match = null; lblCourt2Players.setText("No Match"); court2Timer.setText("00:00"); }
        case 3 -> { court3Match = null; lblCourt3Players.setText("No Match"); court3Timer.setText("00:00"); }
        case 4 -> { court4Match = null; lblCourt4Players.setText("No Match"); court4Timer.setText("00:00"); }
        default -> throw new IllegalArgumentException("Invalid court number: " + courtNumber);
    }
}

private void refreshCourtDisplays() {
    for (int courtNumber = 1; courtNumber <= 4; courtNumber++) {
        Match match = getCourtMatch(courtNumber);
        if (match == null) {
            continue;
        }
        if (match.getStatus() == MatchStatus.FINISHED) {
            queueService.completeMatch(match);
            clearCourt(courtNumber);
        } else {
            String remaining = match.getRemainingTime();
            switch (courtNumber) {
                case 1 -> court1Timer.setText(remaining);
                case 2 -> court2Timer.setText(remaining);
                case 3 -> court3Timer.setText(remaining);
                case 4 -> court4Timer.setText(remaining);
                default -> throw new IllegalArgumentException("Invalid court number: " + courtNumber);
            }
        }
    }
    updateOnHoldDisplay();
    updateQueueDisplay();
}

    @SuppressWarnings("unchecked")
    private void initComponents() {

        ButtonAdd = new javax.swing.JButton();
        scrollpanePlayers = new javax.swing.JScrollPane();
        pnlPlayerRows = new javax.swing.JPanel();
        scrollPaneOnHold = new javax.swing.JScrollPane();
        pnlOnHoldRows = new javax.swing.JPanel();
        scrollPaneQueue = new javax.swing.JScrollPane();
        pnlQueueRows = new javax.swing.JPanel();
        TransferAllToQueue = new javax.swing.JButton();
        ClearAllOnHold = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();
        lblCourt1Players = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        lblCourt2Players = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jTextField9 = new javax.swing.JTextField();
        lblCourt3Players = new javax.swing.JLabel();
        TransferAllToOnHold = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jTextField12 = new javax.swing.JTextField();
        lblCourt4Players = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout(8, 8));

        ButtonAdd.setText("+");
        ButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonAddActionPerformed(evt);
            }
        });

        scrollpanePlayers.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5), "Players", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI Historic", 1, 24)));

        pnlPlayerRows.setPreferredSize(new java.awt.Dimension(500, 30));
        pnlPlayerRows.setLayout(new javax.swing.BoxLayout(pnlPlayerRows, javax.swing.BoxLayout.Y_AXIS));
        scrollpanePlayers.setViewportView(pnlPlayerRows);


        scrollPaneOnHold.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(null, "On Hold", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI Variable", 1, 24)));

        pnlOnHoldRows.setLayout(new javax.swing.BoxLayout(pnlOnHoldRows, javax.swing.BoxLayout.Y_AXIS));
        scrollPaneOnHold.setViewportView(pnlOnHoldRows);


        scrollPaneQueue.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(null, "Queue", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Symbol", 1, 24)));

        pnlQueueRows.setLayout(new javax.swing.BoxLayout(pnlQueueRows, javax.swing.BoxLayout.Y_AXIS));
        scrollPaneQueue.setViewportView(pnlQueueRows);

        QueueTotalLabel = new javax.swing.JLabel("TOTAL IN QUEUE: 0");
        QueueTotalLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        ClearQueueButton = new javax.swing.JButton("CLEAR QUEUE");
        ClearQueueButton.setForeground(new java.awt.Color(180, 0, 0));
        ClearQueueButton.addActionListener(e -> clearQueue());

        AddToQueueButton = new javax.swing.JButton("ADD TO QUEUE");
        AddToQueueButton.addActionListener(e -> addSelectedPlayersToQueue());
        RemoveFromQueueButton = new javax.swing.JButton("REMOVE FROM QUEUE");
        RemoveFromQueueButton.setForeground(new java.awt.Color(180, 0, 0));
        RemoveFromQueueButton.addActionListener(e -> removeSelectedQueueMatch());


        TransferAllToQueue.setText("TRANSFER ALL TO QUEUE");
        TransferAllToQueue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TransferAllToQueueActionPerformed(evt);
            }
        });

        ClearAllOnHold.setText("CLEAR ALL");
        ClearAllOnHold.setForeground(new java.awt.Color(180, 0, 0));
        ClearAllOnHold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearAllOnHoldActionPerformed(evt);
            }
        });

        jPanel13.setBackground(new java.awt.Color(227, 227, 227));
        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 204), null));

        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton13.setBackground(new java.awt.Color(0, 204, 0));
        jButton13.setText("START MATCH");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(204, 0, 0));
        jButton14.setText("END MATCH");

        jTextField6.setEditable(false);
        jTextField6.setBackground(new java.awt.Color(218, 218, 218));
        jTextField6.setText("COURT 1");

        lblCourt1Players.setText("jLabel1");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jButton13)
                        .addGap(18, 18, 18)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(lblCourt1Players)))
                .addGap(35, 35, 35))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(lblCourt1Players)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13)
                    .addComponent(jButton14))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );


        jPanel16.setBackground(new java.awt.Color(227, 227, 227));
        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 204), null));

        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton17.setBackground(new java.awt.Color(0, 204, 0));
        jButton17.setText("START MATCH");

        jButton18.setBackground(new java.awt.Color(204, 0, 0));
        jButton18.setText("END MATCH");

        jTextField8.setEditable(false);
        jTextField8.setBackground(new java.awt.Color(218, 218, 218));
        jTextField8.setText("COURT 2");
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        lblCourt2Players.setText("No Match");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jButton17)
                        .addGap(18, 18, 18)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblCourt2Players)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(lblCourt2Players)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton17)
                    .addComponent(jButton18))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );


        jPanel19.setBackground(new java.awt.Color(227, 227, 227));
        jPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 204), null));

        jPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton19.setBackground(new java.awt.Color(0, 204, 0));
        jButton19.setText("START MATCH");

        jButton20.setBackground(new java.awt.Color(204, 0, 0));
        jButton20.setText("END MATCH");

        jTextField9.setEditable(false);
        jTextField9.setBackground(new java.awt.Color(218, 218, 218));
        jTextField9.setText("COURT 3");
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });

        lblCourt3Players.setText("No Match");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jButton19)
                        .addGap(18, 18, 18)
                        .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblCourt3Players)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(lblCourt3Players)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton19)
                    .addComponent(jButton20))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(572, Short.MAX_VALUE))
        );


        TransferAllToOnHold.setText("TRANSFER ALL TO ON HOLD");
        TransferAllToOnHold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TransferAllToOnHoldActionPerformed(evt);
            }
        });

        jPanel29.setBackground(new java.awt.Color(227, 227, 227));
        jPanel29.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(204, 204, 204), null));

        jPanel30.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton25.setBackground(new java.awt.Color(0, 204, 0));
        jButton25.setText("START MATCH");

        jButton26.setBackground(new java.awt.Color(204, 0, 0));
        jButton26.setText("END MATCH");

        jTextField12.setEditable(false);
        jTextField12.setBackground(new java.awt.Color(218, 218, 218));
        jTextField12.setText("COURT 4");

        lblCourt4Players.setText("No Match");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jButton25)
                        .addGap(18, 18, 18)
                        .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblCourt4Players)
                            .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(lblCourt4Players)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton25)
                    .addComponent(jButton26))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        JPanel playersColumn = new JPanel(new BorderLayout(4, 4));
        JPanel playerActions = new JPanel(new java.awt.GridLayout(1, 2, 4, 4));
        playerActions.add(ButtonAdd);
        playerActions.add(TransferAllToOnHold);
        playersColumn.add(scrollpanePlayers, BorderLayout.CENTER);
        playersColumn.add(playerActions, BorderLayout.SOUTH);

        JPanel onHoldColumn = new JPanel(new BorderLayout(4, 4));
        onHoldColumn.add(scrollPaneOnHold, BorderLayout.CENTER);
        JPanel onHoldActions = new JPanel(new java.awt.GridLayout(1, 2, 4, 4));
        onHoldActions.add(TransferAllToQueue);
        onHoldActions.add(ClearAllOnHold);
        onHoldColumn.add(onHoldActions, BorderLayout.SOUTH);

        JPanel queueColumn = new JPanel(new BorderLayout(4, 4));
        JPanel queueHeader = new JPanel(new BorderLayout());
        queueHeader.add(QueueTotalLabel, BorderLayout.WEST);
        queueHeader.add(ClearQueueButton, BorderLayout.EAST);
        JPanel queueActions = new JPanel(new java.awt.GridLayout(1, 2, 4, 4));
        queueActions.add(AddToQueueButton);
        queueActions.add(RemoveFromQueueButton);
        queueColumn.add(queueHeader, BorderLayout.NORTH);
        queueColumn.add(scrollPaneQueue, BorderLayout.CENTER);
        queueColumn.add(queueActions, BorderLayout.SOUTH);

        JPanel dashboard = new JPanel(new java.awt.GridLayout(1, 3, 8, 8));
        dashboard.add(playersColumn);
        dashboard.add(onHoldColumn);
        dashboard.add(queueColumn);
        dashboard.setPreferredSize(new java.awt.Dimension(1100, 320));

        java.awt.Dimension courtSize = new java.awt.Dimension(270, 320);
        jPanel4.setPreferredSize(courtSize);
        jPanel14.setPreferredSize(courtSize);
        jPanel18.setPreferredSize(courtSize);
        jPanel15.setPreferredSize(courtSize);
        JPanel courts = new JPanel(new java.awt.GridLayout(1, 4, 8, 8));
        courts.add(jPanel4);
        courts.add(jPanel14);
        courts.add(jPanel18);
        courts.add(jPanel15);
        courts.setPreferredSize(new java.awt.Dimension(1120, 320));

        javax.swing.JScrollPane courtsScroll = new javax.swing.JScrollPane(courts);
        courtsScroll.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        courtsScroll.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane(
            javax.swing.JSplitPane.VERTICAL_SPLIT,
            dashboard,
            courtsScroll
        );
        splitPane.setResizeWeight(0.48);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        setSize(1200, 780);
        setLocationRelativeTo(null);
    }

    private void ButtonAddActionPerformed(java.awt.event.ActionEvent evt) {
PlayerRowPanel newRow = new PlayerRowPanel();
pnlPlayerRows.add(newRow);
refreshPlayerListLayout();
    }

    private void TransferAllToOnHoldActionPerformed(java.awt.event.ActionEvent evt) {
if (pnlPlayerRows.getComponentCount() == 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "No players to transfer.", "Notice", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    for (java.awt.Component comp : pnlPlayerRows.getComponents()) {
        if (comp instanceof PlayerRowPanel row) {

            String name = row.getPlayerName();
            SkillLevel skill = row.getSelectedSkill();
            MatchFormat format = row.getSelectedFormat();


            if (name.isEmpty()) {
                continue;
            }

            queueService.addPlayer(name, format, skill);
        }
    }
updateOnHoldDisplay();
    pnlPlayerRows.removeAll();
    refreshPlayerListLayout();
    
    }

    private void TransferAllToQueueActionPerformed(java.awt.event.ActionEvent evt) {
   List<Player> onHoldPlayers =
        queueService.getOnHoldList().getAllPlayers();


    if (onHoldPlayers.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "No players on hold to transfer.", "Notice", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        return;
    }

for (Player p : onHoldPlayers) {
    queueService.movePlayerToQueue(p);
}

    queueService.getOnHoldList().clearAll();
    selectedOnHoldPlayers.clear();

    updateOnHoldDisplay();
    updateQueueDisplay();
    }

    private void ClearAllOnHoldActionPerformed(java.awt.event.ActionEvent evt) {
        for (Player player : queueService.getOnHoldList().getAllPlayers()) {
            pnlPlayerRows.add(new PlayerRowPanel(player));
        }
        queueService.getOnHoldList().clearAll();
        selectedOnHoldPlayers.clear();
        refreshPlayerListLayout();
        updateOnHoldDisplay();
    }

    private void addSelectedPlayersToQueue() {
        if (selectedOnHoldPlayers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select at least one player first.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Player player : new java.util.ArrayList<>(selectedOnHoldPlayers)) {
            if (queueService.getOnHoldList().contains(player)) {
                queueService.movePlayerToQueue(player);
            }
        }
        selectedOnHoldPlayers.clear();
        updateOnHoldDisplay();
        updateQueueDisplay();
    }

    private void clearQueue() {
        queueService.clearQueue();
        selectedQueueMatch = null;
        updateQueueDisplay();
    }

    private void removeSelectedQueueMatch() {
        if (selectedQueueMatch == null) {
            return;
        }
        queueService.removeMatch(selectedQueueMatch);
        selectedQueueMatch = null;
        updateQueueDisplay();
    }

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {
    }
                                            
public void updateOnHoldDisplay() {
    pnlOnHoldRows.removeAll();
    pnlOnHoldRows.setLayout(new java.awt.GridLayout(0, 2, 8, 8));

    int count = 1;
    for (Model.Player p : queueService.getOnHoldList().getAllPlayers()) {
        int currentIndex = count++;
        OnHoldPlayerCard card = new OnHoldPlayerCard(p, currentIndex, () -> {
            queueService.getOnHoldList().remove(p);
            selectedOnHoldPlayers.remove(p);
            pnlPlayerRows.add(new PlayerRowPanel(p));
            refreshPlayerListLayout();
            updateOnHoldDisplay();
        }, selected -> {
            if (selected) {
                selectedOnHoldPlayers.add(p);
            } else {
                selectedOnHoldPlayers.remove(p);
            }
        }, selectedOnHoldPlayers.contains(p));
        pnlOnHoldRows.add(card);
    }

    pnlOnHoldRows.revalidate();
    pnlOnHoldRows.repaint();
}
     
public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainGUI().setVisible(true);
            }
        });
    }

    public void updateQueueDisplay() {

    pnlQueueRows.removeAll();
    int total = 0;

    int count = 1;

    for (Match m : queueService.getAllPendingMatches()) {

        if (m.getStatus() == MatchStatus.RUNNING || m.getCourt() != null) {
            continue;
        }

        total++;

        QueueMatchCard card =
                new QueueMatchCard(
                        m,
                        count++,
                        this::updateQueueDisplay,
                        () -> {
                            selectedQueueMatch = m;
                            updateQueueDisplay();
                        }
                );

        pnlQueueRows.add(card);
    }

    pnlQueueRows.revalidate();
    pnlQueueRows.repaint();
    QueueTotalLabel.setText("TOTAL IN QUEUE: " + total);
}

    
    
    private javax.swing.JButton ButtonAdd;
    private javax.swing.JButton ClearAllOnHold;
    private javax.swing.JButton ClearQueueButton;
    private javax.swing.JButton AddToQueueButton;
    private javax.swing.JButton RemoveFromQueueButton;
    private javax.swing.JLabel QueueTotalLabel;
    private javax.swing.JButton TransferAllToOnHold;
    private javax.swing.JButton TransferAllToQueue;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JLabel lblCourt1Players;
    private javax.swing.JLabel lblCourt2Players;
    private javax.swing.JLabel lblCourt3Players;
    private javax.swing.JLabel lblCourt4Players;
    private javax.swing.JPanel pnlOnHoldRows;
    private javax.swing.JPanel pnlPlayerRows;
    private javax.swing.JPanel pnlQueueRows;
    private javax.swing.JScrollPane scrollPaneOnHold;
    private javax.swing.JScrollPane scrollPaneQueue;
    private javax.swing.JScrollPane scrollpanePlayers;

}
