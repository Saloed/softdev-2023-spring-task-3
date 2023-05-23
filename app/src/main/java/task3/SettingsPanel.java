package task3;

import javax.swing.*;

public class SettingsPanel extends JPanel {

    final InputPanel rows = new InputPanel("Rows");
    final InputPanel columns = new InputPanel("Columns");
    final InputPanel bombs = new InputPanel("Bombs");

    SettingsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        InputPanel[] items = { rows, columns, bombs };

        for (InputPanel item : items) {
            add(item);
        }
    }
}
