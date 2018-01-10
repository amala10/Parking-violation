package com.google.android.gms.samples.vision.ocrreader.ui.camera;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.samples.vision.ocrreader.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by C58712 on 9/15/2017.
 */

public class ReportActivity extends AppCompatActivity {

    private TextView tvData;
    private TextView violation;
    String value;
    String Vehicle_number;
    String user;
    String vehicle_name;
    long mobile;
    String offence;
    int  b;
    Boolean clicked=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Button btnHit = (Button) findViewById(R.id.btnHit);
        tvData = (TextView) findViewById(R.id.tvJsonItem);
        value= getIntent().getStringExtra("getData");
        b=getIntent().getIntExtra("JSONnumber",0);
        violation = (TextView) findViewById(R.id.textView2);
        offence=value;
        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new JSONTask().execute("https://anurous-combustion.000webhostapp.com/this.php");
                violation.setText(value);
                clicked=true;
            }
        });



    }

    public class JSONTask extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try
            {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = " ";

                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line);

                }

                String finalJSON = buffer.toString();
                JSONArray parentArray = new JSONArray(finalJSON);

                JSONObject finalObject = parentArray.getJSONObject(b);
                Vehicle_number = finalObject.getString("Vehicle_number");
                user = finalObject.getString("user");
                vehicle_name = finalObject.getString("vehicle_name");
                mobile = finalObject.getLong("mobile");

                return Vehicle_number + "\n"
                        + user +"\n"
                        + vehicle_name + "\n"
                        + mobile + "\n";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvData.setText(result);

        }

    }



    // Triggers when LocationAdd Button clicked
    public void LocationAdd(View arg0) {
        if(clicked) {
            Intent intent4 = new Intent(this, LocationActivity.class);
            intent4.putExtra("VehicleNo", Vehicle_number);
            intent4.putExtra("userna", user);
            intent4.putExtra("vehiclena", vehicle_name);
            intent4.putExtra("cell", mobile);
            intent4.putExtra("offence", offence);
            startActivity(intent4);
        }
        else{
            Toast.makeText(ReportActivity.this, "Get vehicle details", Toast.LENGTH_LONG).show();
        }

    }


}
