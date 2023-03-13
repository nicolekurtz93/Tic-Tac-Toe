public class Game {
    Player Player = new Player('X');
    Player Opponent = new Player('O');
    RandomPlayer randomPlayer = new RandomPlayer(Opponent);
    QLearning qLearningPlayer = new QLearning(Player, Opponent);
    CSVWriter csvWriter = new CSVWriter("Win Percent Over Training Epochs");
    int PlayerWins = 0;

    void PlayGame() throws Exception {
        int NumberOfTrainings = 5000;
        int NumberOfEpochs = 200;
        int NumberOfTests = 10;
        int totalWinPercent = 0;
        for (int i = 0; i < NumberOfEpochs; ++i) {
            float result = 0;
            for (int j = 0; j < NumberOfTests; ++j) {
                qLearningPlayer.Epsilon = 0;
                result = epoch(j, 100, result);
                if (j == NumberOfTests - 1) {
                    totalWinPercent += (result / NumberOfTests * 100);
                    System.out.println(
                            "Epoch: " + (i + 1) + " - Win Percent: " + (double) (result / NumberOfTests * 100) + "%");
                    csvWriter.dataLines
                            .add((i + 1) + "," + (double) (result / NumberOfTests * 100));
                }
            }
            qLearningPlayer.Epsilon = .8 - (.1 * (i / 50));
            if (qLearningPlayer.Epsilon > 1 || qLearningPlayer.Epsilon < 0)
                qLearningPlayer.Epsilon = 0;
            PlayGameLoop(NumberOfTrainings, NumberOfTrainings / 4);
        }
        csvWriter.CreateCSV();
        System.out.println("Average Win Percent Over " + NumberOfEpochs + " Epochs : "
                + totalWinPercent / NumberOfEpochs + "%");
        // qLearningPlayer.PrintQMatrix();
        System.out.println("Find the results in a CSV in the TicTacToe Folder!");
    }

    private float PlayGameLoop(int NumberOfEpisodes, int NumberOfEpisodesUntilEpsilonChanges) throws Exception {
        float totalScore = 0;
        for (int Episode = 0; Episode < NumberOfEpisodes; ++Episode) {
            epoch(Episode, NumberOfEpisodesUntilEpsilonChanges, totalScore);
        }
        return totalScore;
    }

    private float epoch(int Episode, int NumberOfEpisodesUntilEpsilonChanges, float totalScore) throws Exception {
        Board playerBoard = new Board();
        Board prevBoard;
        int counter = 0;
        if (Episode > 0 && Episode % NumberOfEpisodesUntilEpsilonChanges == 0) {
            if (qLearningPlayer.Epsilon > 0.00)
                qLearningPlayer.Epsilon -= .01;
            if (qLearningPlayer.Epsilon > 1)
                qLearningPlayer.Epsilon = 1.00;
        }
        while (counter < 5) {
            ++counter;
            prevBoard = playerBoard;
            playerBoard = qLearningPlayer.TakeAction(playerBoard);
            if (Player.didPlayWin(playerBoard.lastMove, playerBoard)) {
                qLearningPlayer.UpdateQValue(prevBoard, playerBoard, "WIN");
                totalScore += 1;
                break;
            }
            if (counter == 5) {
                qLearningPlayer.UpdateQValue(prevBoard, playerBoard, "TIE");
                totalScore += .5;
            } else {
                prevBoard = playerBoard;
                playerBoard = randomPlayer.TakeRandomAction(playerBoard);
                if (Opponent.didPlayWin(playerBoard.lastMove, playerBoard)) {
                    qLearningPlayer.UpdateQValue(prevBoard, playerBoard, "LOSE");
                    break;
                }
            }
        }
        return totalScore;
    }
}
