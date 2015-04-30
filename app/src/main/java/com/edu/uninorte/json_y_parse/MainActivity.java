package com.edu.uninorte.json_y_parse;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.parse.Parse;


public class MainActivity extends ActionBarActivity  {
    TextView tvLat, tvLon;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "5JeZkVB7d4MfG1an64w2f3oQYbtndG6ZiWU9BFWc", "aMWvc1fam1TzKQVOOXeFXFqyGLhozqJBfFnsTHZZ");
        tvLat= (TextView) findViewById(R.id.tv_lat);
        tvLon= (TextView) findViewById(R.id.tv_lon);
        tvLat.setText("Latitud: ");
        tvLon.setText("Longitud: ");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,5, (android.location.LocationListener) this);
    }
    public void onLocationChanged(Location location) {
        tvLat.setText("Latitud: "+location.getLatitude());
        tvLon.setText("Longitud: "+location.getLongitude());
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
}
