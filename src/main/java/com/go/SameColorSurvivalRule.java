package com.go;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SameColorSurvivalRule implements ICheckSurvivalGroupRule {

    private final Set<Stone> visitedPositions = new HashSet<>();
    private List<Stone> stoneGroup = new ArrayList<>();
    private Color colorGroup;

    @Override
    public boolean check(Stone stone, Board board) {
        colorGroup = stone.color();// Установка цвета группы

        // Находит группу камней, к которой принадлежит переданный stone
        stoneGroup = findStoneGroup(stone, board);

        /*Проверяет, имеет ли группа камней хотя бы одну свободу с помощью правила LibertiesSurvivalRule и
        возвращает результат: имеет ли группа камней хотя бы одну свободу*/
        LibertiesSurvivalRule libertiesSurvivalRule = new LibertiesSurvivalRule();
        boolean hasSameColor = stoneGroup.stream().anyMatch(groupStone -> libertiesSurvivalRule.check(groupStone, board));
        if (!hasSameColor)
            removeStoneGroup(stoneGroup, board);
        return hasSameColor;
    }

    // Поиск группы камней
    private List<Stone> findStoneGroup(Stone stone, Board board) {
        visitedPositions.clear(); // Очищаем список посещенных позиций
        stoneGroup = new ArrayList<>(); // Создаем новый список для группы камней

        // Находит все связанные камни в группе
        findConnectedStones(stone.x(), stone.y(), board);

        // Возвращает список камней, принадлежащих группе
        return stoneGroup;
    }

    private void findConnectedStones(int x, int y, Board board) {

        Stone position = board.getPosition(x, y);

        if (position == null || visitedPositions.contains(position) || position.color() != colorGroup) {
            return;
        }

        stoneGroup.add(position);
        visitedPositions.add(position);

        if (board.isValidXBoundary(x - 1)) {
            findConnectedStones(x - 1, y, board);
        }
        if (board.isValidXBoundary(x + 1)) {
            findConnectedStones(x + 1, y, board);
        }
        if (board.isValidYBoundary(y - 1)) {
            findConnectedStones(x, y - 1, board);
        }
        if (board.isValidYBoundary(y + 1)) {
            findConnectedStones(x, y + 1, board);
        }
    }

    private void removeStoneGroup(List<Stone> stoneGroup, Board board) {
        for (Stone stone : stoneGroup) {
            if (stone.color() == Color.WHITE)
                board.capturedStonesWhite++;
            else {
                board.capturedStonesBlack++;
            }
            board.removeStone(stone);
        }
    }
}
