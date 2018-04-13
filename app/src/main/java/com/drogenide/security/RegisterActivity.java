package com.drogenide.security;
 
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
 
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID

    EditText edtNum;
    TextView registeredNum;
    Button btnRegistered;
 
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = prefs.edit();
        edtNum=findViewById(R.id.editTextNum);
        registeredNum=findViewById(R.id.txtregnum);
        btnRegistered=findViewById(R.id.btnRegiseration);
        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(prefs.getString("RegisteredNum","").equals("") || prefs.getString("RegisteredNum","").equals(null))
        {}
        else {
            registeredNum.setText("Registered Number :"+prefs.getString("RegisteredNum",""));
        }
        btnRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("RegisteredNum",edtNum.getText().toString());
                editor.commit();
                registeredNum.setText("Registered Number : "+edtNum.getText().toString());
            }
        });

    }
 
    public void onClickSelectContact(View btnSelectContact) {
 
        // using native contacts selection
        // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }
 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();
 
            retrieveContactName();
            retrieveContactNumber();
        }
    }
    
 
    private void retrieveContactNumber() {
 
        String contactNumber = null;
 
        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);
 
        if (cursorID.moveToFirst()) {
 
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
 
        cursorID.close();
 
        Log.d(TAG, "Contact ID: " + contactID);
 
        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
 
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
 
                new String[]{contactID},
                null);
 
        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            edtNum.setText(contactNumber.toString().replace(" ",""));
        }
 
        cursorPhone.close();
 
        Log.d(TAG, "Contact Phone Number: " + contactNumber);
    }
 
    private void retrieveContactName() {
 
        String contactName = null;
 
        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);
 
        if (cursor.moveToFirst()) {
 
            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.
 
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
 
        cursor.close();
 
        Log.d(TAG, "Contact Name: " + contactName);
 
    }
}