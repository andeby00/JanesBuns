package dk.au.mad22spring.janesbuns.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.models.CreamBun;
import dk.au.mad22spring.janesbuns.models.Order;
import dk.au.mad22spring.janesbuns.models.User;

// Based on DemoServices 1.3 from https://brightspace.au.dk/d2l/le/lessons/54767/lessons/832662
// Inspiration taken from https://stackoverflow.com/questions/6391870/how-exactly-to-use-notification-builder
// And error fix from https://stackoverflow.com/questions/33669156/nullpointer-exception-in-service-onstartcommand-after-apk-being-closed-how-to-d
public class OrderService extends Service {
    public static final String SERVICE_CHANNEL = "serviceChannel";
    public static final int NOTIFICATION_ID = 72826;

    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder builder;
    FirebaseFirestore db;
    boolean started = false;

    public OrderService() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //check for Android version - whether we need to create a notification channel (from Android 0 and up, API 26)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(SERVICE_CHANNEL, "OrderService", NotificationManager.IMPORTANCE_LOW);
            NotificationManager notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notiManager.createNotificationChannel(channel);
        }

        notificationManager = NotificationManagerCompat.from(this);

        builder = new NotificationCompat.Builder(this, SERVICE_CHANNEL)
                .setContentTitle("New Order received")
                .setContentText("")
                .setSmallIcon(R.drawable.creambun_placeholder);


        doNotificationSleeper();
        return START_REDELIVER_INTENT;
    }

    private void doNotificationSleeper() {
        if(!started) {
            started = true;
            listenToOrders();
            //doRecursiveWork();
        }
    }

    private void listenToOrders() {
        db.collection("orders").addSnapshotListener((value, error) -> {
            if (value != null) {
                Order tempOrder = value.getDocumentChanges().get(0).getDocument().toObject(Order.class);
                if(tempOrder.userUid != null) {
                    db.collection("users").document(tempOrder.userUid).get().addOnSuccessListener(documentSnapshot -> {
                        User tempUser = documentSnapshot.toObject(User.class);
                        StringBuilder tempString = new StringBuilder();

                        for (CreamBun creamBun : tempOrder.creamBuns) {
                            tempString.append(creamBun.amount).append("x ").append(creamBun.name).append(", ");
                        }
                        tempString.deleteCharAt(tempString.lastIndexOf(","));

                        if (tempUser != null)
                            builder.setContentTitle(tempUser.fullName + " has made an order")
                                    .setContentText(tempString);
                        notificationManager.notify(NOTIFICATION_ID, builder.build());
                    });
                }
            }
        });

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
