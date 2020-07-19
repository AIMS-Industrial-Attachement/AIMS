package com.example.aims;

import androidx.appcompat.app.AppCompatActivity;
import me.itangqi.waveloadingview.WaveLoadingView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Board extends AppCompatActivity {
    TextView temperature, humidity,board,message,s1,s2,s3,s4,debug;
    String ch1,ch2,ch3,ch4,tm,hm,wt;
    String mess = "Message: ",time,day,hr;
    int flag1=0, flag2=0,wt_tank;
    double temp,himu, mxtemp=-1000, mxhimu=-1000,mntemp=1000,mnhimu=10000;
    public double emergency =35.00;
    WaveLoadingView mWaveLoadingView;
    ToggleButton t1,t2,t3,t4;
    DatabaseReference myRef;
    SpannableString on_ = new SpannableString("ON");
    SpannableString off_ = new SpannableString("OFF");
    ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);
    ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN);
    TextView Time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        temperature = (TextView)findViewById(R.id.textView1);
        humidity = (TextView)findViewById(R.id.textView2);
        board = (TextView)findViewById(R.id.textView);
        message = (TextView)findViewById(R.id.textView14);
        s1 =  (TextView)findViewById(R.id.textView3);
        s2 =  (TextView)findViewById(R.id.textView4);
        s3 =  (TextView)findViewById(R.id.textView5);
        s4 =  (TextView)findViewById(R.id.textView6);
        t1 = (ToggleButton)findViewById(R.id.toggleButton);
        t2 = (ToggleButton)findViewById(R.id.toggleButton2);
        t3 = (ToggleButton)findViewById(R.id.toggleButton3);
        t4 = (ToggleButton)findViewById(R.id.toggleButton4);
        Time = (TextView)findViewById(R.id.timeText);
        mWaveLoadingView = (WaveLoadingView)findViewById(R.id.waveload);
        // debug = findViewById(R.id.debug);

        myRef = FirebaseDatabase.getInstance().getReference().child("Readings");
        try {

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    tm = dataSnapshot.child("Temperature").getValue(String.class).toString();
                    hm = dataSnapshot.child("Humidity").getValue(String.class).toString();
                    ch1 = dataSnapshot.child("ch_1").getValue(String.class).toString();
                    ch2 = dataSnapshot.child("ch_2").getValue(String.class).toString();
                    ch3 = dataSnapshot.child("ch_3").getValue(String.class).toString();
                    ch4 = dataSnapshot.child("ch_4").getValue(String.class).toString();
                    day = dataSnapshot.child("day").getValue(String.class).toString();
                    hr =dataSnapshot.child("time").getValue(String.class).toString();
                    wt = dataSnapshot.child("w_level").getValue(String.class).toString();
                    time = "  Device Date : "+day+"\n  Device Time: "+hr;
                    wt_tank = Integer.parseInt(wt);
                    //Log.d(TAG, "Value is: " + value);
                    SetTemperature_Humidity();
                    getMaxValueData();
                    setWaterTankValue(wt_tank);
                    SetDtailsBox(wt_tank);
                    SetMessage();
                    setToggleButton();
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    flag1=1;
                }
            });
        }catch (Exception e){
            Toast.makeText(this,"Database Connection Error", Toast.LENGTH_SHORT).show();
        }
    }
    public void SetTemperature_Humidity(){
        try {
            temp = Double.parseDouble(tm);
            himu = Double.parseDouble(hm);
            temperature.setText(tm+"°C");
            humidity.setText(hm+"%");
            Time.setText(time);
        }catch (Exception e){
            message.setText(mess+"Sorry Database Error! ");
        }
    }
    public void getMaxValueData(){
        if (hr=="23:23"){
            myRef.child("mx_temp").setValue(mxtemp);
            myRef.child("mx_hum").setValue(mxhimu);
            myRef.child("mn_hum").setValue(mntemp);
            myRef.child("mn_hum").setValue(mnhimu);
            mnhimu=1000.00;
            mntemp=1000.00;
            mxtemp=-1000.00;
            mxhimu=-1000.00;
        }
        if(himu>mxhimu)
            mxhimu = himu;
        if(temp>mxtemp)
            mxtemp = temp;
        if(himu<mnhimu)
            mnhimu = himu;
        if(temp<mntemp)
            mntemp = temp;
    }
    public void setWaterTankValue(int x){

        double per = (x-700);
        per=(per/324)*100;
        if (x<=700){
            mWaveLoadingView.setProgressValue(0);
        }
        else {
            mWaveLoadingView.setProgressValue(((int) per));
        }
        if (per<=0){
            //mWaveLoadingView.setProgressValue(per);
            mWaveLoadingView.setBottomTitle("0%");
            mWaveLoadingView.setCenterTitle("");
            mWaveLoadingView.setTopTitle("");
            mWaveLoadingView.setWaveColor(Color.parseColor("#FF1919"));
            flag2=1;
        }
        else if (per<=10 && per>=0){
            //mWaveLoadingView.setProgressValue(per);
            mWaveLoadingView.setBottomTitle(String.valueOf((int)per)+"%");
            mWaveLoadingView.setCenterTitle("");
            mWaveLoadingView.setTopTitle("");
            mWaveLoadingView.setWaveColor(Color.parseColor("#FF1919"));
            flag2=1;
        }
        else if (per>10 && per<=20){
            mWaveLoadingView.setBottomTitle(String.valueOf((int)per)+"%");
            mWaveLoadingView.setTopTitle("");
            mWaveLoadingView.setCenterTitle("");
            mWaveLoadingView.setWaveColor(Color.parseColor("#FF4019"));
            flag2=1;
        }
        else if (per>20 && per<=30){
            flag2=1;
            mWaveLoadingView.setBottomTitle(String.valueOf((int)per)+"%");
            mWaveLoadingView.setTopTitle("");
            mWaveLoadingView.setCenterTitle("");
            mWaveLoadingView.setWaveColor(Color.parseColor("#FF6619"));
        }
        else if (per>30 && per<=40){
            flag2=0;
            mWaveLoadingView.setBottomTitle(String.valueOf((int)per)+"%");
            mWaveLoadingView.setTopTitle("");
            mWaveLoadingView.setCenterTitle("");
            mWaveLoadingView.setWaveColor(Color.parseColor("#FF8C19"));
        }
        else if (per>40 && per<=50){
            flag2=0;
            mWaveLoadingView.setCenterTitle(String.valueOf((int)per)+"%");
            mWaveLoadingView.setTopTitle("");
            mWaveLoadingView.setBottomTitle("");
            mWaveLoadingView.setWaveColor(Color.parseColor("#FFB319"));
        }
        else if (per>50 && per<=60){
            flag2=0;
            mWaveLoadingView.setCenterTitle(String.valueOf((int)per)+"%");
            mWaveLoadingView.setTopTitle("");
            mWaveLoadingView.setBottomTitle("");
            mWaveLoadingView.setWaveColor(Color.parseColor("#FFD919"));
        }
        else if (per>60 && per<=70){
            flag2=0;
            mWaveLoadingView.setCenterTitle(String.valueOf((int)per)+"%");
            mWaveLoadingView.setTopTitle("");
            mWaveLoadingView.setBottomTitle("");
            mWaveLoadingView.setWaveColor(Color.parseColor("#FFFF19"));
        }
        else if (per>70 && per<=80){
            flag2=0;
            mWaveLoadingView.setCenterTitle(String.valueOf((int)per)+"%");
            mWaveLoadingView.setTopTitle("");
            mWaveLoadingView.setBottomTitle("");
            mWaveLoadingView.setWaveColor(Color.parseColor("#D9FF19"));
        }
        else if (per>80 && per<=90){
            flag2=0;
            mWaveLoadingView.setTopTitle(String.valueOf((int)per)+"%");
            mWaveLoadingView.setCenterTitle("");
            mWaveLoadingView.setBottomTitle("");
            mWaveLoadingView.setWaveColor(Color.parseColor("#B3FF19"));
        }
        else if (per>90 && per<=100){
            flag2=0;
            mWaveLoadingView.setTopTitle(String.valueOf((int)per)+"%");
            mWaveLoadingView.setCenterTitle("");
            mWaveLoadingView.setBottomTitle("");
            mWaveLoadingView.setWaveColor(Color.parseColor("#8CFF19"));
        }
    }
    public void SetDtailsBox(int x){
        String level="";
        if (x<700){
            level = "Empty";
        }
        else if (x<800 && x>700){
            level = " Very Low";
        }
        else if (x<850 && x>800){
            level = "Low";
        }
        if (x<950 && x>850){
            level = "Medium";
        }
        else if (x>950){
            level = "High";
        }
        String chnl1, chnl2, chnl3, chnl4;
        if(ch1=="1")
            chnl1="Active    ";
        else
            chnl1="Deactive";
        if(ch2=="1")
            chnl2="Active    ";
        else
            chnl2="Deactive";
        if(ch3=="1")
            chnl3="Active    ";
        else
            chnl3="Deactive";
        if(ch4=="1")
            chnl4="Active    ";
        else
            chnl4="Deactive";
        String Details = "\n\n\n Device Info            : NodeMCU ESP8266\n Sensor1                 : DHT22\n Sensor2                 : Liquid Level Sensor\n Liquid Level          : "+level+"\n Max Temperature: "+mxtemp+"°C\n Min Temperature : "+mntemp+"°C\n Max Humidity       : "+mxhimu+"%\n Min Humidity        : "+mnhimu+"%"+
                "\n Switch-1                : "+chnl1+"\t\t      Switch-2: "+chnl2+"\n Switch-3                : "+chnl3+"\t\t      Switch-4: "+chnl4;
        board.setText(Details);
    }
    public void SetMessage(){
        if (flag2==1){
            mess = " Message:  Please Switch On the Water Pump! \uD83D\uDE10 ";
            message.setTextColor(Color.RED);
            message.setText(mess);
        }
        else if (ch3=="1") {
            message.setTextColor(Color.parseColor("#006400"));
            message.setText("Message: Everything is ok! \uD83D\uDE00");
            flag2=0;
        }
    }
    public void setToggleButton(){
        if (temp<35.00){
            t1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b & temp < 35.00) {
                        myRef.child("ch_1").setValue("1");
                        on_.setSpan(fcsGreen, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        s1.setText(on_);
                    } else {
                        myRef.child("ch_1").setValue("0");
                        off_.setSpan(fcsRed, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        s1.setText(off_);
                        b = false;
                    }
                }
            });
            t2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b & temp < 35.00) {
                        myRef.child("ch_2").setValue("1");
                        on_.setSpan(fcsGreen, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        s2.setText(on_);
                    } else {
                        myRef.child("ch_2").setValue("0");
                        off_.setSpan(fcsRed, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        s2.setText(off_);
                        b = false;
                    }

                }
            });
            t3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (b & temp < 35.00) {
                        myRef.child("ch_3").setValue("1");
                        on_.setSpan(fcsGreen, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        s3.setText(on_);
                    } else {
                        myRef.child("ch_3").setValue("0");
                        off_.setSpan(fcsRed, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        s3.setText(off_);
                        b = false;
                    }

                }
            });
            t4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b & temp < 35.00) {
                        myRef.child("ch_4").setValue("1");
                        on_.setSpan(fcsGreen, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        s4.setText(on_);

                    } else {
                        myRef.child("ch_4").setValue("0");
                        off_.setSpan(fcsRed, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        s4.setText(off_);
                        b = false;
                    }
                }
            });
        }
        else
        {
            if (temp>=35.00){
                myRef.child("ch_1").setValue("0");
                myRef.child("ch_2").setValue("0");
                myRef.child("ch_3").setValue("0");
                myRef.child("ch_4").setValue("0");
                off_.setSpan(fcsRed, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                s4.setText(off_);
                s3.setText(off_);
                s2.setText(off_);
                s1.setText(off_);
            }
        }
    }
}
