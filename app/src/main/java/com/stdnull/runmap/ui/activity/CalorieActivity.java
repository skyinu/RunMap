package com.stdnull.runmap.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.stdnull.runmap.R;

/**
 * 距离转卡路里页面
 * Created by chen on 2017/2/14.
 */

public class CalorieActivity extends BaseActivity implements TextWatcher{
    private EditText mEditWeight;
    private EditText mEditDistance;
    private TextView mTvCalorie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);
        initView();
    }

    private void initView() {
        mEditDistance = (EditText) findViewById(R.id.edit_distance);
        mEditWeight = (EditText) findViewById(R.id.edit_weight);
        mTvCalorie = (TextView) findViewById(R.id.text_calorie);
        mEditWeight.addTextChangedListener(this);
        mEditDistance.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String distance = mEditDistance.getText().toString();
        String weight = mEditWeight.getText().toString();
        try{
            float dis = Float.valueOf(distance);
            float wei = Float.valueOf(weight);
            float res = dis * wei * 1.036F;
            mTvCalorie.setText(res+"");
        }catch (Exception e){

        }
    }
}
