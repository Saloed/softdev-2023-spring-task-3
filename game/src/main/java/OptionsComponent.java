import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class OptionsComponent extends JComponent implements ActionListener{

    private final JButton btnPlay = new JButton("PLAY");
    private final JButton btnExit = new JButton("EXIT");

    public OptionsComponent() {
        btnPlay.addActionListener(this);
        btnExit.addActionListener(this);

    }

    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        BufferedImage backgroundImage;
        try {
            backgroundImage = ImageIO.read(new File("images/background.png"));
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }

        g2.drawImage(backgroundImage, 0, 0, null);

        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 100));
        g2.setColor(Color.WHITE);
        g2.drawString("Welcome", 380, 100);
        g2.drawString("to", 530, 180);
        g2.drawString("BLACKJACK!", 290, 260);

        g2.setFont(new Font("Arial", Font.BOLD, 30));

        btnPlay.setBounds(500, 300, 150, 80);
        btnExit.setBounds(500, 400, 150, 80);

        btnPlay.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
        btnExit.setFont(new Font("Comic Sans MS", Font.BOLD, 40));

        super.add(btnPlay);
        super.add(btnExit);
    }
    public void actionPerformed(ActionEvent e) {
        JButton selectedButton = (JButton)e.getSource();

        if(selectedButton == btnExit) {
            System.exit(0);
        }
        else if(selectedButton == btnPlay) {
            Tester tester = new Tester();
            tester.btnPlay();
        }
    }

}