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
            mCount += mTimeRepeat;
            if (mCount < mTimeRepeat + 20 && mCount > mTimeRepeat - 20) {
                startService(new Intent(MainActivity.this, FlashServices.class));
            } else if (mCount >= mTimeRepeat*2) {
                stopService(new Intent(MainActivity.this, FlashServices.class));
                mCount = 0;
            }
            if(mTimesUp != 0){
                mCountToTimesUp += mTimeRepeat;
            }
            handler.postDelayed(startSv, mTimeRepeat);

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

            if(mCountToTimesUp > mTimesUp){
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
    private int mTimeRepeat;
    private int mCountToTimesUp = 0;

    private ImageView imgBigRing;
    private ImageView imgSmallRing;
    private PulsatorLayout pulsator;

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
    
    private int mTimesUp;

    private void addEvent(){
        SharedPreferences sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        if(sharedPreferences.getBoolean(Utils.AUTOMATIC_ON, true)){
            startService(new Intent(MainActivity.this, FlashServices.class));
            isFlashOn = true;
        }else {
            stopService(new Intent(MainActivity.this, FlashServices.class));
            isFlashOn = false;
        }
        mTimesUp = sharedPreferences.getInt(Utils.TIMES_UP_TIME, 0);

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
                    mBtnTurnOn.setImageResource(R.drawable.light_normal_big_on);
                    pulsator.start();
                    handler.postDelayed(runnable, 10);
                    isFlashOn = true;
                    if (TYPE.equals(TYPE_SOS)) {
                        mTimeRepeat = 200;
                        stopService(new Intent(MainActivity.this, FlashServices.class));
                        handler.removeCallbacks(startSv);
                        handler.postDelayed(startSv, mTimeRepeat);
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
                            mTimeRepeat = 700;
                            handler.postDelayed(startSv, mTimeRepeat);
//                            handler.postDelayed(timesUp, 1000);
                            mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_1_on);
                            STATE_SPEED = ONE_ON;
                        } else if (STATE_SPEED.equals(TWO_OFF)) {
                            mTimeRepeat = 500;
                            handler.postDelayed(startSv, mTimeRepeat);
//                            handler.postDelayed(timesUp, 1000);
                            mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_2_on);
                            STATE_SPEED = TWO_ON;
                        } else if (STATE_SPEED.equals(THREE_OFF)) {
                            mTimeRepeat = 300;
                            handler.postDelayed(startSv, mTimeRepeat);
//                            handler.postDelayed(timesUp, 1000);
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
                    mTimeRepeat = 200;
                    handler.postDelayed(startSv, mTimeRepeat);
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
                    mBtnTurnOn.setImageResource(R.drawable.light_normal_big_on);
                    if (STATE_SPEED.equals(ONE_ON) || STATE_SPEED.equals(ONE_OFF)) {
                        stopService(new Intent(MainActivity.this, FlashServices.class));
                        handler.removeCallbacks(startSv);
                        mTimeRepeat = 500;
                        handler.postDelayed(startSv, mTimeRepeat);
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_2_on);
                        STATE_SPEED = TWO_ON;
                    } else if (STATE_SPEED.equals(TWO_ON) || STATE_SPEED.equals(TWO_OFF)) {
                        stopService(new Intent(MainActivity.this, FlashServices.class));
                        handler.removeCallbacks(startSv);
                        mTimeRepeat = 300;
                        handler.postDelayed(startSv, mTimeRepeat);
                        mImgLightSpeed.setImageResource(R.drawable.flash_light_speed_3_on);
                        STATE_SPEED = THREE_ON;
                    } else if (STATE_SPEED.equals(THREE_ON) || STATE_SPEED.equals(THREE_OFF)) {
                        stopService(new Intent(MainActivity.this, FlashServices.class));
                        handler.removeCallbacks(startSv);
                        mTimeRepeat = 700;
                        handler.postDelayed(startSv, mTimeRepeat);
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
