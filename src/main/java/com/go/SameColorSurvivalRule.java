package com.go;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SameColorSurvivalRule implements ICheckSurvivalGroupRule {
    private Set<Stone> visitedPositions;

    @Override
    public boolean check(Stone stone, Board board) {
        // Находит группу камней, к которой принадлежит переданный stone
        List<Stone> stoneGroup = findStoneGroup(stone, board);
        LibertiesSurvivalRule libertiesSurvivalRule = new LibertiesSurvivalRule();

        // Проверяет, имеет ли группа камней хотя бы одну свободу с помощью правила LibertiesSurvivalRule
        boolean hasSameColor = stoneGroup.stream().anyMatch(groupStone -> libertiesSurvivalRule.check(groupStone, board));

        // Если группа не имеет свободы, удаляем все камни группы с игрового поля
        if (!hasSameColor) {
            removeStones(stoneGroup, board);
        }

        // Возвращает результат: имеет ли группа камней хотя бы одну свободу
        return hasSameColor;
    }

    // Поиск группы камней
    private List<Stone> findStoneGroup(Stone stone, Board board) {
        visitedPositions = new HashSet<>();
        List<Stone> stoneGroup = new ArrayList<>();
        Color colorGroup = stone.color();

        // Находит все связанные камни в группе
        findConnectedStones(stone.x(), stone.y(), stoneGroup, colorGroup, board);

        // Возвращает список камней, принадлежащих группе
        return stoneGroup;
    }

    // Рекурсивно ищет все связанные в группу камни
    private void findConnectedStones(int x, int y, List<Stone> stoneGroup, Color colorGroup, Board board) {
        Stone position = board.getPosition(x, y);

        // Если текущая позиция не существует, уже посещена, имеет другой цвет или
        // уже присутствует в группе, выходим из цикла
        if (position == null || visitedPositions.contains(position) ||
                position.color() != colorGroup || stoneGroup.contains(position)) {
            return;
        }

        // Добавляет камень в группу и его координаты в список посещенных
        visitedPositions.add(position);
        stoneGroup.add(position);

        // Рекурсивно ищет соседей
        if (board.isValidXBoundary(x - 1)) {
            findConnectedStones(x - 1, y, stoneGroup, colorGroup, board);
        }
        if (board.isValidXBoundary(x + 1)) {
            findConnectedStones(x + 1, y, stoneGroup, colorGroup, board);
        }
        if (board.isValidYBoundary(y - 1)) {
            findConnectedStones(x, y - 1, stoneGroup, colorGroup, board);
        }
        if (board.isValidYBoundary(y + 1)) {
            findConnectedStones(x, y + 1, stoneGroup, colorGroup, board);
        }
    }
    // Метод осуществляющий удаление группы камней
    private void removeStones(List<Stone> stoneGroup, Board board) {
        List<Stone> stonesToRemove = new ArrayList<>(stoneGroup);

        for (Stone stone : stonesToRemove) {
            board.removeStone(stone);
        }
    }
}

