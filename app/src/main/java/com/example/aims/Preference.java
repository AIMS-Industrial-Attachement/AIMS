package com.example.aims;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

public class Preference extends AppCompatActivity {
    Button select_temp,set;
    EditText get_temp;
    TextView emr_txt,crnt_unit;
    String emergency;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("Readings");
    MainTask mainTask = new MainTask();
    public  String x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        select_temp = findViewById(R.id.unit);
        get_temp = findViewById(R.id.set_temp);
        set = findViewById(R.id.set_emr);
        emr_txt = findViewById(R.id.textView28);
        crnt_unit = findViewById(R.id.textView31);

        myRef = FirebaseDatabase.getInstance().getReference().child("Readings");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.child("emr").getValue(String.class);
                x = dataSnapshot.child("unit").getValue(String.class);
                crnt_unit.setText(x);
                emr_txt.setText(value+"°C");
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });


        select_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Preference.this, select_temp);
                popupMenu.getMenuInflater().inflate(R.menu.menu_2,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(Preference.this, menuItem.getTitle()+"unit setted", Toast.LENGTH_SHORT).show();
                        select_temp.setText(menuItem.getTitle());
                        x = menuItem.getTitle().toString();
                        myRef.child("unit").setValue(x);
                        return  true;
                    }
                });
                popupMenu.show();
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    emergency = get_temp.getText().toString();
                    myRef.child("emr").setValue(emergency);
                    Toast.makeText(Preference.this,"Data Setted",Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    Toast.makeText(Preference.this,"Data Setted",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
