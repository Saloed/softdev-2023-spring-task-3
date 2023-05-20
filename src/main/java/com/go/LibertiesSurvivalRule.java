package com.go;

public class LibertiesSurvivalRule implements ICheckSurvivalGroupRule {

    @Override
    public boolean check(Stone stone, Board board) {
        // Поиск дыханий на доске
        boolean hasLeftNeighbor = board.isValidXBoundary(stone.x()) && board.isEmptyPosition(stone.x() - 1, stone.y());
        boolean hasRightNeighbor = board.isValidXBoundary(stone.x()) && board.isEmptyPosition(stone.x() + 1, stone.y());
        boolean hasTopNeighbor = board.isValidYBoundary(stone.y()) && board.isEmptyPosition(stone.x(), stone.y() + 1);
        boolean hasBottomNeighbor = board.isValidYBoundary(stone.y()) && board.isEmptyPosition(stone.x(), stone.y() - 1);

        //Поиск дыханий в угловых позициях
        boolean hasTopLeftCorner = board.isValidXBoundary(stone.x() - 1) && board.isEmptyPosition(stone.x() - 1, stone.y())
                || board.isValidYBoundary(stone.y() + 1) && board.isEmptyPosition(stone.x(), stone.y() + 1);
        boolean hasTopRightCorner = board.isValidXBoundary(stone.x() + 1) && board.isEmptyPosition(stone.x() + 1, stone.y())
                || board.isValidYBoundary(stone.y() + 1) && board.isEmptyPosition(stone.x(), stone.y() + 1);
        boolean hasBottomLeftCorner = board.isValidXBoundary(stone.x() - 1) && board.isEmptyPosition(stone.x() - 1, stone.y())
                || board.isValidYBoundary(stone.y() - 1) && board.isEmptyPosition(stone.x(), stone.y() - 1);
        boolean hasBottomRightCorner = board.isValidXBoundary(stone.x() + 1) && board.isEmptyPosition(stone.x() + 1, stone.y())
                || board.isValidYBoundary(stone.y() - 1) && board.isEmptyPosition(stone.x(), stone.y() - 1);

        // Если хотя бы одно из направлений имеет свободное дыхание
        return (hasLeftNeighbor || hasRightNeighbor || hasTopNeighbor || hasBottomNeighbor || hasTopLeftCorner
                || hasTopRightCorner || hasBottomLeftCorner || hasBottomRightCorner);
    }
}
