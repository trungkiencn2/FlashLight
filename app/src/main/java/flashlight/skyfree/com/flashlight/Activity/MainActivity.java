package flashlight.skyfree.com.flashlight.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import flashlight.skyfree.com.flashlight.R;
import flashlight.skyfree.com.flashlight.Ultils.Utils;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isFlashOn = false;
    private ImageView mImgBigRing;
    private ImageView mImgSmallRing;
    private PulsatorLayout mPulsator;
    private ImageView mBtnTurnOn;
    private ImageView mImgLightModeSOS, mImgLightNormal, mImgLightSpeed;
    private Button mBtnSetting;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mImgBigRing.setRotation((float) (mImgBigRing.getRotation() + 0.1));
            mImgSmallRing.setRotation(-(float) (mImgBigRing.getRotation() - 0.1));
            handler.postDelayed(runnable, 10);
        }
    };
    private Runnable startSv = new Runnable() {
        @Override
        public void run() {
            mCount += mTimeSpeedRepeat;
            if (mCount < mTimeSpeedRepeat + 20 && mCount > mTimeSpeedRepeat - 20) {
                startService(new Intent(MainActivity.this, FlashServices.class));
            } else if (mCount >= mTimeSpeedRepeat*2) {
                stopService(new Intent(MainActivity.this, FlashServices.class));
                mCount = 0;
            }
            if(mTimesUp != 0){
                mCountToTimesUp += mTimeSpeedRepeat;
            }
            handler.postDelayed(startSv, mTimeSpeedRepeat);

            if(mCountToTimesUp > mTimesUp){
                handler.removeCallbacks(startSv);
                stopService(new Intent(MainActivity.this, FlashServices.class));
                handler.removeCallbacks(runnable);
                pulsator.stop();
                if(TYPE.equals(TYPE_SOS)){
                    mImgLightModeSOS.setImageResource(R.drawable.light_mode_sos_close_on);
                    mBtnTurnOn.setImageResource(R.drawable.light_mode_sos_off_big);
                }else if(TYPE.equals(TYPE_NORMAL)){
                    mImgLightNormal.setImageResource(R.drawable.light_normal_close_on);
                    mBtnTurnOn.setImageResource(R.drawable.light_normal_big_off);
                }else if(TYPE.equals(TYPE_SPEED)){
                    if(STATE_SPEED.equals(ONE_ON) || STATE_SPEED.equals(ONE_OFF)){
                        STATE_SPEED = ONE_OFF;
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_1_close_on);
                    }else if(STATE_SPEED.equals(TWO_ON) || STATE_SPEED.equals(TWO_OFF)){
                        STATE_SPEED = TWO_OFF;
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_2_close_on);
                    }else if(STATE_SPEED.equals(THREE_ON) || STATE_SPEED.equals(THREE_OFF)){
                        STATE_SPEED = THREE_OFF;
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_3_close_on);
                    }
                    mBtnTurnOn.setImageResource(R.drawable.light_normal_big_off);
                }
                isFlashOn = false;
                mCountToTimesUp = 0;
            }
        }
    };
    private Runnable timesUp = new Runnable() {
        @Override
        public void run() {
            mCountToTimesUp += 1000;
            handler.postDelayed(timesUp, 1000);

            if(mCountToTimesUp > mTimesUp && mTimesUp > 10){
                handler.removeCallbacks(timesUp);
                handler.removeCallbacks(startSv);
                stopService(new Intent(MainActivity.this, FlashServices.class));
                handler.removeCallbacks(runnable);
                pulsator.stop();

                if(TYPE.equals(TYPE_SOS)){
                    mImgLightModeSOS.setImageResource(R.drawable.light_mode_sos_close_on);
                    mBtnTurnOn.setImageResource(R.drawable.light_mode_sos_off_big);
                    STATE_SOS = SOS_OFF;
                }else if(TYPE.equals(TYPE_NORMAL)){
                    mImgLightNormal.setImageResource(R.drawable.light_normal_close_on);
                    mBtnTurnOn.setImageResource(R.drawable.light_normal_big_off);
                    STATE_NORMAL = NORMAL_OFF;
                }else if(TYPE.equals(TYPE_SPEED)){
                    if(STATE_SPEED.equals(ONE_ON) || STATE_SPEED.equals(ONE_OFF)){
                        STATE_SPEED = ONE_OFF;
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_1_close_on);
                    }else if(STATE_SPEED.equals(TWO_ON) || STATE_SPEED.equals(TWO_OFF)){
                        STATE_SPEED = TWO_OFF;
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_2_close_on);
                    }else if(STATE_SPEED.equals(THREE_ON) || STATE_SPEED.equals(THREE_OFF)){
                        STATE_SPEED = THREE_OFF;
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_3_close_on);
                    }

                    mBtnTurnOn.setImageResource(R.drawable.light_normal_big_off);
                }
                isFlashOn = false;
                mCountToTimesUp = 0;
            }
        }
    };

    private int mCount = 0;
    private int mTimeSpeedRepeat;
    private int mCountToTimesUp = 0;

    private ImageView imgBigRing;
    private ImageView imgSmallRing;
    private PulsatorLayout pulsator;
    private int mTimesUp;

    public static final String SOS_ON = "SOS_ON";
    public static final String SOS_OFF = "SOS_OFF";
    public static final String NORMAL_ON = "NORMAL_ON";
    public static final String NORMAL_OFF = "NORMAL_OFF";
    public static final String ONE_ON = "ONE_ON";
    public static final String TWO_ON = "TWO_ON";
    public static final String THREE_ON = "THREE_ON";
    public static final String ONE_OFF = "ONE_OFF";
    public static final String TWO_OFF = "TWO_OFF";
    public static final String THREE_OFF = "THREE_OFF";

    public static String STATE_SOS = SOS_OFF;
    public static String STATE_NORMAL = NORMAL_OFF;
    public static String STATE_SPEED = ONE_OFF;

    public static final String TYPE_SOS = "TYPE_SOS";
    public static final String TYPE_NORMAL = "TYPE_NORMAL";
    public static final String TYPE_SPEED = "TYPE_SPEED";
    public static String TYPE = TYPE_NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
    }

    private void initView() {
        mImgBigRing = (ImageView) findViewById(R.id.img_big_ring);
        mImgSmallRing = (ImageView) findViewById(R.id.img_small_ring);
        mPulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        mBtnTurnOn = (ImageView) findViewById(R.id.btn_turn_on);
        imgBigRing = (ImageView) findViewById(R.id.img_big_ring);
        imgSmallRing = (ImageView) findViewById(R.id.img_small_ring);
        pulsator = (PulsatorLayout) findViewById(R.id.pulsator);

        mBtnSetting = (Button) findViewById(R.id.img_setting);
        mImgLightModeSOS = (ImageView) findViewById(R.id.img_light_sos);
        mImgLightNormal = (ImageView) findViewById(R.id.img_light_normal);
        mImgLightSpeed = (ImageView) findViewById(R.id.img_light_speed);

        mBtnSetting.setOnClickListener(this);
        mImgLightModeSOS.setOnClickListener(this);
        mImgLightNormal.setOnClickListener(this);
        mImgLightSpeed.setOnClickListener(this);
        mBtnTurnOn.setOnClickListener(this);
    }

    private void addEvent(){
        TYPE = TYPE_NORMAL;
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.SETTING, MODE_PRIVATE);
        mTimesUp = sharedPreferences.getInt(Utils.TIMES_UP_TIME, 0) / 2;
        boolean mCheckAutoOn = sharedPreferences.getBoolean(Utils.AUTOMATIC_ON, true);
        if(mCheckAutoOn && mTimesUp < 10){
            mImgLightNormal.setImageResource(R.drawable.light_normal_on);
            mBtnTurnOn.setImageResource(R.drawable.light_normal_big_on);
            handler.postDelayed(runnable, 10);
            pulsator.start();
            startService(new Intent(MainActivity.this, FlashServices.class));
            isFlashOn = true;
        }else if(mCheckAutoOn && mTimesUp > 10){
            startService(new Intent(MainActivity.this, FlashServices.class));
            mImgLightModeSOS.setImageResource(R.drawable.light_mode_sos_off);
            mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_1_off);
            mImgLightNormal.setImageResource(R.drawable.light_normal_on);
            mBtnTurnOn.setImageResource(R.drawable.light_normal_big_on);
            handler.postDelayed(runnable, 10);
            pulsator.start();
            handler.postDelayed(timesUp, mTimesUp);
            isFlashOn = true;
        } else if(!mCheckAutoOn) {
            pulsator.stop();
            handler.removeCallbacks(runnable);
            mBtnTurnOn.setImageResource(R.drawable.light_normal_big_off);
            mImgLightNormal.setImageResource(R.drawable.light_normal_close_on);
            stopService(new Intent(MainActivity.this, FlashServices.class));
            isFlashOn = false;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        addEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, FlashServices.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_turn_on:
                if (!isFlashOn) {
                    SharedPreferences sharedPreferences = getSharedPreferences(Utils.SETTING, MODE_PRIVATE);
                    mTimesUp = sharedPreferences.getInt(Utils.TIMES_UP_TIME, 0);
                    mCountToTimesUp = 0;
                    mBtnTurnOn.setImageResource(R.drawable.light_normal_big_on);
                    pulsator.start();
                    handler.postDelayed(runnable, 10);
                    isFlashOn = true;
                    if (TYPE.equals(TYPE_SOS)) {
                        mTimeSpeedRepeat = 200;
                        stopService(new Intent(MainActivity.this, FlashServices.class));
                        handler.removeCallbacks(startSv);
                        handler.postDelayed(startSv, mTimeSpeedRepeat);
                        mBtnTurnOn.setImageResource(R.drawable.light_mode_sos_on_big);
                        mImgLightModeSOS.setImageResource(R.drawable.light_mode_sos_on);
                    } else if (TYPE.equals(TYPE_NORMAL)) {
                        handler.removeCallbacks(startSv);
                        startService(new Intent(MainActivity.this, FlashServices.class));
                        handler.postDelayed(timesUp, 1000);
                        mBtnTurnOn.setImageResource(R.drawable.light_normal_big_on);
                        mImgLightNormal.setImageResource(R.drawable.light_normal_on);
                    } else if (TYPE.equals(TYPE_SPEED)) {
                        mBtnTurnOn.setImageResource(R.drawable.light_normal_big_on);
                        stopService(new Intent(MainActivity.this, FlashServices.class));
                        handler.removeCallbacks(startSv);
                        if (STATE_SPEED.equals(ONE_OFF)) {
                            mTimeSpeedRepeat = 700;
                            handler.postDelayed(startSv, mTimeSpeedRepeat);
                            mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_1_on);
                            STATE_SPEED = ONE_ON;
                        } else if (STATE_SPEED.equals(TWO_OFF)) {
                            mTimeSpeedRepeat = 500;
                            handler.postDelayed(startSv, mTimeSpeedRepeat);
                            mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_2_on);
                            STATE_SPEED = TWO_ON;
                        } else if (STATE_SPEED.equals(THREE_OFF)) {
                            mTimeSpeedRepeat = 300;
                            handler.postDelayed(startSv, mTimeSpeedRepeat);
                            mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_3_on);
                            STATE_SPEED = THREE_ON;
                        }

                    }
                } else {
                    mImgLightModeSOS.setImageResource(R.drawable.light_mode_sos_off);
                    mImgLightNormal.setImageResource(R.drawable.light_normal_off);

                    if (TYPE.equals(TYPE_SOS)) {
                        mBtnTurnOn.setImageResource(R.drawable.light_mode_sos_off_big);
                    } else if (TYPE.equals(TYPE_NORMAL)) {
                        mBtnTurnOn.setImageResource(R.drawable.light_normal_big_off);
                    } else if (TYPE.equals(TYPE_SPEED)) {
                        mBtnTurnOn.setImageResource(R.drawable.light_normal_big_off);
                    }

                    if (STATE_SPEED.equals(ONE_ON)) {
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_1_off);
                        STATE_SPEED = ONE_OFF;
                    } else if (STATE_SPEED.equals(TWO_ON)) {
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_2_off);
                        STATE_SPEED = TWO_OFF;
                    } else if (STATE_SPEED.equals(THREE_ON)) {
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_3_off);
                        STATE_SPEED = THREE_OFF;
                    }
                    handler.removeCallbacks(runnable);
                    handler.removeCallbacks(startSv);
                    handler.removeCallbacks(timesUp);
                    pulsator.stop();
                    isFlashOn = false;
                    stopService(new Intent(MainActivity.this, FlashServices.class));
                    mCountToTimesUp = 0;
                }

                break;
            case R.id.img_light_sos:
                TYPE = TYPE_SOS;
                mImgLightNormal.setImageResource(R.drawable.light_normal_off);
                STATE_NORMAL = NORMAL_OFF;
                if (STATE_SPEED.equals(ONE_ON) || STATE_SPEED.equals(ONE_OFF)) {
                    mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_1_off);
                    STATE_SPEED = ONE_OFF;
                } else if (STATE_SPEED.equals(TWO_ON) || STATE_SPEED.equals(TWO_OFF)) {
                    mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_2_off);
                    STATE_SPEED = TWO_OFF;
                } else if (STATE_SPEED.equals(THREE_ON) || STATE_SPEED.equals(THREE_OFF)) {
                    mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_3_off);
                    STATE_SPEED = THREE_OFF;
                }
                if (!isFlashOn) {
                    mBtnTurnOn.setImageResource(R.drawable.light_mode_sos_off_big);
                    mImgLightModeSOS.setImageResource(R.drawable.light_mode_sos_close_on);
                    STATE_SOS = SOS_ON;
                } else {
                    stopService(new Intent(MainActivity.this, FlashServices.class));
                    handler.removeCallbacks(startSv);
                    handler.removeCallbacks(timesUp);
                    mTimeSpeedRepeat = 200;
                    handler.postDelayed(startSv, mTimeSpeedRepeat);
                    handler.postDelayed(timesUp, 1000);
                    mBtnTurnOn.setImageResource(R.drawable.light_mode_sos_on_big);
                    mImgLightModeSOS.setImageResource(R.drawable.light_mode_sos_on);
                    STATE_SOS = SOS_OFF;
                }
                break;
            case R.id.img_light_normal:
                TYPE = TYPE_NORMAL;
                mImgLightModeSOS.setImageResource(R.drawable.light_mode_sos_off);
                STATE_SOS = SOS_OFF;
                if (STATE_SPEED.equals(ONE_ON) || STATE_SPEED.equals(ONE_OFF)) {
                    mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_1_off);
                    STATE_SPEED = ONE_OFF;
                } else if (STATE_SPEED.equals(TWO_ON) || STATE_SPEED.equals(TWO_OFF)) {
                    mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_2_off);
                    STATE_SPEED = TWO_OFF;
                } else if (STATE_SPEED.equals(THREE_ON) || STATE_SPEED.equals(THREE_OFF)) {
                    mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_3_off);
                    STATE_SPEED = THREE_OFF;
                }
                if (!isFlashOn) {
                    mBtnTurnOn.setImageResource(R.drawable.light_normal_big_off);
                    mImgLightNormal.setImageResource(R.drawable.light_normal_close_on);
                    STATE_NORMAL = NORMAL_ON;
                } else {
                    handler.removeCallbacks(startSv);
                    handler.postDelayed(timesUp, 1000);
                    stopService(new Intent(MainActivity.this, FlashServices.class));
                    startService(new Intent(MainActivity.this, FlashServices.class));
                    mBtnTurnOn.setImageResource(R.drawable.light_normal_big_on);
                    mImgLightNormal.setImageResource(R.drawable.light_normal_on);
                    STATE_NORMAL = NORMAL_OFF;
                }
                break;
            case R.id.img_light_speed:
                TYPE = TYPE_SPEED;
                mImgLightModeSOS.setImageResource(R.drawable.light_mode_sos_off);
                mImgLightNormal.setImageResource(R.drawable.light_normal_off);
                STATE_SOS = SOS_OFF;
                STATE_NORMAL = NORMAL_OFF;
                if (!isFlashOn) {
                    mBtnTurnOn.setImageResource(R.drawable.light_normal_big_off);
                    if (STATE_SPEED.equals(ONE_OFF)) {
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_2_close_on);
                        STATE_SPEED = TWO_OFF;
                    } else if (STATE_SPEED.equals(TWO_OFF)) {
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_3_close_on);
                        STATE_SPEED = THREE_OFF;
                    } else if (STATE_SPEED.equals(THREE_OFF)) {
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_1_close_on);
                        STATE_SPEED = ONE_OFF;
                    }
                } else {
                    handler.postDelayed(timesUp, 1000);
                    mBtnTurnOn.setImageResource(R.drawable.light_normal_big_on);
                    if (STATE_SPEED.equals(ONE_ON) || STATE_SPEED.equals(ONE_OFF)) {
                        stopService(new Intent(MainActivity.this, FlashServices.class));
                        handler.removeCallbacks(startSv);
                        mTimeSpeedRepeat = 500;
                        handler.postDelayed(startSv, mTimeSpeedRepeat);
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_2_on);
                        STATE_SPEED = TWO_ON;
                    } else if (STATE_SPEED.equals(TWO_ON) || STATE_SPEED.equals(TWO_OFF)) {
                        stopService(new Intent(MainActivity.this, FlashServices.class));
                        handler.removeCallbacks(startSv);
                        mTimeSpeedRepeat = 300;
                        handler.postDelayed(startSv, mTimeSpeedRepeat);
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_3_on);
                        STATE_SPEED = THREE_ON;
                    } else if (STATE_SPEED.equals(THREE_ON) || STATE_SPEED.equals(THREE_OFF)) {
                        stopService(new Intent(MainActivity.this, FlashServices.class));
                        handler.removeCallbacks(startSv);
                        mTimeSpeedRepeat = 700;
                        handler.postDelayed(startSv, mTimeSpeedRepeat);
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_1_on);
                        STATE_SPEED = ONE_ON;
                    }
                }
                break;
            case R.id.img_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }


}
