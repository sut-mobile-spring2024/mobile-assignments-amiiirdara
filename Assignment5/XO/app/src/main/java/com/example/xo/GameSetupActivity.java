package com.example.xo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameSetupActivity extends AppCompatActivity {

    private RadioGroup playerTypeGroup;
    private RadioGroup symbolGroup;
    private RadioButton playerHuman;
    private RadioButton playerCPU;
    private RadioButton symbolX;
    private RadioButton symbolO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);

        playerTypeGroup = findViewById(R.id.playerTypeGroup);
        symbolGroup = findViewById(R.id.symbolGroup);
        playerHuman = findViewById(R.id.playerHuman);
        playerCPU = findViewById(R.id.playerCPU);
        symbolX = findViewById(R.id.symbolX);
        symbolO = findViewById(R.id.symbolO);
        Button startGameButton = findViewById(R.id.startGameButton);
        Button backButton = findViewById(R.id.backButton);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPlayerType = playerTypeGroup.getCheckedRadioButtonId();
                int selectedSymbol = symbolGroup.getCheckedRadioButtonId();

                if (selectedPlayerType == -1 || selectedSymbol == -1) {
                    Toast.makeText(GameSetupActivity.this, "Please select both options", Toast.LENGTH_SHORT).show();
                } else {
                    boolean playWithCPU = (selectedPlayerType == R.id.playerCPU);
                    String playerSymbol = (selectedSymbol == R.id.symbolX) ? "X" : "O";

                    Intent intent = new Intent(GameSetupActivity.this, GameActivity.class);
                    intent.putExtra("playWithCPU", playWithCPU);
                    intent.putExtra("playerSymbol", playerSymbol);
                    startActivity(intent);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameSetupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
