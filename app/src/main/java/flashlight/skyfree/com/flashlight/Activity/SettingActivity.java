package flashlight.skyfree.com.flashlight.Activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import flashlight.skyfree.com.flashlight.R;
import flashlight.skyfree.com.flashlight.Ultils.Utils;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImgBack;
    private LinearLayout mLinearOn, mLinearOff;
    private Switch mSwOn;
    private TextView mTvTimesUp;

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();

        sharedPreferences = getSharedPreferences(Utils.SETTING, MODE_PRIVATE);
        mEditor  = sharedPreferences.edit();
    }

    private void initView(){
        mImgBack = (ImageView) findViewById(R.id.img_back_setting);
        mLinearOn = (LinearLayout) findViewById(R.id.linear_automatic_on);
        mLinearOff = (LinearLayout) findViewById(R.id.linear_automatic_off);
        mSwOn = (Switch) findViewById(R.id.sw_on);
        mTvTimesUp = (TextView) findViewById(R.id.tv_times_up);
        mImgBack.setOnClickListener(this);
        mLinearOn.setOnClickListener(this);
        mLinearOff.setOnClickListener(this);
    }

    private void addEvent(){
        mTvTimesUp.setText(sharedPreferences.getString(Utils.TIMES_UP_TEXT, getString(R.string.never)));
        mSwOn.setChecked(sharedPreferences.getBoolean(Utils.AUTOMATIC_ON, true));
    }

    @Override
    protected void onResume() {
        super.onResume();
        addEvent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back_setting:
                finish();
                break;
            case R.id.linear_automatic_on:
                break;
            case R.id.linear_automatic_off:
                showAlertDialog();
                break;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mSwOn.isChecked()){
            mEditor.putBoolean(Utils.AUTOMATIC_ON, true);
            mEditor.apply();
        }else {
            mEditor.putBoolean(Utils.AUTOMATIC_ON, false);
            mEditor.apply();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_choose_times_up, null);
        dialogBuilder.setView(dialogView);

        RadioGroup mRadioGroup = (RadioGroup) dialogView.findViewById(R.id.radio_gr_times_up);
        RadioButton mRadioBtn10s = (RadioButton) dialogView.findViewById(R.id.radio_10s);
        RadioButton mRadioBtn20s = (RadioButton) dialogView.findViewById(R.id.radio_20s);
        RadioButton mRadioBtn1m = (RadioButton) dialogView.findViewById(R.id.radio_1m);
        RadioButton mRadioBtn3m = (RadioButton) dialogView.findViewById(R.id.radio_3m);
        RadioButton mRadioBtn5m = (RadioButton) dialogView.findViewById(R.id.radio_5m);
        RadioButton mRadioBtn10m = (RadioButton) dialogView.findViewById(R.id.radio_10m);
        RadioButton mRadioBtn30m = (RadioButton) dialogView.findViewById(R.id.radio_30m);
        RadioButton mRadioBtnNever = (RadioButton) dialogView.findViewById(R.id.radio_never);
        TextView mTvCancel = (TextView) dialogView.findViewById(R.id.tv_cancel_dialog);
        TextView mTvOk = (TextView) dialogView.findViewById(R.id.tv_ok_dialog);

        final AlertDialog alertStartDialog = dialogBuilder.create();
        alertStartDialog.show();

        if(sharedPreferences.getInt(Utils.TIMES_UP_TIME, 0) == 10000){
            mRadioGroup.check(R.id.radio_10s);
        }else if(sharedPreferences.getInt(Utils.TIMES_UP_TIME, 0) == 20000){
            mRadioGroup.check(R.id.radio_20s);
        }else if(sharedPreferences.getInt(Utils.TIMES_UP_TIME, 0) == 60000){
            mRadioGroup.check(R.id.radio_1m);
        }else if(sharedPreferences.getInt(Utils.TIMES_UP_TIME, 0) == 60000 * 3){
            mRadioGroup.check(R.id.radio_3m);
        }else if(sharedPreferences.getInt(Utils.TIMES_UP_TIME, 0) == 60000 * 5){
            mRadioGroup.check(R.id.radio_5m);
        }else if(sharedPreferences.getInt(Utils.TIMES_UP_TIME, 0) == 60000 * 10){
            mRadioGroup.check(R.id.radio_10m);
        }else if(sharedPreferences.getInt(Utils.TIMES_UP_TIME, 0) == 60000 * 30){
            mRadioGroup.check(R.id.radio_30m);
        }

        mRadioBtn10s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mEditor.putInt(Utils.TIMES_UP_TIME, 10000);
                    mEditor.putString(Utils.TIMES_UP_TEXT, getString(R.string.ten_seconds));
                }
            }
        });

        mRadioBtn20s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mEditor.putInt(Utils.TIMES_UP_TIME, 20000);
                    mEditor.putString(Utils.TIMES_UP_TEXT, getString(R.string.twenty_seconds));
                }
            }
        });

        mRadioBtn1m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mEditor.putInt(Utils.TIMES_UP_TIME, 60000);
                    mEditor.putString(Utils.TIMES_UP_TEXT, getString(R.string.one_minute));
                }
            }
        });

        mRadioBtn3m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mEditor.putInt(Utils.TIMES_UP_TIME, 60000 * 3);
                    mEditor.putString(Utils.TIMES_UP_TEXT, getString(R.string.three_minute));
                }
            }
        });

        mRadioBtn5m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mEditor.putInt(Utils.TIMES_UP_TIME, 60000 * 5);
                    mEditor.putString(Utils.TIMES_UP_TEXT, getString(R.string.five_minute));
                }
            }
        });

        mRadioBtn10m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mEditor.putInt(Utils.TIMES_UP_TIME, 60000 * 10);
                    mEditor.putString(Utils.TIMES_UP_TEXT, getString(R.string.ten_minute));
                }
            }
        });

        mRadioBtn30m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mEditor.putInt(Utils.TIMES_UP_TIME, 60000 * 30);
                    mEditor.putString(Utils.TIMES_UP_TEXT, getString(R.string.thirty_minute));
                }
            }
        });

        mRadioBtnNever.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mEditor.putInt(Utils.TIMES_UP_TIME, 0);
                    mEditor.putString(Utils.TIMES_UP_TEXT, getString(R.string.never));
                }
            }
        });

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertStartDialog.cancel();
            }
        });

        final SharedPreferences.Editor finalMEditor8 = mEditor;
        mTvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.apply();
                mTvTimesUp.setText(sharedPreferences.getString(Utils.TIMES_UP_TEXT, ""));
                alertStartDialog.cancel();
            }
        });
    }
}
