import java.util.*;

public class QLearning {
    Random random = new Random();
    double Epsilon = 0.6;
    double learningRate = 0.9;
    double Gamma = 0.75; // Discount Factor
    HashMap<String, QTableObject> QTable = new HashMap<String, QTableObject>();
    Player player;
    Player opponent;

    QLearning(Player player, Player opponent) {
        this.player = player;
        this.opponent = opponent;
    }

    Board TakeAction(Board board) throws Exception {
        Double maxQValue = Double.MIN_VALUE;
        String key = null;
        Board newBoard;
        if (QTable.containsKey(String.valueOf(board.board))) {
            double randomChoice = random.nextDouble();
            if (randomChoice > (1 - Epsilon)) {
                newBoard = MakeRandomAction(board);
            } else {
                var current = QTable.get(String.valueOf(board.board));
                for (String action : current.Actions) {
                    if (QTable.containsKey(action)) {
                        var foundAction = QTable.get(action);
                        if (foundAction.QValue > maxQValue) {
                            maxQValue = foundAction.QValue;
                            key = action;
                        }
                    }
                }
                if (maxQValue == Double.MIN_VALUE) {
                    newBoard = MakeRandomAction(board);
                } else {
                    newBoard = new Board(board, key, player.playerType);
                }
            }
        } else {
            CreateActionsForBoardState(board, player);
            newBoard = MakeRandomAction(board);
        }
        CreateActionsForBoardState(newBoard, opponent);
        if (board.parent != null)
            UpdateQValue(board, newBoard, null);
        return newBoard;
    }

    public void UpdateQValue(Board childParent, Board child, String gameResult) {
        if (gameResult == "LOSE") {
            var childOfLoseBoard = QTable.get(String.valueOf(childParent.board));
            var parentOfChildOfLoseBoard = QTable.get(String.valueOf(childParent.parent.board));
            childOfLoseBoard.QValue = -1.00;
            QTable.put(String.valueOf(child.board), childOfLoseBoard);
            parentOfChildOfLoseBoard.QValue = parentOfChildOfLoseBoard.QValue
                    + learningRate * (Gamma * childOfLoseBoard.QValue - parentOfChildOfLoseBoard.QValue);
            QTable.put(String.valueOf(childParent.parent.board), parentOfChildOfLoseBoard);
            return;
        }
        var parent = QTable.get(String.valueOf(childParent.parent.board));
        var childObject = QTable.get(String.valueOf(child.board));
        if (gameResult == "WIN") {
            child.Qvalue = 1.00;
            childObject.QValue = child.Qvalue;
            QTable.put(String.valueOf(child.board), childObject);
        }
        if (gameResult == "TIE") {
            child.Qvalue = 0.50;
            childObject.QValue = child.Qvalue;
            QTable.put(String.valueOf(child.board), childObject);
        }
        parent.QValue = parent.QValue + learningRate * (Gamma * child.Qvalue - parent.QValue);
        childParent.parent.Qvalue = parent.QValue;
        QTable.put(String.valueOf(childParent.parent.board), parent);
        return;
    }

    private Board MakeRandomAction(Board board) throws Exception {
        int randomNumber = 0;
        do {
            randomNumber = random.nextInt(9);
        } while (board.board[randomNumber] != '0');
        Board newBoard = new Board(board, randomNumber, player.playerType);
        if (!QTable.containsKey(String.valueOf(newBoard.board)))
            CreateActionsForBoardState(newBoard, player);
        return newBoard;
    }

    private void CreateActionsForBoardState(Board board, Player player) throws Exception {
        if (QTable.containsKey(String.valueOf(board.board)))
            return;
        List<String> actions = new ArrayList<String>();
        var slots = board.empty_slots.values();
        for (int slot : slots) {
            Board createdBoard = new Board(board, slot, player.playerType);
            actions.add(String.valueOf(createdBoard.board));
        }
        QTable.put(String.valueOf(board.board), new QTableObject(actions, board.Qvalue));
    }

    public void PrintQMatrix() {
        var result = QTable.entrySet();
        for (Map.Entry<String, QTableObject> entry : result) {
            System.out.println(entry.getKey() + ":" + entry.getValue().QValue);
        }
    }

}
