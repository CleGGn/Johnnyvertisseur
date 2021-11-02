package fr.afpa.convertisseur;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class ConvertAR extends AppCompatActivity {

    public final static String TAG = "ConvertARActivity";
    public final static String RETOUR = "msgConversion";
    public final static int RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        //setContentView(R.layout.activity_convert_a_r);

        // Récupère les données de l'Intent

        Intent intent = getIntent();

        String msg = intent.getExtras().getString("msg");

        // Retourner le résultat
        Intent intentResult = new Intent();
        intentResult.putExtra(ConvertAR.RETOUR, msg);
Log.i(TAG,msg);
        setResult(RESULT_CODE, intentResult);
        finish();
    }
}
