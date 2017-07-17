package com.homemade.akhilez.timetable;

import android.os.CountDownTimer;
import android.text.format.Time;
import android.widget.TextView;

/**
 * Created by Akhilez on 1/26/2016.
 */
public class Timer {

    public static void startTimer(long duration, final long interval, final TextView timerText) {

        CountDownTimer timer = new CountDownTimer(duration, interval) {

            @Override
            public void onFinish() {
                long fiftyMin = 50*60*1000;
                startTimer(fiftyMin,10,timerText);
            }

            @Override
            public void onTick(long millisecondsLeft) {
                int secondsLeft = (int) Math.round((millisecondsLeft / (double) 1000));
                String t = Timer.secondsToString(secondsLeft);
                if(t.charAt(0)=='0' && t.charAt(1)=='0'){
                    String x = "";
                    for(int i=3; i<8; i++){
                        x += t.charAt(i);
                    }
                    timerText.setText(x);
                }else{
                    timerText.setText(t);
                }
            }
        };

        timer.start();
    }
    public static String secondsToString(int improperSeconds) {

        //Seconds must be fewer than are in a day

        if(Table.getDay().equals("Sun")&&Table.getTime24hr()<1615)
            return "";

        Time secConverter = new Time();

        secConverter.hour = 0;
        secConverter.minute = 0;
        secConverter.second = 0;

        secConverter.second = improperSeconds;
        secConverter.normalize(true);

        String hours = String.valueOf(secConverter.hour);
        String minutes = String.valueOf(secConverter.minute);
        String seconds = String.valueOf(secConverter.second);

        if (seconds.length() < 2) {
            seconds = "0" + seconds;
        }
        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }
        if (hours.length() < 2) {
            hours = "0" + hours;
        }

        String timeString =  hours + ":" + minutes + ":" + seconds;
        return timeString;
    }

}
