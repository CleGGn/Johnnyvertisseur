package fr.afpa.convertisseur;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        afficheRes();
    }

    public void afficheRes(){
        TextView txt = findViewById(R.id.showRes);
        Intent followIntent = getIntent();
        String msg = followIntent.getExtras().getString("msg");
        txt.setText(msg);

    }

    public void retourClic(View v) {
        Log.i(TAG, "retourClic");
        finish();
    }
}

