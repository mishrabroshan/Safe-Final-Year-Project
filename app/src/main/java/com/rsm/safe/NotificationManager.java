package com.rsm.safe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.rsm.safe.Bean.TrustedContactModel;
import com.rsm.safe.Constants.ConstantsVariables;
import com.rsm.safe.Database.SafeDatabase;

import java.util.List;

public class NotificationManager extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SafeDatabase safeDatabase = new SafeDatabase(context);

        List<TrustedContactModel> trustedContacts = safeDatabase.getTrustedContacts();
        if (trustedContacts.size() > 0) {
            SmsManager smsManager = SmsManager.getDefault();
            for (TrustedContactModel contactModel : trustedContacts) {
                String number = String.valueOf(contactModel.getNumber());
                if (numberManager(number) != null){
                    smsManager.sendTextMessage(numberManager(number), number, ConstantsVariables.MESSAGE, null, null);
                }
                else {
                    Toast.makeText(context, "Invalid Number", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String numberManager(String n){
        if (n.length() == 10){
            return n;
        }
        else if (n.length() > 10){
            return n.substring(n.length() - 10);
        }
        else {
            return null;
        }
    }
}
