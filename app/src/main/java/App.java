import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.rsm.safe.Constants.ConstantsVariables;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    ConstantsVariables.CHANNELID,
                    "SafeChannel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("This Safe Channel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
