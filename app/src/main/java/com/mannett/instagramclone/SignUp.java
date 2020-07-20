package com.mannett.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private EditText edtName, edtPunchSpeed, edtPunchPower, edtKickSpeed, edtKickPower;
    private Button btnSaveToServer, btnGetAllData, btnTransition;
    private TextView txtGetData;
    private String allKickboxers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = findViewById(R.id.edtName);
        edtPunchSpeed = findViewById(R.id.edtPunchSpeed);
        edtPunchPower = findViewById(R.id.edtPunchPower);
        edtKickSpeed = findViewById(R.id.edtKickSpeed);
        edtKickPower = findViewById(R.id.edtKickPower);
        btnGetAllData = findViewById(R.id.btnGetAllData);
        btnTransition = findViewById(R.id.btnNextActivity);

        btnSaveToServer = findViewById(R.id.btnSaveToServer);
        btnSaveToServer.setOnClickListener(SignUp.this);

        txtGetData = findViewById(R.id.txtGetData);
        txtGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("KickBoxer");
                parseQuery.getInBackground("aHMOSmwtG8", new GetCallback<ParseObject>() { //getInBackground to get 1 object
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        if (object != null && e == null){

                            txtGetData.setText(object.get("name")+" - "+"PunchPower = "+object.get("punch_power"));
                        }
                    }
                });
            }
        });

        btnGetAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allKickboxers = "";

                ParseQuery<ParseObject> queryAll = ParseQuery.getQuery("KickBoxer");

                //queryAll.whereGreaterThan("punch_power",300);
                queryAll.whereGreaterThanOrEqualTo("punch_power",200);
                queryAll.setLimit(1);

                queryAll.findInBackground(new FindCallback<ParseObject>() { //findInBackground to get ALL objects
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        if (e == null ){

                            if (objects.size() > 0){

                                for (ParseObject kickBoxer : objects) {

                                    allKickboxers = allKickboxers + kickBoxer.get("name") + "\n";
                                }
                                FancyToast.makeText(SignUp.this, allKickboxers, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();


                            } else {
                                FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();


                            }
                        }

                    }
                });
            }
        });

        btnTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void onClick(View v){

        try {

            final ParseObject kickboxer = new ParseObject("KickBoxer");
            kickboxer.put("name", edtName.getText().toString());
            kickboxer.put("punch_speed", Integer.parseInt(edtPunchSpeed.getText().toString()));
            kickboxer.put("punch_power", Integer.parseInt(edtPunchPower.getText().toString()));
            kickboxer.put("kick_speed", Integer.parseInt(edtKickSpeed.getText().toString()));
            kickboxer.put("kick_power", Integer.parseInt(edtKickPower.getText().toString()));
            kickboxer.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(SignUp.this, kickboxer.get("name") + " is saved to server", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                        //Toast.makeText(SignUp.this,kickboxer.get("name")+"is saved to server",Toast.LENGTH_SHORT).show();
                    } else {
                        FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        // Toast.makeText(SignUp.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e){
            FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

        }
    }
}