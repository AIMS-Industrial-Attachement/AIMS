package com.example.aims;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class AdminArea extends AppCompatActivity {
        EditText name, pass;
        Button change,delete;
        TextView debug,noOfId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_area);
        name = findViewById(R.id.editText6);
        pass = findViewById(R.id.editText7);
        change = findViewById(R.id.button2);
        noOfId = findViewById(R.id.textView45);
        delete = findViewById(R.id.button6);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("User");
                String x, y;
                x = name.getText().toString();
                y = pass.getText().toString();
              // debug.setText(x + y);
                if (!x.isEmpty() || !y.isEmpty()) {
                    myRef.child("Admin").setValue(x);
                    myRef.child("Pass").setValue(y);
                    Toast.makeText(getApplicationContext(), "Successfully changed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminArea.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(), "Please put ID and Password both!", Toast.LENGTH_SHORT).show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"This option will come soon.......",Toast.LENGTH_SHORT).show();
            }
        });
    }
}