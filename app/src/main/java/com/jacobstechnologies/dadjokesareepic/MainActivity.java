package com.jacobstechnologies.dadjokesareepic;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static String firstLine;
    public static SharedPreferences sharedPreferences;
    private TextView tv;
    private TextView joke;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        joke = findViewById((R.id.jokeBody));
        String[] lines = getJoke().split(System.getProperty("line.separator"));
        day = getDate()-1;
        firstLine = lines[day];
        joke.setText(firstLine);
        sharedPreferences = getSharedPreferences(getString(R.string.sharedPrefs), Context.MODE_PRIVATE);

        Toast.makeText(getApplicationContext(),"What's Up?!!", Toast.LENGTH_SHORT).show();

        tv = findViewById(R.id.Thegreeting);
        tv.setText(sharedPreferences.getString("greeting", "Hey! What's up doc?!"));

        //set alarm
        if (!(sharedPreferences.contains("alarmSetTrue"))) {
            SetAlarm(this);
            SetAlarmForOccasion(this, sharedPreferences.getInt("monthForBday", 5), sharedPreferences.getInt("dayForBday", 20));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            startActivity(new Intent(getApplicationContext(), SettingsActiv.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getJoke(){
        String joke = "";
        try {
            InputStream is = getAssets().open("jokes.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            joke = new String(buffer);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return joke;
    }

    public int getDate(){
        return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }

    public static void SetAlarm(Context c){
        NotificationManager nMgr = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(c, MainService.class);
        alarmIntent = PendingIntent.getBroadcast(c, 0, intent, 0);

// Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 01);

// setRepeating() lets you specify a precise custom interval--in this case,
// 20 minutes.

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putBoolean("alarmSetTrue", true);
        e.commit();
    }

    public static void SetAlarmForOccasion(Context c, int month, int day){
        NotificationManager nMgr = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
        AlarmManager alarmMgr;
        PendingIntent occIntent;
        alarmMgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(c, MainService.class);
        occIntent = PendingIntent.getBroadcast(c, 0, intent, 0);

// Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);


        calendar.set(Calendar.YEAR, month, day);
// setRepeating() lets you specify a precise custom interval--in this case,
// 20 minutes.

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                DateUtils.YEAR_IN_MILLIS, occIntent);
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putBoolean("alarmSetTrue", true);
        e.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences(getString(R.string.sharedPrefs), Context.MODE_PRIVATE);
        tv.setText(sharedPreferences.getString("greeting", "Hey! What's up doc?!"));
        if (sharedPreferences.getBoolean("anims", true)) {
            initAnims();
        }
    }

    public void setAnims(TextView obj, int duration, int path){
        if (sharedPreferences.getBoolean("anims", true)) {
            Animation a = AnimationUtils.loadAnimation(getApplicationContext(), path);

            a.setDuration(duration);
            if (path == R.anim.bounce){
                a.setRepeatCount(Animation.INFINITE);
            }

            obj.setAnimation(a);
        }
    }

    public void initAnims(){
        if (sharedPreferences.getBoolean("anims", true)) {
            setAnims(joke, 500, R.anim.zoomout);
            setAnims(tv, 700, R.anim.bounce);
        }
    }

    public void NextJoke(View v){
        String[] lines = getJoke().split(System.getProperty("line.separator"));
        day++;
        firstLine = lines[day];
        joke.setText(firstLine);
    }

    public void PrevJoke(View v){
        String[] lines = getJoke().split(System.getProperty("line.separator"));
        day--;
        firstLine = lines[day];
        joke.setText(firstLine);
    }

    public void ResetJoke(View v){
        String[] lines = getJoke().split(System.getProperty("line.separator"));
        firstLine = lines[getDate()-1];
        joke.setText(firstLine);
    }

    public void SendNotification(String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.mipmap.app_icon_round)
                .setContentTitle("Here's your joke of the day!")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotifChannel";
            String description = "Nothin";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

