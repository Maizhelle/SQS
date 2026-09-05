package GUI;

import Model.Player;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.function.Consumer;

public class OnHoldPlayerCard extends JPanel {

        public OnHoldPlayerCard(
            Player player,
            int index,
            Runnable onRemove,
            Consumer<Boolean> onSelectionChanged,
            boolean selected) {
        setLayout(new BorderLayout(4, 2));
        setBorder(new LineBorder(Color.GRAY, 1));
        setPreferredSize(new Dimension(170, 76));
        setMinimumSize(new Dimension(150, 76));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 76));

        JLabel numberLabel = new JLabel(String.valueOf(index));
        numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numberLabel.setPreferredSize(new Dimension(28, 28));
        numberLabel.setBorder(new javax.swing.border.EtchedBorder());

        JLabel nameLabel = new JLabel(player.getName());

        JButton removeButton = new JButton("X");
        removeButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        removeButton.setForeground(Color.RED);
        removeButton.setFont(removeButton.getFont().deriveFont(Font.BOLD, 16f));
        removeButton.setPreferredSize(new Dimension(34, 26));
        removeButton.setMinimumSize(new Dimension(34, 26));
        removeButton.setMaximumSize(new Dimension(34, 26));
        removeButton.setToolTipText("Return player to player list");
        removeButton.setBorderPainted(false);
        removeButton.setContentAreaFilled(false);
        removeButton.addActionListener(e -> onRemove.run());

        JPanel header = new JPanel(new BorderLayout(4, 0));
        header.add(nameLabel, BorderLayout.CENTER);
        header.add(removeButton, BorderLayout.EAST);

        JButton statusButton = new JButton("ON HOLD");
        statusButton.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        statusButton.setPreferredSize(new Dimension(78, 24));
        statusButton.setMinimumSize(new Dimension(78, 24));
        statusButton.setMaximumSize(new Dimension(78, 24));
        statusButton.setSelected(selected);
        updateSelectionButton(statusButton);
        statusButton.addActionListener(e -> {
            statusButton.setSelected(!statusButton.isSelected());
            updateSelectionButton(statusButton);
            onSelectionChanged.accept(statusButton.isSelected());
        });

        JButton formatButton = new JButton(player.getFormat() == Model.MatchFormat.SINGLE ? "S" : "D");
        formatButton.setEnabled(false);
        formatButton.setPreferredSize(new Dimension(34, 24));

        JLabel skillIndicator = new JLabel();
        skillIndicator.setOpaque(true);
        skillIndicator.setPreferredSize(new Dimension(26, 24));
        skillIndicator.setBackground(skillColor(player));
        skillIndicator.setToolTipText(player.getSkillName());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        controls.add(statusButton);
        controls.add(formatButton);
        controls.add(skillIndicator);

        add(numberLabel, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
        add(controls, BorderLayout.CENTER);
    }

    private void updateSelectionButton(JButton statusButton) {
        statusButton.setText(statusButton.isSelected() ? "SELECTED" : "ON HOLD");
        statusButton.setForeground(statusButton.isSelected() ? new Color(0, 120, 0) : new Color(70, 140, 230));
    }

    private Color skillColor(Player player) {
        switch (player.getSkillRank()) {
            case 1 -> { return new Color(95, 235, 95); }
            case 2 -> { return new Color(80, 190, 255); }
            case 3 -> { return new Color(255, 215, 70); }
            case 4 -> { return new Color(255, 170, 70); }
            case 5 -> { return new Color(255, 120, 120); }
            case 6 -> { return new Color(190, 130, 255); }
            default -> { return new Color(120, 120, 120); }
        }
    }
    
}
