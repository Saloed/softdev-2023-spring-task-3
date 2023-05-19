package com.go;

public class LibertiesSurvivalRule implements ICheckSurvivalGroupRule {

    @Override
    public boolean check(Stone stone, Board board) {
        boolean hasLeftNeighbor = board.isValidXBoundary(stone.x()) && board.isNotEmptyPosition(stone.x() - 1, stone.y());
        boolean hasRightNeighbor = board.isValidXBoundary(stone.x()) && board.isNotEmptyPosition(stone.x() + 1, stone.y());
        boolean hasTopNeighbor = board.isValidYBoundary(stone.y()) && board.isNotEmptyPosition(stone.x(), stone.y() + 1);
        boolean hasBottomNeighbor = board.isValidYBoundary(stone.y()) && board.isNotEmptyPosition(stone.x(), stone.y() - 1);

        return (hasLeftNeighbor && hasRightNeighbor && hasTopNeighbor && hasBottomNeighbor);
    }
}
