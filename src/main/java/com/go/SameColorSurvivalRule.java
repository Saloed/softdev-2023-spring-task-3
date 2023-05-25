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
        return stoneGroup.stream().anyMatch(groupStone -> libertiesSurvivalRule.check(groupStone, board));
    }

    // Поиск группы камней
    private List<Stone> findStoneGroup(Stone stone, Board board) {
        visitedPositions.clear(); // Очищаем список посещенных позиций
        stoneGroup = new ArrayList<>(); // Создаем новый список для группы камней

        findConnectedStones(stone.x(), stone.y(), board);// Находит все связанные камни в группе

        return stoneGroup;// Возвращает список камней, принадлежащих группе
    }

    private void findConnectedStones(int x, int y, Board board) {

        Stone position = board.getPosition(x, y);

        if (position == null || visitedPositions.contains(position) || position.color() != colorGroup) {
            return;
        }

        stoneGroup.add(position);
        visitedPositions.add(position);

        int[] dx = {-1, 1, 0, 0};  // Смещения по горизонтали для каждого направления
        int[] dy = {0, 0, -1, 1};  // Смещения по вертикали для каждого направления

        // Рекурсивный поиск соседей
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (board.isValidBoundary(nx, ny)) {
                findConnectedStones(nx, ny, board);
            }
        }
    }

    public List<Stone> getStoneGroup() {
        return stoneGroup;
    }
}
