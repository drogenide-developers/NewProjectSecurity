package com.drogenide.security;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button btnMachineOn,btnSironOn,btnStopMachine,btnSironOff,btnClearLog;
    TextView machineDescTxt;
    SharedPreferences prefs;
    String machineDesc="App Started";
    String registeredNum="";
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(prefs.getString("RegisteredNum","").equals("") || prefs.getString("RegisteredNum","").equals(null))
        {}
        else {
            registeredNum=prefs.getString("RegisteredNum","");
        }
        
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
       btnMachineOn=findViewById(R.id.btn_startMachine);
       btnSironOn=findViewById(R.id.btn_startSiron);
       machineDescTxt=findViewById(R.id.txtMachineDesc);
       btnStopMachine=findViewById(R.id.btn_stopMachine);
       btnSironOff=findViewById(R.id.btn_stopSiron);
       btnClearLog=findViewById(R.id.btn_clearLog);
       machineDescTxt.setText(machineDesc);

       btnClearLog.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               machineDesc="Log Cleared";
               machineDescTxt.setText(machineDesc);
           }
       });

       btnMachineOn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               CheckSMSPermission();
               if(registeredNum.equals(""))
               {
                   Toast.makeText(DrawerActivity.this, "No number registered yet....!", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   sendSMS("e");
               }
           }
       });

        btnStopMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckSMSPermission();
                if(registeredNum.equals(""))
                {
                    Toast.makeText(DrawerActivity.this, "No number registered yet....!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sendSMS("d");
                }
            }
        });

        btnSironOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckSMSPermission();
                if(registeredNum.equals(""))
                {
                    Toast.makeText(DrawerActivity.this, "No number registered yet....!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sendSMS("on");
                }
            }
        });

        btnSironOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckSMSPermission();
                if(registeredNum.equals(""))
                {
                    Toast.makeText(DrawerActivity.this, "No number registered yet....!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sendSMS("off");
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent i=null;
        int id = item.getItemId();

        if (id == R.id.nav_registration) {
            // Handle the camera action
            i=new Intent(DrawerActivity.this,RegisterActivity.class);
        }
        else if (id == R.id.nav_home) {
            // Handle the camera action

        }
        else if (id == R.id.nav_user) {
            // Handle the camera action
            i=new Intent(DrawerActivity.this,RegisterActivity.class);

        }
        else if (id == R.id.nav_message) {
            // Handle the camera action
            i=new Intent(DrawerActivity.this,RegisterActivity.class);

        }
        if (i!=null)
        {
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sendSMS(String msg) {
        Log.i("Send SMS", "");
        SmsManager sms=SmsManager.getDefault();
        PendingIntent piSent=PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
        PendingIntent piDelivered=PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
        sms.sendTextMessage(registeredNum, null, msg, piSent, piDelivered);
        machineDesc=machineDesc+"\n Sending..";
        machineDescTxt.setText(machineDesc);
    }

    public void CheckSMSPermission()
    {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_CONTACTS},0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(prefs.getString("RegisteredNum","").equals("") || prefs.getString("RegisteredNum","").equals(null))
        {}
        else {
            registeredNum=prefs.getString("RegisteredNum","");
        }

        smsSentReceiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                // TODO Auto-generated method stub
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        //Toast.makeText(getBaseContext(), "SMS has been sent", Toast.LENGTH_SHORT).show();
                        machineDesc=machineDesc+"\n SMS Sent";
                        machineDescTxt.setText(machineDesc);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        //Toast.makeText(getBaseContext(), "Generic Failure", Toast.LENGTH_SHORT).show();
                        machineDesc=machineDesc+"\n SMS Failed";
                        machineDescTxt.setText(machineDesc);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No Service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio Off", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        };
        smsDeliveredReceiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                // TODO Auto-generated method stub
                switch(getResultCode()) {
                    case Activity.RESULT_OK:
                        //Toast.makeText(getBaseContext(), "SMS Delivered", Toast.LENGTH_SHORT).show();
                        machineDesc=machineDesc+"\n SMS Delivered";
                        machineDescTxt.setText(machineDesc);
                        break;
                    case Activity.RESULT_CANCELED:
                        //Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                        machineDesc=machineDesc+"\n SMS not Delivered";
                        machineDescTxt.setText(machineDesc);
                        break;
                }
            }
        };
        registerReceiver(smsSentReceiver, new IntentFilter("SMS_SENT"));
        registerReceiver(smsDeliveredReceiver, new IntentFilter("SMS_DELIVERED"));
    }
}
