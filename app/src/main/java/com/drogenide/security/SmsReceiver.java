package com.drogenide.security;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

    String specificPhoneNumber = "No you want";
    SharedPreferences prefs;

    public void onReceive(Context context, Intent intent)
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        specificPhoneNumber=prefs.getString("RegisteredNum","");
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";

        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                String phNum = msgs[i].getOriginatingAddress();
                str += msgs[i].getMessageBody().toString();
                if (specificPhoneNumber.equals(phNum))
                {
                    Uri uri = Uri.parse("content://sms/inbox");

                    ContentResolver contentResolver = context.getContentResolver();

                    String where = "address="+phNum;
                    Cursor cursor = contentResolver.query(uri, new String[] { "_id", "thread_id"}, where, null,
                            null);

                    while (cursor.moveToNext()) {

                        long thread_id = cursor.getLong(1);
                        where = "thread_id="+thread_id;
                        Uri thread = Uri.parse("content://sms/inbox");
                        context.getContentResolver().delete(thread, where, null);
                    }
                    Intent l = new Intent(context,DrawerActivity.class);
                    l.putExtra("msg",str);
                    l.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(l);
                }
            }
        }
    }
}
