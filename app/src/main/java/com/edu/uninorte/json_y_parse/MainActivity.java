package com.edu.uninorte.json_y_parse;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements LocationListener{

    private ProgressDialog pDialog;
    private ListView listView;
    List<ParseObject> ob;
    private ArrayList values;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "5JeZkVB7d4MfG1an64w2f3oQYbtndG6ZiWU9BFWc", "aMWvc1fam1TzKQVOOXeFXFqyGLhozqJBfFnsTHZZ");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }
    public void CargarDatos(View view){
               stopCapturing();
                Toast.makeText(getApplicationContext(), "EMPIEZA DESCARGA.", Toast.LENGTH_SHORT).show();
                new GetData().execute();
            }
      public void MandarDatos (View view){
            stopCapturing();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0,this);
            Toast.makeText(getApplicationContext(), "GPS INICIADO. SU UBICACIÓN ESTA SIENDO GUARDADA EN PARSE", Toast.LENGTH_SHORT).show();
        }

    public void stopCapturing() {

        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isNetworkAvaible = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isNetworkAvaible = true;
            Toast.makeText(this, "Network is available ", Toast.LENGTH_LONG)
                    .show();
        } else {
            Toast.makeText(this, "Network not available ", Toast.LENGTH_LONG)
                    .show();
        }
        return isNetworkAvaible;
    }



    @Override
    public void onLocationChanged(Location location) {
        if (isNetworkAvailable()){
            new SendData(location).execute();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private class SendData extends AsyncTask<Void, Void, Void> {

        Location location;
        public SendData(Location location){
            super();
            this.location = location;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {


            ParseObject testObject = new ParseObject("Rutas");
            testObject.put("latitude",location.getLatitude());
            testObject.put("longitude",location.getLongitude());
            Log.i("location: ", location.getLatitude()+"");
            Log.i("location: ", location.getLongitude()+"");
            testObject.saveInBackground();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
        }

    }

    // RemoteDataTask AsyncTask
    private class GetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            pDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
            pDialog.setTitle("Cargando datos de Parse");
            // Set progressdialog message
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            // Show progressdialog
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
            values = new ArrayList<String>();
            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Rutas");

                ob = query.find();
                for (ParseObject dato : ob) {

                    values.add("Latitude: "+dato.get("latitude")+ "Longitude: " + dato.get("longitude"));

                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            // Pass the results into ListViewAdapter.java
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);
            listView.setAdapter(adapter);
            // Close the progressdialog
            pDialog.dismiss();
        }
    }
}
