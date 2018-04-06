package flashlight.skyfree.com.flashlight.Activity;

import android.app.Service;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;

import flashlight.skyfree.com.flashlight.Ultils.FlashLight;

public class FlashServices extends Service {

    private Handler mHandler = new Handler();
    private Runnable update = new Runnable() {
        @Override
        public void run() {


        }
    };

    FlashLight flashLight =new FlashLight();
    public FlashServices() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {

        try {
            FlashLight.flashOn(getApplicationContext());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDestroy() {
        try {
            FlashLight.flashOff();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
