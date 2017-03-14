package com.rromo12.retroachivementsmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FindPlayer extends BaseActivity implements View.OnClickListener {
    EditText userInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_player);
        String userName;
        userInput = (EditText) findViewById(R.id.userNameInput);
        Button findPlayerButton = (Button) findViewById(R.id.button_findPlayer);
        findPlayerButton.setText(getString(R.string.find_player));
        findPlayerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String userName = userInput.getText().toString();
        Intent intent = new Intent(this, UserProfile.class);
        intent.putExtra("user_name", userName);
        intent.putExtra("child", 1);
        startActivity(intent);
    }
}
