package flashlight.skyfree.com.flashlight.Ultils;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.os.Build;
import android.os.Handler;

import com.noob.lumberjack.LogLevel;
import com.noob.noobcameraflash.managers.NoobCameraManager;

/**
 * <!-- 手电筒 -->
 * Call requires API level 5
 * <uses-permission android:name="android.permission.FLASHLIGHT"/>
 * <uses-permission android:name="android.permission.CAMERA"/>
 *
 * @author MaTianyu
 * @date 2014-11-04
 */
public class FlashLight {

    public static void flashOn(Context context) throws CameraAccessException {
        NoobCameraManager.getInstance().init(context, LogLevel.Verbose);
        NoobCameraManager.getInstance().turnOnFlash();
    }


    public static void flashOff() throws CameraAccessException {
        NoobCameraManager.getInstance().turnOffFlash();
    }
}
