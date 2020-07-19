package com.example.aims;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

import androidx.appcompat.app.AppCompatActivity;

public class Extra extends AppCompatActivity {
    TextView t1,t2,t3,t4;
    Double prmnT, prmxT;
    DatabaseReference reference;
    DecimalFormat dec = new DecimalFormat("#0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        t1 = (TextView) findViewById(R.id.maxT);
        t2 = (TextView) findViewById(R.id.minT);
        t3 = (TextView) findViewById(R.id.maxH);
        t4 = (TextView) findViewById(R.id.minH);

        reference = FirebaseDatabase.getInstance().getReference().child("Readings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String mxT= dataSnapshot.child("mx_temp").getValue(String.class).toString();
                String mnT= dataSnapshot.child("mn_temp").getValue(String.class).toString();
                String mxH= dataSnapshot.child("mx_hum").getValue(String.class).toString();
                String mnH= dataSnapshot.child("mn_hum").getValue(String.class).toString();
                String x= dataSnapshot.child("unit").getValue(String.class).toString();

                if (x.equals("°F")){
                    double tmx = Double.valueOf(mxT);
                    double tmn = Double.valueOf(mnT);
                    prmxT = (tmx * (9.00/5.00))+32;
                    prmnT = (tmn * (9.00/5.00))+32;
                }
                else if(x.equals("°K")){
                    double tmx = Double.valueOf(mxT);
                    double tmn = Double.valueOf(mnT);
                    prmxT = (tmx + 273.15);
                    prmnT = (tmn + 273.15);
                }
                else {
                    prmxT = Double.valueOf(mxT);
                    prmnT = Double.valueOf(mnT);
                }
                t1.setText(dec.format(prmxT)+x);
                t2.setText(dec.format(prmnT)+x);
                t3.setText(mxH+"%");
                t4.setText(mnH+"%");
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

    }
}
