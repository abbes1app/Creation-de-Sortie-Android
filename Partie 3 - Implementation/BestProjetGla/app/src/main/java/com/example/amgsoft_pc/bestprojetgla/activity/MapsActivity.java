package com.example.amgsoft_pc.bestprojetgla.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.amgsoft_pc.bestprojetgla.R;
import com.example.amgsoft_pc.bestprojetgla.adapter.GooglePlacesAutocompleteAdapter;
import com.example.amgsoft_pc.bestprojetgla.database.DatabaseManager;
import com.example.amgsoft_pc.bestprojetgla.database.ligne.TypeEtape;
import com.example.amgsoft_pc.bestprojetgla.database.manipulation.ManipulationTypeEtape;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        View.OnClickListener, OnItemClickListener, GoogleMap.OnMapClickListener {
    SupportMapFragment mapFrag;
    Double Longitude1, Latitude1;
    ImageButton Place, btn;
    Boolean check = false;
    GPSTracker gps;
    AutoCompleteTextView autoCompView;
    private CircleMenu circleMenu;
    private GoogleMap mGoogleMap;
    private GoogleApiClient googleApiClient;
    private int cp;
    private int zoom;

    private static int[] listeIncrements = new int[]{1000, 2000, 3000, 6000, 10000, 20000, 40000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        circleMenu = (CircleMenu) findViewById(R.id.circle_menu);

        btn = (ImageButton) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!check) {
                    if (circleMenu.isOpened()) {
                        circleMenu.closeMenu();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                View i = findViewById(R.id.radar);
                                i.setVisibility(View.VISIBLE);
                                check = true;
                            }
                        }, 650);
                    } else {
                        View i = findViewById(R.id.radar);
                        i.setVisibility(View.VISIBLE);
                        check = true;
                    }
                } else {
                    View i = findViewById(R.id.radar);
                    i.setVisibility(View.INVISIBLE);
                    check = false;
                }
            }
        });


        cp = 6000;
        zoom = cpToZoom(cp);
        ElegantNumberButton radar = (ElegantNumberButton) findViewById(R.id.radar);
        radar.setNumber(Integer.toString(cp));
        radar.setRange(1000, 50000);
        radar.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                if (newValue > oldValue && newValue<listeIncrements[listeIncrements.length-1]) {
                    for(int i=0;i<listeIncrements.length;i++){
                        if(newValue < listeIncrements[i]){
                            cp = listeIncrements[i];
                            break;
                        }
                    }

                } if (newValue < oldValue && newValue>listeIncrements[0]) {
                    for(int i=listeIncrements.length-1;i>=0;i--){
                        if(newValue > listeIncrements[i]){
                            cp = listeIncrements[i];
                            break;
                        }
                    }
                }

                view.setNumber(String.valueOf(cp));

                zoom = cpToZoom(cp);

                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
            }
        });

        Place = (ImageButton) findViewById(R.id.buttonView);

//        Bsearch.setOnClickListener(this);
        Place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View k = findViewById(R.id.radar);
                k.setVisibility(View.INVISIBLE);
                check = false;

                if (circleMenu.isOpened()) {
                    circleMenu.closeMenu();
                } else {
                    View i = findViewById(R.id.circle_menu);
                    i.setVisibility(View.VISIBLE);
                    circleMenu.openMenu();
                }
            }
        });


        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        final ArrayList<TypeEtape> listeType = ManipulationTypeEtape.liste(db);
        DatabaseManager.getInstance().closeDatabase();

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.drawable.icon_menu, R.mipmap.icon_cancel);

        String[] listeCouleurs = new String[]{"#258CFF", "#30A400", "#FF4B32", "#8A39FF", "#FF6A00", "#FF6A50"};
        for (int i = 0; i < 6; i++) {
            circleMenu.addSubMenu(Color.parseColor(listeCouleurs[i]),
                    Integer.valueOf(listeType.get(i).getImage()));
        }
        circleMenu.setOnMenuSelectedListener(new OnMenuSelectedListener() {

            @Override
            public void onMenuSelected(int index) {
                if (Longitude1 == null || Latitude1 == null) {
                    Toast.makeText(getApplicationContext(), "Désolé, impossible de trouver votre position actuelle.", Toast.LENGTH_SHORT).show();
                } else {

                    StringBuilder sbValue = new StringBuilder(sbMethod(Latitude1, Longitude1, listeType.get(index).getType()));
                    PlacesTask placesTask = new PlacesTask(Integer.valueOf(listeType.get(index).getImage()));
                    placesTask.execute(sbValue.toString());
                }
            }

        });

        circleMenu.setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {
            @Override
            public void onMenuOpened() {
            }

            @Override
            public void onMenuClosed() {
                View i = findViewById(R.id.circle_menu);
                i.setVisibility(View.INVISIBLE);
            }
        });

        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_tiem));
        autoCompView.setOnItemClickListener(this);
        autoCompView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    double[] coord = getLocation(v.getText().toString());

                    if(coord != null) {
                        double latitude = coord[0];
                        double longitude = coord[1];
                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                        );

                        moveMap(latitude, longitude);
                        Latitude1 = latitude;
                        Longitude1 = longitude;

                        autoCompView.dismissDropDown();

                        // Pour fermer le clavier quand on clique sur un item
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private int cpToZoom(int cp){
        return (int) Math.round(-1.581 * Math.log(cp) + 26);
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMapToolbarEnabled(true);

            mGoogleMap.setOnMarkerDragListener(this);
            mGoogleMap.setOnMapLongClickListener(this);
            mGoogleMap.setOnMapClickListener(this);
            mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override

                public boolean onMyLocationButtonClick() {
                    autoCompView.setText("");

                    if (mGoogleMap.getMyLocation() == null) {
                        Toast.makeText(getApplicationContext(), "Désolé, impossible de trouver votre position actuelle", Toast.LENGTH_SHORT).show();
                    } else {
                        Longitude1 = mGoogleMap.getMyLocation().getLongitude();
                        Latitude1 = mGoogleMap.getMyLocation().getLatitude();

                        LatLng loc = new LatLng(Latitude1, Longitude1);

                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                    }
                    return true;
                }
            });

        }     }


    public StringBuilder sbMethod(double Lat, double Long, String Placee) {

        //use your current location here


        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + Lat + "," + Long);
        sb.append("&radius=" + cp);
        sb.append("&types=" + Placee);
        sb.append("&sensor=true");

        sb.append("&key=AIzaSyB1fipD3QM0XXQk88eS5xKrq3pmds2ZTKs");

        Log.d("Map", "url: " + sb.toString());

        return sb;
    }

    @Override
    public void onConnected(Bundle bundle) {
        gps = new GPSTracker(MapsActivity.this);

        if (gps.canGetLocation()) {
            Latitude1 = gps.getLatitude();
            Longitude1 = gps.getLongitude();
            moveMap();
        } else {
            gps.showSettingsAlert();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Impossible de vous localiser sans autorisation", Toast.LENGTH_LONG).show();
            } else {
                onMapReady(mGoogleMap);
            }
        }
    }

    private void moveMap(double Lat, double Long, int zoom){
        LatLng latLng = new LatLng(Lat, Long);

        //Moving the camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));

    }

    private void moveMap(double Lat, double Long) {
        LatLng latLng = new LatLng(Lat, Long);

        //Moving the camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }


    private void moveMap() {
        moveMap(Latitude1, Longitude1);
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        onMapClick(latLng);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (circleMenu.isOpened()) {
            circleMenu.closeMenu();
        } else if (check) {
            btn.callOnClick();
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        circleMenu.openMenu();
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onBackPressed() {
        if (circleMenu.isOpened()) {
            circleMenu.closeMenu();
        } else if (check) {
            btn.callOnClick();
        } else {
            super.onBackPressed();
        }
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);

        double[] coord = getLocation(str);

        if(coord != null) {
            double latitude = coord[0];
            double longitude = coord[1];
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
            );
            moveMap(latitude, longitude);
            Latitude1 = latitude;
            Longitude1 = longitude;

            // Pour fermer le clavier quand on clique sur un item
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    public double[] getLocation(String adresse){
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext());
            List<Address> addresses = geocoder.getFromLocationName(adresse, 1);
            if (addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();

                return new double[]{latitude, longitude};
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;
        int indexImage;

        public PlacesTask(int i) {
            indexImage = i;
        }

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask(indexImage);

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParserTask
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;
        int indexImage;

        public ParserTask(int i) {
            indexImage = i;
        }

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            Place_JSON placeJson = new Place_JSON();

            try {
                jObject = new JSONObject(jsonData[0]);

                places = placeJson.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            Log.d("Map", "list size: " + list.size());
            // Clears all the existing markers;
            mGoogleMap.clear();

            for (int i = 0; i < list.size(); i++) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);


                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                Log.d("Map", "place: " + name);

                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");

                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                markerOptions.title(name + " : " + vicinity);

                markerOptions.icon(BitmapDescriptorFactory.fromResource(indexImage));

                // Placing a marker on the touched position
                Marker m = mGoogleMap.addMarker(markerOptions);
                moveMap(Latitude1, Longitude1,zoom);
            }
        }
    }

    public class Place_JSON {

        /**
         * Receives a JSONObject and returns a list
         */
        public List<HashMap<String, String>> parse(JSONObject jObject) {

            JSONArray jPlaces = null;
            try {
                /** Retrieves all the elements in the 'places' array */
                jPlaces = jObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /** Invoking getPlaces with the array of json object
             * where each json object represent a place
             */
            return getPlaces(jPlaces);
        }

        private List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
            int placesCount = jPlaces.length();
            List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> place = null;

            /** Taking each place, parses and adds to list object */
            for (int i = 0; i < placesCount; i++) {
                try {
                    /** Call getPlace with place JSON object to parse the place */
                    place = getPlace((JSONObject) jPlaces.get(i));
                    placesList.add(place);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return placesList;
        }

        /**
         * Parsing the Place JSON object
         */
        private HashMap<String, String> getPlace(JSONObject jPlace) {

            HashMap<String, String> place = new HashMap<String, String>();
            String placeName = "-NA-";
            String vicinity = "-NA-";
            String latitude = "";
            String longitude = "";
            String reference = "";

            try {
                // Extracting Place name, if available
                if (!jPlace.isNull("name")) {
                    placeName = jPlace.getString("name");
                }

                // Extracting Place Vicinity, if available
                if (!jPlace.isNull("vicinity")) {
                    vicinity = jPlace.getString("vicinity");
                }

                latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = jPlace.getString("reference");
                place.put("place_name", placeName);
                place.put("vicinity", vicinity);
                place.put("lat", latitude);
                place.put("lng", longitude);
                place.put("reference", reference);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return place;
        }
    }
}
