import java.util.*;

public class Board {
    final int SIZE = 3;
    char[] board = new char[9];
    HashMap<Integer, Integer> empty_slots = new HashMap<Integer, Integer>();
    Double Qvalue = 0.00;
    int lastMove = -1;
    Board parent = null;

    Board() {
        for (int i = 0; i < (SIZE * SIZE); ++i) {
            empty_slots.put(i, i);
            board[i] = '0';
        }
    }

    Board(Board copyBoard, String key, char player) throws Exception {
        if (key == null)
            throw new Exception("Could not create board, the key was null");
        this.empty_slots.putAll(copyBoard.empty_slots);
        for (int i = 0; i < SIZE * SIZE; ++i) {
            char keyValue = key.charAt(i);
            this.board[i] = keyValue;
            if (keyValue != copyBoard.board[i]) {
                lastMove = i;
            }
        }
        empty_slots.remove(lastMove);
        this.parent = copyBoard;
    }

    Board(Board copyBoard, int move, char player) throws Exception {
        this.empty_slots.putAll(copyBoard.empty_slots);
        for (int i = 0; i < SIZE * SIZE; ++i) {
            this.board[i] = copyBoard.board[i];
            if (i == move) {
                lastMove = i;
                this.board[i] = player;
            }
        }
        empty_slots.remove(lastMove);
        this.parent = copyBoard;
    }

    Board(Board copyBoard) {
        for (int i = 0; i < SIZE; ++i) {
            this.board[i] = copyBoard.board[i];
        }
        this.empty_slots.putAll(copyBoard.empty_slots);
        lastMove = copyBoard.lastMove;
        this.parent = copyBoard;
    }

    void Print() {
        int counter = 1;
        for (char string : board) {
            if (counter % 3 == 0) {
                System.out.println(string + " ");
            } else
                System.out.print(string + " ");
            ++counter;
        }
        System.out.println();
    }

}
