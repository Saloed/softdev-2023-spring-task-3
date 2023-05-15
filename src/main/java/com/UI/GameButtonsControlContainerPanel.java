package com.UI;

import javax.swing.*;
import java.awt.*;

class GameButtonsControlContainerPanel extends JPanel {

    public GameButtonsControlContainerPanel(LayoutManager layoutManager, JPanel content) {
        super(layoutManager);

        add(content);
    }
}