package task3;

import javax.swing.*;

public class InputPanel extends JPanel {

    private final JTextField textField = new JTextField(5);

    void setValue(int value) {
        textField.setText(String.valueOf(value));
    }

    int value() {
        try {
            return Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    InputPanel(String label) {
        add(new JLabel(label));
        add(textField);
    }
}
