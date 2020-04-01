package com.rsm.safe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.rsm.safe.Bean.TrustedContactModel;
import com.rsm.safe.Constants.ConstantsVariables;
import com.rsm.safe.Database.SafeDatabase;

import java.util.List;

public class NotificationManager extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SafeDatabase safeDatabase = new SafeDatabase(context);

        List<TrustedContactModel> trustedContacts = safeDatabase.getTrustedContacts();
        if (trustedContacts.size() > 0){
            SmsManager smsManager = SmsManager.getDefault();
            for (TrustedContactModel contactModel : trustedContacts){
                String number = String.valueOf(contactModel.getNumber());
                smsManager.sendTextMessage(number,null, ConstantsVariables.MESSAGE, null, null);
            }
        }
    }
}
