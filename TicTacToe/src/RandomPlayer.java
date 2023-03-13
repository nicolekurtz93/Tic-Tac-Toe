import java.util.Random;

public class RandomPlayer {
    Player Player;
    Random random = new Random();

    RandomPlayer(Player player) {
        this.Player = player;
    }

    Board TakeRandomAction(Board board) throws Exception {
        int randomNumber = 0;
        do {
            randomNumber = random.nextInt(9);
        } while (board.board[randomNumber] != '0');
        Board newBoard = new Board(board, randomNumber, Player.playerType);
        return newBoard;
    }

}
