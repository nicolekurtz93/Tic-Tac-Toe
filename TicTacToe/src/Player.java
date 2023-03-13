import java.util.*;

public class Player {
    char playerType;

    Player(char X_or_O) {
        this.playerType = X_or_O;
    }

    boolean didPlayWin(int lastMove, Board board) {
        // Check row for win
        int rowStart = (lastMove / 3) * 3;
        if (board.board[rowStart] == playerType)
            if (board.board[rowStart] == playerType) {
                if (board.board[rowStart + 1] == playerType) {
                    if (board.board[rowStart + 2] == playerType) {
                        return true;
                    }
                }
            }
        // check column for win
        int columnStart = lastMove % 3;
        if (board.board[columnStart] == playerType) {
            if (board.board[columnStart + 3] == playerType) {
                if (board.board[columnStart + 6] == playerType) {
                    return true;
                }
            }
        }
        // check diagonal
        int isDiagonalImpossible = lastMove % 2;
        if (isDiagonalImpossible == 0) // diagonal is possible
        {
            if (!(board.board[4] == playerType))
                return false;
            if (lastMove % 4 == 0) {
                if (board.board[0] == playerType) {
                    if (board.board[8] == playerType) {
                        return true;
                    }
                }
            } else {
                if (board.board[2] == playerType) {
                    if (board.board[6] == playerType) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
