package com.example.xo_game;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static android.graphics.Color.parseColor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mGifImage ;
    private TextView playerOneScore , playerTwoScore , playerStatus ;
    private Button[] buttons = new Button[9];
    private Button resetGame ;

    private int playerOneScoreCount , playerTwoScoreCount , roundCount ;
    boolean activePlayer ;  // To Switch between the players

    // P1 : 0
    // P2 : 1
    // empty : 2

    int [] gameState = {2,2,2,2,2,2,2,2,2} ;
    int [][] winningPositions = {
            {0,1,2} , {3,4,5} , {6,7,8} // rows
            ,{0,3,6} , {1,4,7} , {2,5,8} // columns
            , {0,4,8} , {2,4,6}    // cross
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        roundCount = 0 ;
        playerOneScoreCount = 0 ;
        playerTwoScoreCount = 0 ;
        activePlayer = true ;
    }
    private void initViews() {
        playerOneScore = findViewById(R.id.PlayerOneScore);
        playerTwoScore = findViewById(R.id.PlayerTwoScore);
        playerStatus = findViewById(R.id.PlayerStatus);
        resetGame = findViewById(R.id.resetGame);

        mGifImage = findViewById(R.id.gif_image);
        Glide.with(MainActivity.this).load(R.raw.giphy).into(mGifImage);

        for (int i = 0 ; i<buttons.length ; i++)
        {
            String buttonId = "btn_"+ i ;
            int resourceId = getResources().getIdentifier(buttonId , "id" , getPackageName());
            buttons[i] = findViewById(resourceId);
            buttons[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(!((Button)v).getText().toString().equals("")){
            return;
        }
        String buttonId = v.getResources().getResourceEntryName(v.getId());   // for example "btn_2"
        int gameStatePointer = Integer.parseInt(buttonId.substring(buttonId.length()-1 , buttonId.length()));    // it will be :  2

        if(activePlayer){
            ((Button)v).setText("X");
            ((Button)v).setTextColor(parseColor("#FF5722"));
            gameState[gameStatePointer] = 0 ;
        }
        else {
            ((Button)v).setText("O");
            ((Button)v).setTextColor(parseColor("#4CAF50"));
            gameState[gameStatePointer] = 1 ;
        }
        roundCount++ ;

        if(checkWinner()){
            if (activePlayer){
                playerOneScoreCount++ ;
                updatePlayerScore();
                Toast.makeText(this , "Player one won !" , Toast.LENGTH_LONG).show();
                playAgain();
            }
            else {
                playerTwoScoreCount++ ;
                updatePlayerScore();
                Toast.makeText(this , "Player Two won !" , Toast.LENGTH_LONG).show();
                playAgain();
            }
        }
        else if(roundCount == 9){
            playAgain();
            Toast.makeText(this , "No winner !" , Toast.LENGTH_LONG).show();
        }
        else {
            activePlayer =  !activePlayer ;
        }


        if (playerOneScoreCount > playerTwoScoreCount)
            playerStatus.setText("Player one Winner !");

        else if(playerTwoScoreCount > playerOneScoreCount)
            playerStatus.setText("Player two Winner !");

        else
            playerStatus.setText("Tied");

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
                playerOneScoreCount = 0 ;
                playerTwoScoreCount = 0 ;
                playerStatus.setText("");
                updatePlayerScore();
            }
        });
    }

    public boolean checkWinner(){
        boolean winnerResult = false ;
        for(int[] winningPossition  : winningPositions)
        {
            if(gameState[winningPossition[0]] == gameState[winningPossition[1]] &&
                    gameState[winningPossition[1]]  == gameState[winningPossition[2]] &&
                    gameState[winningPossition[0]] != 2 ){
                winnerResult = true ;
            }
        }
        return winnerResult ;
    }

    public void updatePlayerScore(){
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerOneScore.setTextColor(parseColor("#01DF01"));
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        playerOneScore.setTypeface(boldTypeface) ;
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
        playerTwoScore.setTextColor(parseColor("#01DF01"));
        playerTwoScore.setTypeface(boldTypeface) ;
    }

    public void playAgain(){
        roundCount = 0 ;
        activePlayer = true ;

        for (int i=0 ; i<buttons.length ; i++){
            gameState[i] = 2 ;
            buttons[i].setText("");
        }
    }
}