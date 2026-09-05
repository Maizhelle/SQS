package GUI;

import Model.Match;
import Model.Player;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class QueueMatchCard extends JPanel {

    public QueueMatchCard(
            Match match,
            int matchNumber,
            Runnable onStarted,
            Runnable onSelected) {

        setLayout(new BorderLayout(5, 5));
        setBorder(new LineBorder(Color.GRAY, 1));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(170, 180));
                addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mousePressed(java.awt.event.MouseEvent event) {
                                onSelected.run();
                        }
                });

        JLabel titleLabel =
                new JLabel(String.valueOf(matchNumber));

        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        3, 5, 3, 5
                )
        );

        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();

        centerPanel.setBackground(Color.WHITE);

        centerPanel.setLayout(
                new BoxLayout(
                        centerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        List<Player> players = match.getPlayers();

        int half = players.size() / 2;

        for (int i = 0; i < half; i++) {

            centerPanel.add(
                    playerBox(players.get(i))
            );

            centerPanel.add(
                    Box.createVerticalStrut(4)
            );
        }

        JLabel vsLabel = new JLabel("VS");
        vsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(vsLabel);

        centerPanel.add(
                Box.createVerticalStrut(4)
        );

        for (int i = half; i < players.size(); i++) {

            centerPanel.add(
                    playerBox(players.get(i))
            );

            centerPanel.add(
                    Box.createVerticalStrut(4)
            );
        }

        add(centerPanel, BorderLayout.CENTER);

        JButton startButton =
                new JButton("START MATCH");

        startButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        9
                )
        );

       startButton.addActionListener(e -> {

   boolean started =
        MainGUI.getInstance()
               .assignMatchToCourt(match);

        if (started) {
    onStarted.run();
}
});

        add(startButton, BorderLayout.SOUTH);
    }

    private JPanel playerBox(Player p) {

        JPanel box =
                new JPanel(new BorderLayout());

        box.setMaximumSize(
                new Dimension(140, 24)
        );

        box.setPreferredSize(
                new Dimension(140, 24)
        );

        box.setBorder(
                BorderFactory.createLineBorder(
                        Color.LIGHT_GRAY
                )
        );

        JLabel nameLabel =
                new JLabel(" " + p.getName());

        nameLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        10
                )
        );

        JLabel skillLabel =
                new JLabel(
                        p.getSkillName() + " "
                );

        skillLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        9
                )
        );

        skillLabel.setForeground(
                Color.RED
        );

        box.add(
                nameLabel,
                BorderLayout.WEST
        );

        box.add(
                skillLabel,
                BorderLayout.EAST
        );

        return box;
    }
}