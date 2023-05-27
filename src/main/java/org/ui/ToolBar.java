package org.ui;

import org.helpers.LoadSave;
import org.objects.Tile;
import org.scenes.Editing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.main.GameStates.MENU;
import static org.main.GameStates.SetGameState;
import static org.main.LangTexts.*;

public class ToolBar extends Bar {

    private MyButton bMenu, bSave;
    private MyButton bPathStart, bPathEnd;
    private BufferedImage pathStart, pathEnd;
    private Tile selectedTile;
    private Editing editing;
    private MyButton bGrass, bWater, bRoadS, bRoadC, bWaterC, bWaterb, bWaterI;
    private MyButton currentButton;
    private int currentIndex;

    private Map<MyButton, ArrayList<Tile>> map;


    public ToolBar(int x, int y, int width, int height, Editing editing) {
        super(x, y, width, height);
        this.editing = editing;

        map = new HashMap<>();

        currentIndex = 0;

        initPathImgs();
        initButtons();
    }

    private void initPathImgs() {
        //Загружает картинки места появления врагов и картинку где вход в базу
        pathStart = LoadSave.getSpriteAtlas().getSubimage(7 * 32, 2 * 32, 32, 32);
        pathEnd = LoadSave.getSpriteAtlas().getSubimage(8 * 32, 2 * 32, 32, 32);
    }

    private void initButtons() {
        bMenu = new MyButton(bMenuStr.toString(), 2, 642, 100, 30);
        bSave = new MyButton(bSaveStr.toString(), 2, 682, 100, 30);

        int w = 50;
        int h = 50;
        int xStart = 110;
        int yStart = 650;
        int xOffset = (int) (w * 1.1f);
        int i = 0;

        bGrass = new MyButton("", xStart, yStart, w, h, i++);
        bWater = new MyButton("", xStart + xOffset, yStart, w, h, i++);

        //Тут, короче, есть всего 5 кнопок для вращаемых плиток, и у каждой кнопки присвоено значение ArrayList<Tile>
        //Нужно для вращения, вот
        initMapButton(bRoadS, editing.getGame().getTileManager().getRoadsS(), xStart, yStart, xOffset, w, h, i++);
        initMapButton(bRoadC, editing.getGame().getTileManager().getRoadsC(), xStart, yStart, xOffset, w, h, i++);
        initMapButton(bWaterC, editing.getGame().getTileManager().getCorners(), xStart, yStart, xOffset, w, h, i++);
        initMapButton(bWaterb, editing.getGame().getTileManager().getBeaches(), xStart, yStart, xOffset, w, h, i++);
        initMapButton(bWaterI, editing.getGame().getTileManager().getIslands(), xStart, yStart, xOffset, w, h, i++);
        bPathStart = new MyButton("", xStart, yStart + xOffset, w, h, i++);
        bPathEnd = new MyButton("", xStart + xOffset, yStart + xOffset, w, h, i++);

    }

    public void setButtonText() {
        bMenu.setText(bMenuStr.toString());
        bSave.setText(bSaveStr.toString());
    }


    private void initMapButton(MyButton b, ArrayList<Tile> list, int x, int y, int xOff, int w, int h, int id) {
        b = new MyButton("", x + xOff * id, y, w, h, id);
        map.put(b, list);

    }

    private void saveLevel() {
        editing.saveLevel();
    }

    public void rotateSprite() {
        //Проходит по каждому тайлу с помощью индекса (при нажатии на R), и присваевает эту клетку к selectedTile
        currentIndex++;
        if (currentIndex >= map.get(currentButton).size())
            currentIndex = 0;

        selectedTile = map.get(currentButton).get(currentIndex);
        editing.setSelectedTile(selectedTile);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(220, 123, 14));
        g.fillRect(x, y, width, height);


        drawButtons(g);
    }

    private void drawButtons(Graphics g) {
        bSave.draw(g);
        bMenu.draw(g);

        drawPathButton(g, bPathStart, pathStart);
        drawPathButton(g, bPathEnd, pathEnd);

        drawNormalButton(g, bGrass);
        drawNormalButton(g, bWater);
        drawSelectedTile(g);
        drawMapButtons(g);

        g.setFont(new Font("LucidaSans", Font.BOLD, 15));
        g.drawString(rotHintStr.toString(), 110, 780);
    }

    private void drawPathButton(Graphics g, MyButton b, BufferedImage img) {
        g.drawImage(img, b.x, b.y, b.width, b.height, null);
        drawButtonFeedback(g, b);
    }

    private void drawNormalButton(Graphics g, MyButton b) {
        g.drawImage(getButtonImg(b.getId()), b.x, b.y, b.width, b.height, null);
        drawButtonFeedback(g, b);
    }

    private void drawMapButtons(Graphics g) {
        for (Map.Entry<MyButton, ArrayList<Tile>> entry : map.entrySet()) {
            MyButton b = entry.getKey();
            BufferedImage img = entry.getValue().get(0).getSprite();

            g.drawImage(img, b.x, b.y, b.width, b.height, null);
            drawButtonFeedback(g, b);
        }
    }

    private void drawSelectedTile(Graphics g) {
        //Просто снова рисует выбранную клетку в углу нижней панели
        if (selectedTile != null) {
            g.drawImage(selectedTile.getSprite(), 550, 650, 50, 50, null);
            g.setColor(Color.BLACK);
            g.drawRect(550, 650, 50, 50);
        }
    }

    public BufferedImage getButtonImg(int id) {
        return editing.getGame().getTileManager().getSprite(id);
    }


    public void mouseClicked(int x, int y) {
        if (bMenu.getBounds().contains(x, y)) {
            SetGameState(MENU);
        } else if (bSave.getBounds().contains(x, y)) {
            saveLevel();
        } else if (bWater.getBounds().contains(x, y)) {
            selectedTile = editing.getGame().getTileManager().getTile(bWater.getId());
            editing.setSelectedTile(selectedTile);
        } else if (bGrass.getBounds().contains(x, y)) {
            selectedTile = editing.getGame().getTileManager().getTile(bGrass.getId());
            editing.setSelectedTile(selectedTile);
        } else if (bPathStart.getBounds().contains(x, y)) {
            selectedTile = new Tile(pathStart, -1, -1);
            editing.setSelectedTile(selectedTile);
        } else if (bPathEnd.getBounds().contains(x, y)) {
            selectedTile = new Tile(pathEnd, -2, -2);
            editing.setSelectedTile(selectedTile);
        } else {
            for (MyButton b : map.keySet())
                if (b.getBounds().contains(x, y)) {
                    selectedTile = map.get(b).get(0);
                    editing.setSelectedTile(selectedTile);
                    currentButton = b;
                    currentIndex = 0;
                    return;
                }
        }
    }


    public void mouseMoved(int x, int y) {
        bMenu.setMouseOver(false);
        bSave.setMouseOver(false);
        bWater.setMouseOver(false);
        bGrass.setMouseOver(false);
        bPathStart.setMouseOver(false);
        bPathEnd.setMouseOver(false);
        for (MyButton b : map.keySet())
            b.setMouseOver(false);
        if (bMenu.getBounds().contains(x, y)) {
            bMenu.setMouseOver(true);
        } else if (bSave.getBounds().contains(x, y)) {
            bSave.setMouseOver(true);
        } else if (bWater.getBounds().contains(x, y)) {
            bWater.setMouseOver(true);
        } else if (bGrass.getBounds().contains(x, y)) {
            bGrass.setMouseOver(true);
        } else if (bPathStart.getBounds().contains(x, y)) {
            bPathStart.setMouseOver(true);
        } else if (bPathEnd.getBounds().contains(x, y)) {
            bPathEnd.setMouseOver(true);
        } else {
            for (MyButton b : map.keySet())
                if (b.getBounds().contains(x, y)) {
                    b.setMouseOver(true);
                    return;
                }
        }
    }


    public void mousePressed(int x, int y) {
        if (bMenu.getBounds().contains(x, y)) {
            bMenu.setMousePressed(true);
        } else if (bSave.getBounds().contains(x, y)) {
            bSave.setMousePressed(true);
        } else if (bWater.getBounds().contains(x, y)) {
            bWater.setMousePressed(true);
        } else if (bGrass.getBounds().contains(x, y)) {
            bGrass.setMousePressed(true);
        } else if (bPathStart.getBounds().contains(x, y)) {
            bPathStart.setMousePressed(true);
        } else if (bPathEnd.getBounds().contains(x, y)) {
            bPathEnd.setMousePressed(true);
        } else {
            for (MyButton b : map.keySet())
                if (b.getBounds().contains(x, y)) {
                    b.setMousePressed(true);
                    return;
                }
        }
    }


    public void mouseReleased(int x, int y) {
        bMenu.resetBooleans();
        bSave.resetBooleans();
        bWater.resetBooleans();
        bGrass.resetBooleans();
        bPathStart.resetBooleans();
        bPathEnd.resetBooleans();
        for (MyButton b : map.keySet())
            b.resetBooleans();
    }

    public BufferedImage getPathStartImg() {
        return pathStart;
    }

    public BufferedImage getPathEndImg() {
        return pathEnd;
    }
}
