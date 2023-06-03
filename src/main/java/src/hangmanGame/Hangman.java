package src.hangmanGame;

import src.domain.HangmanInfo;
import src.vo.MenuNumber;
import src.view.InputView;
import src.view.OutputView;

import java.util.Objects;

public class Hangman {

    private static final String INPUT_GAME_ID = "게임 id를 입력해주세요.";
    private static final String INPUT_ROUND_ID = "라운드 id를 입력해주세요.";

    private static final String GAME_END_MESSAGE = "게임 종료. 이용해주셔서 감사합니다.";
    private static final String GAME_NEXT_MESSAGE = "다음 게임을 시작합니다.";

    private final HangmanInfo hangmanInfo;
    private final HangmanGameTable gameTable;

    public Hangman(HangmanInfo hangmanInfo, HangmanGameTable gameTable) {
        this.hangmanInfo = hangmanInfo;
        this.gameTable = gameTable;
    }

    public void gameSet() {
        HangmanGame hangmanGame;
        for (int gameCount = 0; gameCount < hangmanInfo.numberGames(); gameCount++) {
            hangmanGame = generateHangmanGame(gameCount);

            chooseMenu();

            hangmanGame.start();
            if (!isLastGame(gameCount)) {
                OutputView.printMessage(GAME_NEXT_MESSAGE);
            }
        }
        OutputView.printMessage(GAME_END_MESSAGE);
    }

    private void chooseMenu() {
        MenuNumber menuNumber = InputView.inputMenuNumber();
        switch (menuNumber.number()) {
            case 1: break;
            case 2: viewGameResult();
            case 3: viewRoundResult();
            default: chooseMenu();
        }
    }

    private void viewRoundResult() {
        try {
            int gameId = InputView.inputNumberOf(INPUT_GAME_ID);
            HangmanGameRoundTable roundTable = gameTable.getRoundTableWithException(gameId);
            int roundId = InputView.inputNumberOf(INPUT_ROUND_ID);
            HangmanGameRoundTable.HangmanGameRoundInfo roundInfo = roundTable.getRound(roundId);

            OutputView.printRoundResult(roundId, roundInfo);
        } catch (IllegalArgumentException exception) {
            OutputView.printMessage(exception.getMessage());
        }
    }

    private void viewGameResult() {
        try {
            int gameId = InputView.inputNumberOf(INPUT_GAME_ID);
            HangmanGameRoundTable roundTable = gameTable.getRoundTableWithException(gameId);

            OutputView.printGameResult(roundTable);
        } catch (IllegalArgumentException exception) {
            OutputView.printMessage(exception.getMessage());
        }
    }

    private HangmanGame generateHangmanGame(int gameCount) {
        int numberGames = gameCount + 1;
        int life = hangmanInfo.life();
        HangmanWord randomWord = RandomWordChooser.chooseWord();
        HangmanGameRoundTable roundTable = gameTable.getRoundTable(numberGames);

        return new HangmanGame(numberGames, life, randomWord, roundTable);
    }

    private boolean isLastGame(int gameCount) {
        return gameCount == hangmanInfo.numberGames() - 1;
    }

    public boolean equals(Hangman that) {
        return this.hangmanInfo.equals(that.hangmanInfo)
                && Objects.equals(this.gameTable, that.gameTable);
    }
}
