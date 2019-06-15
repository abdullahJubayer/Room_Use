package com.aj.smartreminder.Activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aj.smartreminder.Model.LocationDataModel;
import com.aj.smartreminder.Model.LocationViewModel;
import com.aj.smartreminder.R;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText title_et,desc_et;
    public static TextView location_et,latitude,longitude;
    private LinearLayout image_btn;
    private Spinner priority_sp;
    private LocationDataModel locationDataModel;
    private String[] adapterData = {"Set Priority","Default", "Medium","High"};
    private ArrayAdapter<CharSequence> spinnerAdapter ;
    private LocationViewModel locationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        spinnerAdapter=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_dropdown_item,adapterData);
        findViwebyId();
        setSpinner();

        image_btn.setOnClickListener(this);
        locationViewModel= ViewModelProviders.of(this).get(LocationViewModel.class);

        LocationDataModel dataModel= (LocationDataModel) getIntent().getSerializableExtra("location");
        if (dataModel != null){
            locationDataModel=dataModel;
            title_et.setText(locationDataModel.getTitle());
            desc_et.setText(locationDataModel.getDescription());
            location_et.setText(locationDataModel.getRoadNo());
            latitude.setText(String.valueOf(locationDataModel.getLatitude()));
            longitude.setText(String.valueOf(locationDataModel.getLongitude()));
            priority_sp.setSelection(spinnerAdapter.getPosition(locationDataModel.getPriority()));
        }else {
            Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        if (v==image_btn){
            openMap();
        }
    }

    private void openMap() {
        startActivity(new Intent(getApplicationContext(),MapsActivity.class).putExtra("Lat",locationDataModel.getLatitude()).putExtra("Lng",locationDataModel.getLongitude()));
    }

    private void setSpinner() {
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
                dataModel.setId(locationDataModel.getId());
                locationViewModel.update(dataModel);
                Toast.makeText(this, "Data Update", Toast.LENGTH_SHORT).show();

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
