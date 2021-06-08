package com.jacobstechnologies.dadjokesareepic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


public class SettingsActiv extends AppCompatActivity {

    static SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    EditText et;
    Button reset;
    CheckBox animations;
    Button done;
    static TextView viewBday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sharedPreferences =  getApplicationContext().getSharedPreferences(getString(R.string.sharedPrefs), Context.MODE_PRIVATE);

        reset = findViewById(R.id.resetGreeting);
        done = findViewById(R.id.done);
        viewBday = findViewById(R.id.viewDate);
        String s = sharedPreferences.getInt("monthOfBday", 5) + "/" + "" + sharedPreferences.getInt("dayOfBday", 20) + "/1973";
        viewBday.setText(s);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et.getText().toString() != "" && et.getText().toString() != null){
                    editor.putString("greeting", et.getText().toString());
                }
                else{
                    editor.putString("greeting", sharedPreferences.getString("greeting", "Hey! What's up doc?!"));
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (et.getText().toString() != "" && et.getText().toString() != null){
            editor.putString("greeting", et.getText().toString());
        }
        else{
            editor.putString("greeting", sharedPreferences.getString("greeting", "Hey! What's up doc?!"));
        }
        editor.putBoolean("anims", animations.isChecked());
        editor.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences =  getApplicationContext().getSharedPreferences(getString(R.string.sharedPrefs), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        et = findViewById(R.id.changeGreeting);
        et.setText(sharedPreferences.getString("greeting", "Hey! What's up doc?!"));

        animations = findViewById(R.id.animations);
        animations.setChecked(sharedPreferences.getBoolean("anims", true));
        String s = sharedPreferences.getInt("monthOfBday", 5) + "/" + "" + sharedPreferences.getInt("dayOfBday", 20) + "/" + sharedPreferences.getInt("yearOfBday", 1973);
        viewBday.setText(s);
    }

    public void reset(View v){
        et.setText("Hey! What's up doc?!");
    }

    public void changeBday(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static void resetShit(){
        String s = sharedPreferences.getInt("monthOfBday", 5) + "/" + "" + sharedPreferences.getInt("dayOfBday", 20) + "/" + sharedPreferences.getInt("yearOfBday", 1973);
        viewBday.setText(s);
    }
}
