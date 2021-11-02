package fr.afpa.convertisseur;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = "MainActivity";
    public final static String DEVISE_DEPART = "devDep";
    public final static String DEVISE_ARRIVEE = "devArr";
    public final static String MONTANT = "montant";
    public final static int REQUEST_CODE = 10;
    private ArrayList<String> listDevise;

    private String ligSpinDep = null;
    private String ligSpinArr = null;
    private Double dblAmount = null;
    private Double dblMontantArr = null;
    private String strAmount = null;
    private String msg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chargeDevise();
        chargeSpinner(R.id.spinner_devise_depart);
        chargeSpinner(R.id.spinner_devise_arrivee);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult");
        Log.i(TAG,"resultCode:" + resultCode);
        Log.i(TAG,"requestCode:" + requestCode);
        switch (requestCode) {
            case MainActivity.REQUEST_CODE:
Log.i(TAG,"SWITCH");

                if (resultCode == ConvertAR.RESULT_CODE) {
                    String msg = data.getStringExtra(ConvertAR.RETOUR);
Log.i(TAG,"msg:" + msg);

                    Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,R.string.err_result_code,Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(this,R.string.err_request_code,Toast.LENGTH_SHORT).show();
        }
    }

    private void chargeSpinner(int idSpinner){
        Spinner spinner = findViewById(idSpinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, listDevise);
        spinner.setAdapter(adapter);
    }

    public void chargeDevise(){
        Map<String, Double> tConversion = Convert.getConversionTable();
        this.listDevise = new ArrayList<String>(tConversion.keySet());

        this.listDevise.add("");
        Collections.sort(this.listDevise);

    }

    public void clicConvertirAR(View v) {
        Log.i(TAG, "clicConvertirAR");
        MediaPlayer m1 =  MediaPlayer.create(getBaseContext(),R.raw.optic);
        MediaPlayer m2 =  MediaPlayer.create(getBaseContext(),R.raw.vivre);
        if (doConvertir()){
            Intent intent = new Intent(MainActivity.this, ConvertAR.class);
            intent.putExtra("msg", this.msg);
            m2.start();
Log.i(TAG,this.msg);
            startActivityForResult(intent, MainActivity.REQUEST_CODE);
        } else m1.start();
    }

    private boolean doConvertir(){
        Log.i(TAG, "doConvertir");
        Boolean bFlag = false;
        // Récupérer les champs
        Spinner spinDevDep = (Spinner) findViewById(R.id.spinner_devise_depart);
        this.ligSpinDep = (String) spinDevDep.getSelectedItem();
        Spinner spinDevArr = (Spinner) findViewById(R.id.spinner_devise_arrivee);
         this.ligSpinArr = (String) spinDevArr.getSelectedItem();

        EditText totAmount = (EditText) findViewById(R.id.editText2);
        this.strAmount = totAmount.getText().toString();

        // Controler les valeurs
        if(ligSpinDep.equals("")){
            bFlag = false;
            Toast.makeText(this,R.string.errDevDep,Toast.LENGTH_SHORT).show();
        } else if(ligSpinArr.equals("")) {
            bFlag = false;
            Toast.makeText(this,R.string.errDevArr,Toast.LENGTH_SHORT).show();
        }else if(ligSpinDep.equals(ligSpinArr)) {
            bFlag = false;
            Toast.makeText(this,R.string.errSameAm,Toast.LENGTH_SHORT).show();
        } else if (strAmount.matches("[^0-9]")) {
            bFlag = false;
            Toast.makeText(this,R.string.errEmptyAm,Toast.LENGTH_SHORT).show();
        } else {
            try {
                this.dblAmount = Double.valueOf(strAmount);
                // Conversion
                this.dblMontantArr = Convert.convertir(ligSpinDep, ligSpinArr, dblAmount);
                //Afficher resultats
                this.msg = dblAmount + " " + ligSpinDep + " = " + dblMontantArr + " " + ligSpinArr;
                bFlag = true;
            } catch (NumberFormatException e) {
                bFlag = false;
            }
        } return bFlag;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
// Instanciation du menu XML spécifier en un objet Menu
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        View vItem = item.getActionView();
        Intent changerLangue = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        Intent changerDate = new Intent(Settings.ACTION_DATE_SETTINGS);
        Intent changerAffich = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
        switch (item.getItemId()) {
            case R.id.langue:
               startActivity(changerLangue);
                return true;
            case R.id.date:
                startActivity(changerDate);
                return true;
            case R.id.affichage:
                startActivity(changerAffich);
                return true;
            case R.id.convertir:
                convertClic(vItem);
                return true;
            case R.id.quitter:
                exitClic(vItem);
                return true;
        }
        return false;
    }

    public void convertClic(View v) {
         MediaPlayer m1 =  MediaPlayer.create(getBaseContext(),R.raw.optic);
         MediaPlayer m2 =  MediaPlayer.create(getBaseContext(),R.raw.vivre);
        if (doConvertir()){
            Log.i(TAG, "convertClic");
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("msg", this.msg);
            m2.start();
            startActivity(intent);
        } else m1.start();
    }

    public void exitClic(View v) {
        Log.i(TAG, "exitClic");
        System.exit(0);
        Toast.makeText(getBaseContext(), R.string.exited,Toast.LENGTH_SHORT).show();

    }
}