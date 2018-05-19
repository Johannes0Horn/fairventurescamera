package com.digitalmedia_design.fairventurescamera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    SharedPreferences myPrefs;

    //prefix TextEdit no object
    EditText mEdit1;
    //prefix TextEdit with object
    EditText mEdit2;
    //prefix TextEdit with diameter
    EditText mEdit3;
    //prefix TextEdit with TreeType
    EditText mEdit4;
    //prefix TextEdit with TreeHeight
    EditText mEdit5;
    //prefix TextEdit with TreeAge
    EditText mEdit6;

    RadioButton OR1;
    RadioButton OR2;
    RadioButton OR3;
    RadioButton OR4;

    RadioButton soil1;
    RadioButton soil2;
    RadioButton soil3;
    RadioButton soil4;

    RadioButton GPS1;
    RadioButton GPS2;

    int soilQuality = 0;

    int orientation = 0;

    boolean GPS = false;

    String myLocation = "disabled";

    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    LocationManager manager = null;

    String locationProvider = LocationManager.GPS_PROVIDER;

    LocationListener locationListener = null;

    private void makeUseOfNewLocation(Location l) {
        try {
            myLocation = Location.convert(l.getLatitude(), Location.FORMAT_DEGREES) + "-" + Location.convert(l.getLongitude(), Location.FORMAT_DEGREES);
        } catch (Exception e) {
            Log.v("Moment", "Fehler beim Location Parsen");
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public  void OnOrientation1(android.view.View view){
        orientation = 1;
    }

    public  void OnOrientation2(android.view.View view){
        orientation = 2;
    }

    public  void OnOrientation3(android.view.View view){
        orientation = 3;
    }

    public  void OnOrientation4(android.view.View view){
        orientation = 0;
    }

    public void OnSoilClick1(android.view.View view) {
        if (!soil1.isChecked())
            soil1.toggle();
        if (soil2.isChecked())
            soil2.toggle();
        if (soil3.isChecked())
            soil3.toggle();
        if (soil4.isChecked())
            soil4.toggle();

        soilQuality = 1;
    }

    public void OnSoilClick2(android.view.View view) {
        if (soil1.isChecked())
            soil1.toggle();
        if (!soil2.isChecked())
            soil2.toggle();
        if (soil3.isChecked())
            soil3.toggle();
        if (soil4.isChecked())
            soil4.toggle();

        soilQuality = 2;
    }

    public void OnSoilClick3(android.view.View view) {
        if (soil1.isChecked())
            soil1.toggle();
        if (soil2.isChecked())
            soil2.toggle();
        if (!soil3.isChecked())
            soil3.toggle();
        if (soil4.isChecked())
            soil4.toggle();

        soilQuality = 3;
    }

    public void OnSoilClick4(android.view.View view) {
        if (soil1.isChecked())
            soil1.toggle();
        if (soil2.isChecked())
            soil2.toggle();
        if (soil3.isChecked())
            soil3.toggle();
        if (!soil4.isChecked())
            soil4.toggle();

        soilQuality = 0;
    }

    public void OnGPSOn(android.view.View view) {
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            if (GPS1.isChecked())
                GPS1.toggle();
            if (!GPS2.isChecked())
                GPS2.toggle();
        } else {
            if (!GPS1.isChecked())
                GPS1.toggle();
            if (GPS2.isChecked())
                GPS2.toggle();
            GPS = true;
            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    makeUseOfNewLocation(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                manager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
            }catch (Exception e){
                    buildAlertMessageNoGps();
            }
        }
        }

    public  void  OnGPSOff(android.view.View view){
            if (!GPS2.isChecked())
                GPS2.toggle();
            if (GPS1.isChecked())
                GPS1.toggle();
            GPS = false;
            locationListener = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        mEdit1   = (EditText)findViewById(R.id.prefix);
        mEdit2   = (EditText)findViewById(R.id.prefix1);
        mEdit3   = (EditText)findViewById(R.id.diameter);
        mEdit4   = (EditText)findViewById(R.id.treetype);
        mEdit5   = (EditText)findViewById(R.id.height);
        mEdit6   = (EditText)findViewById(R.id.age);

        OR1      = (RadioButton)findViewById(R.id.OR1);
        OR2      = (RadioButton)findViewById(R.id.OR2);
        OR3      = (RadioButton)findViewById(R.id.OR3);
        OR4      = (RadioButton)findViewById(R.id.OR4);

        soil1    = (RadioButton)findViewById(R.id.radio1);
        soil2    = (RadioButton)findViewById(R.id.radio2);
        soil3    = (RadioButton)findViewById(R.id.radio3);
        soil4    = (RadioButton)findViewById(R.id.radio4);
        GPS1     = (RadioButton)findViewById(R.id.GPS1);
        GPS2     = (RadioButton)findViewById(R.id.GPS2);

        mEdit1.setText(myPrefs.getString("mEdit1",""));
        mEdit2.setText(myPrefs.getString("mEdit2",""));
        mEdit3.setText(myPrefs.getString("mEdit3",""));
        mEdit4.setText(myPrefs.getString("mEdit4",""));
        mEdit5.setText(myPrefs.getString("mEdit5",""));
        mEdit6.setText(myPrefs.getString("mEdit6",""));



        //region ShittySetOrientationRadioButtonsValueToState
        if(myPrefs.getInt("orientation",0)==1) {
            if (!OR1.isChecked()){
                OR1.toggle();
                orientation=1;
            }
        }
        else
        if (OR1.isChecked()) {
            OR1.toggle();
        }

        if(myPrefs.getInt("orientation",0)==2){
            if(!OR2.isChecked()){
                OR2.toggle();
                orientation=2;
            }
        }
        else
        if (OR2.isChecked()) {
            OR2.toggle();
        }

        if(myPrefs.getInt("orientation",0)==3){
            if(!OR3.isChecked()){
                OR3.toggle();
                orientation=3;
            }
        }
        else
        if (OR3.isChecked()) {
            OR3.toggle();
        }

        if(myPrefs.getInt("orientation",0)==0){
            if(!OR4.isChecked()) {
                OR4.toggle();
                orientation=0;
            }
        }
        else
        if (OR4.isChecked()) {
            OR4.toggle();
        }
        //endregion



        //region ShittySetSoilRadioButtonsValueToState
        if(myPrefs.getBoolean("soil1",false)) {
            if (!soil1.isChecked()){
                soil1.toggle();
                soilQuality=1;
            }
        }
        else
        if (soil1.isChecked()) {
            soil1.toggle();
        }

        if(myPrefs.getBoolean("soil2",false)){
            if(!soil2.isChecked()){
                soil2.toggle();
                soilQuality=2;
            }
        }
        else
        if (soil2.isChecked()) {
            soil2.toggle();
        }

        if(myPrefs.getBoolean("soil3",false)){
            if(!soil3.isChecked()){
                soil3.toggle();
                soilQuality=3;
            }
        }
        else
        if (soil3.isChecked()) {
            soil3.toggle();
        }

        if(myPrefs.getBoolean("soil4",true)){
            if(!soil4.isChecked()) {
                soil4.toggle();
                soilQuality=0;
            }
        }
        else
        if (soil4.isChecked()) {
            soil4.toggle();
        }
        //endregion

        if(!GPS2.isChecked())
            GPS2.toggle();

        mCurrentPhotoPath = myPrefs.getString("mCurrentPhotoPath",null);
        Log.v("Moment:",myPrefs.getString("mEdit2",""));

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onDestroy(){
        SharedPreferences.Editor e = myPrefs.edit();
        e.putString("mEdit1",mEdit1.getText().toString());
        e.putString("mEdit2",mEdit2.getText().toString());
        e.putString("mEdit3",mEdit3.getText().toString());
        e.putString("mEdit4",mEdit4.getText().toString());
        e.putString("mEdit5",mEdit5.getText().toString());
        e.putString("mEdit6",mEdit6.getText().toString());
        e.putBoolean("soil1",soil1.isChecked());
        e.putBoolean("soil2",soil2.isChecked());
        e.putBoolean("soil3",soil3.isChecked());
        e.putBoolean("soil4",soil4.isChecked());
        e.putInt("orientation",orientation);
        e.commit();
        super.onDestroy();
    }


    public  void callDispatchTakePictureIntentWithObject(android.view.View view){
        String FileName = "defaultWithReferenceObject";
        FileName = mEdit1.getText().toString();
        if(FileName==null)
            FileName ="defaultWithReferenceObject";
        String treeType = "defaultTreeType";
        treeType = mEdit4.getText().toString();
        if(treeType==null)
            treeType ="defaultTreeType";
        int Diameter = 0;
        int height = 0;
        int age = 0;
        try {
            Diameter = Integer.parseInt(mEdit3.getText().toString());
        }catch (Exception e){
            Log.v("Moment:","Keinen Durchmesser eingegeben");
        }
        try {
            height = Integer.parseInt(mEdit5.getText().toString());
        }catch (Exception e){
            Log.v("Moment:","Keine Höhe eingegeben");
        }
        try {
            age = Integer.parseInt(mEdit6.getText().toString());
        }catch (Exception e){
            Log.v("Moment:","Keine Alter eingegeben");
        }
        dispatchTakePictureIntent(FileName,Diameter,treeType,height,age);
    }

    public  void callDispatchTakePictureIntentWithoutObject(android.view.View view){
        String FileName = "defaultWithoutReferenceObject";
        if(FileName==null)
            FileName ="defaultWithoutReferenceObject";
        FileName = mEdit2.getText().toString();
        String treeType = "defaultTreeType";
        treeType = mEdit4.getText().toString();
        if(treeType==null)
            treeType ="defaultTreeType";
        int Diameter = 0;
        int height = 0;
        int age = 0;
        try {
            Diameter = Integer.parseInt(mEdit3.getText().toString());
        }catch (Exception e){
            Log.v("Moment:","Keinen Durchmesser eingegeben");
        }
        try {
            height = Integer.parseInt(mEdit5.getText().toString());
        }catch (Exception e){
            Log.v("Moment:","Keine Höhe eingegeben");
        }
        try {
            age = Integer.parseInt(mEdit6.getText().toString());
        }catch (Exception e){
            Log.v("Moment:","Keine Alter eingegeben");
        }
        dispatchTakePictureIntent(FileName,Diameter,treeType,height, age);
    }

    public void dispatchTakePictureIntent(String FileName, int Diameter, String treeType, int height, int age) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity    to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(FileName, Diameter, treeType, height, age);

            } catch (IOException ex) {
                Log.v("Moment","Fehler beim Bild Speichern");
                mCurrentPhotoPath=null;
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.digitalmedia_design.fairventurescamera.fileprovider",
                        photoFile);
               /* Uri photoURI;
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    File Root = Environment.getExternalStorageDirectory();
                    photoURI  = Uri.parse(new File(Root.getAbsolutePath() + "Fairventures Pictures").toString());
                }
                else
                    photoURI = FileProvider.getUriForFile(this,
                            "com.digitalmedia_design.fairventurescamera.fileprovider",
                            photoFile);*/

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }else {
            Log.v("Moment","keine Kamera");
            return;
        }
    }

    private File createImageFile(String FileName, int Diameter, String treeType, int height, int age) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName =FileName + "_" + Diameter + "_" + height + "_" + age + "_" + treeType + "_" + soilQuality + "_" + orientation + "_" + myLocation + "_" + timeStamp + "_"; //behind random temp string to be feteched
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* su   ffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Log.v("Got Here",mCurrentPhotoPath);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onResume(){
        super.onResume();



        //region ShittySetOrientationRadioButtonsValueToState
        if(myPrefs.getInt("orientation",0)==1) {
            if (!OR1.isChecked()){
                OR1.toggle();
                orientation=1;
            }
        }
        else
        if (OR1.isChecked()) {
            OR1.toggle();
        }

        if(myPrefs.getInt("orientation",0)==2){
            if(!OR2.isChecked()){
                OR2.toggle();
                orientation=2;
            }
        }
        else
        if (OR2.isChecked()) {
            OR2.toggle();
        }

        if(myPrefs.getInt("orientation",0)==3){
            if(!OR3.isChecked()){
                OR3.toggle();
                orientation=3;
            }
        }
        else
        if (OR3.isChecked()) {
            OR3.toggle();
        }

        if(myPrefs.getInt("orientation",0)==0){
            if(!OR4.isChecked()) {
                OR4.toggle();
                orientation=0;
            }
        }
        else
        if (OR4.isChecked()) {
            OR4.toggle();
        }
        //endregion



        //region ShittySetSoilRadioButtonsValueToState
        if(myPrefs.getBoolean("soil1",false)) {
            if (!soil1.isChecked()){
                soil1.toggle();
                soilQuality=1;
            }
        }
        else
        if (soil1.isChecked()) {
            soil1.toggle();
        }

        if(myPrefs.getBoolean("soil2",false)){
            if(!soil2.isChecked()){
                soil2.toggle();
                soilQuality=2;
            }
        }
        else
        if (soil2.isChecked()) {
            soil2.toggle();
        }

        if(myPrefs.getBoolean("soil3",false)){
            if(!soil3.isChecked()){
                soil3.toggle();
                soilQuality=3;
            }
        }
        else
        if (soil3.isChecked()) {
            soil3.toggle();
        }

        if(myPrefs.getBoolean("soil4",false)){
            if(!soil4.isChecked()) {
                soil4.toggle();
                soilQuality=0;
            }
        }
        else
        if (soil4.isChecked()) {
            soil4.toggle();
        }
        //endregion



        mCurrentPhotoPath = myPrefs.getString("mCurrentPhotoPath",null);

        if(mCurrentPhotoPath==null) {
            return;
        }
        Snackbar.make(findViewById(R.id.Coordinator), "Picture Saved.",
                Snackbar.LENGTH_LONG).show();
        Log.v("Moment","Snackbar Showed");
        galleryAddPic();
        mCurrentPhotoPath = null;
        SharedPreferences.Editor ed = myPrefs.edit();
        ed.putString("mCurrentPhotoPath", null);
        ed.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor ed = myPrefs.edit();
        ed.putString("mCurrentPhotoPath", mCurrentPhotoPath);
        ed.commit();

        SharedPreferences.Editor e = myPrefs.edit();
        e.putString("mEdit1",mEdit1.getText().toString());
        e.putString("mEdit2",mEdit2.getText().toString());
        e.putString("mEdit3",mEdit3.getText().toString());
        e.putString("mEdit4",mEdit4.getText().toString());
        e.putString("mEdit5",mEdit5.getText().toString());
        e.putString("mEdit6",mEdit6.getText().toString());
        e.putBoolean("soil1",soil1.isChecked());
        e.putBoolean("soil2",soil2.isChecked());
        e.putBoolean("soil3",soil3.isChecked());
        e.putBoolean("soil4",soil4.isChecked());
        e.putInt("orientation",orientation);
        e.commit();
    }
}
