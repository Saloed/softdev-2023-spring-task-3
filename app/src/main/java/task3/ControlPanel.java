package task3;

import javax.swing.*;
import java.awt.*;

abstract class ControlPanel extends JPanel {

    ControlPanel() {
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        var layout = new GridLayout(1, 0);
        layout.setHgap(4);
        setLayout(layout);

        var restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restartGame());
        add(restartButton);

        var settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> editSettings());
        add(settingsButton);
    }

    abstract void restartGame();

    abstract void editSettings();
}
