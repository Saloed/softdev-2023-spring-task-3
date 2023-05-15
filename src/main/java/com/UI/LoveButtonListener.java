package com.UI;

import javax.swing.*;

public class LoveButtonListener implements GameButtonsControlPanel.LoveButtonListener {

    LoveButtonListener() {
    }

    @Override
    public void onLoveButtonClick() {
        JOptionPane.showMessageDialog(null, "Стараемся )", "Благодарю!", JOptionPane.INFORMATION_MESSAGE);
    }
}
