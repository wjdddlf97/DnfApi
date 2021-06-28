package com.example.dnfapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.dnfapi.board.BoardInputActivity;

public class MainBoardActivity extends AppCompatActivity {

    Button board1ChangeButton;
    Button board2ChangeButton;
    Button board3ChangeButton;
    Button boardPlusBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_board);

        board1ChangeButton = findViewById(R.id.board1ChangeButton);
        board2ChangeButton = findViewById(R.id.board2ChangeButton);
        board3ChangeButton = findViewById(R.id.board3ChangeButton);
        boardPlusBtn = findViewById(R.id.boardPlusBtn);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, Fragment1.newInstance()).commit();

        board1ChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.add(R.id.frameLayout, Fragment1.newInstance()).commit();

                replaceFragment(Fragment1.newInstance());


            }
        });

        board2ChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.add(R.id.frameLayout, Fragment2.newInstance()).commit();

                replaceFragment(Fragment2.newInstance());
                Log.d("2","2실행");
            }
        });

        board3ChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.add(R.id.frameLayout, Fragment2.newInstance()).commit();

                replaceFragment(Fragment3.newInstance());
                Log.d("e","3실행");
            }
        });

        boardPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBoardInputActivity();
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit();
    }

    private void startBoardInputActivity() {
        Intent intent = new Intent(this, BoardInputActivity.class);
        startActivity(intent);
    }

}