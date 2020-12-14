package com.androidcodefinder.cascadingspinners;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerMobile,spinnerModel;
    Button buttonSubmit;
    ProgressDialog pDialog;
    private String estado;
    // array list for spinner adapter
    private ArrayList<Nombre> nombreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerMobile= (Spinner)findViewById(R.id.spinnerMobile);
        spinnerModel=(Spinner)findViewById(R.id.spinnerModel);
        nombreList = new ArrayList<Nombre>();

        spinnerMobile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Refresh Spinner
                nombreList.clear();
                new GetModelFromServer().execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private class GetModelFromServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Fetching Data");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            estado = spinnerMobile.getSelectedItem().toString();
            Handler jsonParser = new Handler();
            String json = null;
            try {
                json = jsonParser.makeServiceCall("https://chilaquilesenterprise.com/localidades/get_model.php?nombre_estado="+ URLEncoder.encode(estado,"UTF-8"), Handler.GET);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.e("Response: ", "> " + json);

            if (json.equals(0)) {
                Log.e("JSON Empty", "Json Vacio");
            }
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray model = jsonObj
                                .getJSONArray("nombre");

                        for (int i = 0; i < model.length(); i++) {
                            JSONObject modObj = (JSONObject) model.get(i);
                            Nombre nom = new Nombre(modObj.getString("nombre"));
                            nombreList.add(nom);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
                populateSpinnerModel();
        }
    }

    private void populateSpinnerModel() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < nombreList.size(); i++) {
            lables.add(nombreList.get(i).getNombre());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerModel.setAdapter(spinnerAdapter);
    }
}
