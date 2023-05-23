package com.go;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SameColorSurvivalRule implements ICheckSurvivalGroupRule {

    private final Set<Stone> visitedPositions = new HashSet<>();
    private final List<Stone> stoneGroup = new ArrayList<>();
    private Color colorGroup;

    @Override
    public boolean check(Stone stone, Board board) {
        colorGroup = stone.color();
        // Находит группу камней, к которой принадлежит переданный stone
        List<Stone> stoneGroup = findStoneGroup(stone, board);
        LibertiesSurvivalRule libertiesSurvivalRule = new LibertiesSurvivalRule();
        /*Проверяет, имеет ли группа камней хотя бы одну свободу с помощью правила LibertiesSurvivalRule и
        возвращает результат: имеет ли группа камней хотя бы одну свободу*/
        return stoneGroup.stream().anyMatch(groupStone -> libertiesSurvivalRule.check(groupStone, board));
    }

    // Поиск группы камней
    private List<Stone> findStoneGroup(Stone stone, Board board) {

        // Находит все связанные камни в группе
        findConnectedStones(stone.x(), stone.y(), board);
        // Возвращает список камней, принадлежащих группе
        return stoneGroup;
    }

    // Рекурсивно ищет все связанные в группу камни
    private void findConnectedStones(int x, int y, Board board) {
        Stone position = board.getPosition(x, y);

        // Если текущая позиция пустая, уже посещена или имеет другой цвет выходим из цикла
        if (position == null || visitedPositions.contains(position) ||
                position.color() != colorGroup) {
            return;
        }

        // Добавляет камень в группу и его координаты в список посещенных
        visitedPositions.add(position);
        stoneGroup.add(position);

        // Рекурсивно ищет соседей
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
}
