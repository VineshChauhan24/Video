package com.home.quhong.quhong;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.home.quhong.quhong.Local.fragments.LocalFragment;
import com.home.quhong.quhong.My.fragments.MyFragment;
import com.home.quhong.quhong.TV.FiltrateActivity;
import com.home.quhong.quhong.TV.FloatButtonActivity;
import com.home.quhong.quhong.TV.fragments.TVFragment;
import com.home.quhong.quhong.TV.fragments.VideoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuHong extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {


    private TVFragment mTVFragment;
    private VideoFragment mVideoFragment;
    private LocalFragment mLocalFragment;
    private MyFragment mMyFragment;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qu_hong);
        ButterKnife.bind(this);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        Fragment f = manager.findFragmentByTag("TV");
        radioGroup = (RadioGroup) findViewById(R.id.home_rg);
        if (radioGroup != null) {
            radioGroup.setOnCheckedChangeListener(this);
        }
        mTVFragment = new TVFragment();
        mVideoFragment = new VideoFragment();
        mLocalFragment = new LocalFragment();
        mMyFragment = new MyFragment();
        radioGroup.check(R.id.home_rb_tv);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        switch (checkedId) {
            case R.id.home_rb_tv:
                ft.replace(R.id.home_fragment_contain, mTVFragment);
                break;
            case R.id.home_rb_video:
                ft.replace(R.id.home_fragment_contain, mVideoFragment);
                break;
            case R.id.home_rb_local:
                ft.replace(R.id.home_fragment_contain, mLocalFragment);
                break;
            case R.id.home_rb_my:
                ft.replace(R.id.home_fragment_contain, mMyFragment);
                break;
        }
        ft.commit();
    }

     @OnClick(R.id.floatingActionButton)
    public void onClick() {
        Intent intent = new Intent(this, FiltrateActivity.class);
        startActivity(intent);
    }
}
