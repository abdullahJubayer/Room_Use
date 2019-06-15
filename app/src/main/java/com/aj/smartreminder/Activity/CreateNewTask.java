package com.aj.smartreminder.Activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aj.smartreminder.Model.LocationDataModel;
import com.aj.smartreminder.R;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.io.Serializable;

public class CreateNewTask extends AppCompatActivity implements View.OnClickListener {
    EditText title_et,desc_et;
    public static TextView location_et,latitude,longitude;
    LinearLayout image_btn;
    Spinner priority_sp;
    private static final String Key="com.aj.smartreminder."+"NewTaskData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_task);

        findViwebyId();
        setSpinner();

        image_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v==image_btn){
            openMap();
        }
    }

    private void openMap() {
        startActivity(new Intent(getApplicationContext(),MapsActivity.class));
    }

    private void setSpinner() {
        String[] adapterData = {"Set Priority","Default", "Medium","High"};
        ArrayAdapter<CharSequence> spinnerAdapter =    new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_dropdown_item,adapterData);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority_sp.setAdapter(spinnerAdapter);
    }

    private void findViwebyId() {
        title_et=findViewById(R.id.newTask_title);
        desc_et=findViewById(R.id.newTask_description);
        location_et=findViewById(R.id.newTask_location);
        latitude=findViewById(R.id.lat_id);
        longitude=findViewById(R.id.long_id);
        image_btn=findViewById(R.id.newTask_locationIcon);
        priority_sp=findViewById(R.id.newTask_priority_spinner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.newtask_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String title=title_et.getText().toString().trim();
        String des=desc_et.getText().toString().trim();
        String address=location_et.getText().toString().trim();
        Double lat=Double.valueOf(latitude.getText().toString());
        Double lng=Double.valueOf(longitude.getText().toString());
        String priority=priority_sp.getSelectedItem().toString();


        if (item.getItemId()== R.id.menu_save_id){
            if (validate()){
                if (des.isEmpty()){
                    des="Description";
                }
                if (address.isEmpty()){
                    address="Address";
                }
                LocationDataModel dataModel=new LocationDataModel(title,des,address,lat,lng,priority);
                Intent intent=new Intent();
                intent.putExtra(Key, dataModel);
                setResult(RESULT_OK,intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validate() {

        String title=title_et.getText().toString().trim();
        Double lat=Double.valueOf(latitude.getText().toString());
        Double lng=Double.valueOf(longitude.getText().toString());
        boolean val=true;

        if ( title.isEmpty() || title ==null){
            val=false;
            title_et.setError("Title Must Added");
        }
        if ( lat==null){
            val=false;
            latitude.setError("Latitude Must Added");
        }
        if ( lng==null){
            val=false;
            longitude.setError("Longitude Must Added");
        }

        return val;
    }
}
