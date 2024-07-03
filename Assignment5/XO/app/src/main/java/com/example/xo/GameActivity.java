package com.example.xo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private boolean playerXTurn; // True if Player X's turn, False if Player O's turn
    private boolean playWithCPU;
    private String playerSymbol;
    private String cpuSymbol;
    private Button[][] buttons = new Button[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        playWithCPU = intent.getBooleanExtra("playWithCPU", false);
        playerSymbol = intent.getStringExtra("playerSymbol");
        cpuSymbol = playerSymbol.equals("X") ? "O" : "X";
        playerXTurn = playerSymbol.equals("X");

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button button = (Button) row.getChildAt(j);
                buttons[i][j] = button;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onButtonClick((Button) v);
                    }
                });
            }
        }

        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // If the player chooses O, the CPU starts if playing with CPU
        if (playWithCPU && playerSymbol.equals("O")) {
            cpuPlay();
        }
    }

    private void onButtonClick(Button button) {
        if (!button.getText().toString().equals("")) {
            return; // If button is already clicked, do nothing
        }

        button.setText(playerXTurn ? playerSymbol : cpuSymbol);
        String movePlayer = playWithCPU ? (playerXTurn ? "Player" : "CPU") : (playerXTurn ? "Player1" : "Player2");
        Toast.makeText(this, movePlayer + " moved to: " + getButtonPosition(button), Toast.LENGTH_SHORT).show();

        String prediction = getPrediction();
        Toast.makeText(this, prediction, Toast.LENGTH_SHORT).show();

        if (checkForWin()) {
            if (playWithCPU) {
                Toast.makeText(this, playerXTurn ? "Player wins!" : "CPU wins!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, playerXTurn ? "Player1 wins!" : "Player2 wins!", Toast.LENGTH_SHORT).show();
            }
            resetBoard();
        } else if (isBoardFull()) {
            Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
            resetBoard();
        } else {
            playerXTurn = !playerXTurn;
            if (playWithCPU && !playerXTurn) {
                cpuPlay();
            }
        }
    }

    private void cpuPlay() {
        int[] bestMove = findBestMove();
        if (bestMove != null) {
            Button button = buttons[bestMove[0]][bestMove[1]];
            button.setText(cpuSymbol);
            Toast.makeText(this, "CPU moved to: " + (bestMove[0] + 1) + ", " + (bestMove[1] + 1), Toast.LENGTH_SHORT).show();

            String prediction = getPrediction();
            Toast.makeText(this, prediction, Toast.LENGTH_SHORT).show();

            if (checkForWin()) {
                Toast.makeText(this, "CPU wins!", Toast.LENGTH_SHORT).show();
                resetBoard();
            } else if (isBoardFull()) {
                Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
                resetBoard();
            } else {
                playerXTurn = !playerXTurn;
            }
        }
    }

    private int[] findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    buttons[i][j].setText(cpuSymbol);
                    int score = minimax(0, false);
                    buttons[i][j].setText("");

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{i, j};
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMaximizing) {
        if (checkForWinCondition(cpuSymbol)) {
            return 1;
        }
        if (checkForWinCondition(playerSymbol)) {
            return -1;
        }
        if (isBoardFull()) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().toString().equals("")) {
                        buttons[i][j].setText(cpuSymbol);
                        int score = minimax(depth + 1, false);
                        buttons[i][j].setText("");
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().toString().equals("")) {
                        buttons[i][j].setText(playerSymbol);
                        int score = minimax(depth + 1, true);
                        buttons[i][j].setText("");
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    private boolean checkForWinCondition(String symbol) {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        // Check rows
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(symbol) && field[i][1].equals(symbol) && field[i][2].equals(symbol)) {
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(symbol) && field[1][i].equals(symbol) && field[2][i].equals(symbol)) {
                return true;
            }
        }

        // Check diagonals
        if (field[0][0].equals(symbol) && field[1][1].equals(symbol) && field[2][2].equals(symbol)) {
            return true;
        }

        if (field[0][2].equals(symbol) && field[1][1].equals(symbol) && field[2][0].equals(symbol)) {
            return true;
        }

        return false;
    }

    private boolean checkForWin() {
        return checkForWinCondition(playerXTurn ? playerSymbol : cpuSymbol);
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        playerXTurn = playerSymbol.equals("X");
        if (playWithCPU && playerSymbol.equals("O")) {
            cpuPlay();
        }
    }

    private String getButtonPosition(Button button) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == button) {
                    return (i + 1) + ", " + (j + 1);
                }
            }
        }
        return "";
    }

    private String getPrediction() {
        // Check if the next player can win with their move
        String nextPlayerSymbol = playerXTurn ? cpuSymbol : playerSymbol;
        if (canWinNextMove(nextPlayerSymbol)) {
            return playWithCPU ? (playerXTurn ? "CPU is predicted to win." : "Player is predicted to win.")
                    : (playerXTurn ? "Player2 is predicted to win." : "Player1 is predicted to win.");
        }

        int bestMove = findBestMoveForPrediction();
        if (bestMove == 1) {
            return playWithCPU ? "CPU is predicted to win." : "Player2 is predicted to win.";
        } else if (bestMove == -1) {
            return playWithCPU ? "Player is predicted to win." : "Player1 is predicted to win.";
        } else {
            return "The game is predicted to be a draw.";
        }
    }

    private boolean canWinNextMove(String symbol) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    buttons[i][j].setText(symbol);
                    boolean win = checkForWinCondition(symbol);
                    buttons[i][j].setText("");
                    if (win) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int findBestMoveForPrediction() {
        int bestScore = Integer.MIN_VALUE;
        String currentSymbol = playerXTurn ? playerSymbol : cpuSymbol;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    buttons[i][j].setText(currentSymbol);
                    int score = minimaxForPrediction(0, !playerXTurn);
                    buttons[i][j].setText("");
                    if (score > bestScore) {
                        bestScore = score;
                    }
                }
            }
        }
        return bestScore;
    }

    private int minimaxForPrediction(int depth, boolean isMaximizing) {
        String currentSymbol = isMaximizing ? cpuSymbol : playerSymbol;
        if (checkForWinCondition(currentSymbol)) {
            return isMaximizing ? 1 : -1;
        }
        if (isBoardFull()) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().toString().equals("")) {
                        buttons[i][j].setText(cpuSymbol);
                        int score = minimaxForPrediction(depth + 1, false);
                        buttons[i][j].setText("");
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().toString().equals("")) {
                        buttons[i][j].setText(playerSymbol);
                        int score = minimaxForPrediction(depth + 1, true);
                        buttons[i][j].setText("");
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }
}
