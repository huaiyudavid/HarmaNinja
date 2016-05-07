package com.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.adapter.MyFragPagerAdapter;
import com.fragment.FragAwareness;
import com.fragment.FragCalibrate;
import com.fragment.FragEQCustom;
import com.fragment.FragEQSetting;
import com.fragment.FragSetting;
import com.widget.CustomViewPager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mainVp)
    public CustomViewPager mainVp;
    @Bind(R.id.awareBtn)
    TextView awareBtn;
    @Bind(R.id.EQBtn)
    TextView EQBtn;
    @Bind(R.id.caliBtn)
    TextView caliBtn;
    @Bind(R.id.SettingBtn)
    TextView SettingBtn;
    //    @Bind(R.id.eqCustom)
    public FragEQCustom frgEQCstm;
    @Bind(R.id.container)
    FrameLayout container;

    private ArrayList<Fragment> fragments;
    public FragAwareness frgAware;
    public FragEQSetting frgEQ;

    public FragSetting frgSetting;
    public FragCalibrate frgCal;
    MyFragPagerAdapter vpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragments = new ArrayList<Fragment>();
        fragments.add(frgAware = new FragAwareness());
        fragments.add(frgEQ = new FragEQSetting());
        fragments.add(frgCal = new FragCalibrate());
        fragments.add(frgSetting = new FragSetting());
//        fragments.add(frgEQCstm = new FragEQCustom());
        headphoneConnListener = frgAware;
        vpAdapter = new MyFragPagerAdapter(getSupportFragmentManager(), fragments);
        mainVp.setOffscreenPageLimit(fragments.size());
        mainVp.setAdapter(vpAdapter);
        mainVp.setScanScroll(false);
        awareBtn.setOnClickListener(this);
        EQBtn.setOnClickListener(this);
        caliBtn.setOnClickListener(this);
        SettingBtn.setOnClickListener(this);
//        frgEQCstm = new FragEQCustom();
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.container, frgEQCstm).hide(frgEQCstm).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.container, frgEQCstm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.awareBtn:
                mainVp.setCurrentItem(0);
                break;
            case R.id.EQBtn:
                mainVp.setCurrentItem(1);
                break;
            case R.id.caliBtn:
                mainVp.setCurrentItem(2);
                break;
            case R.id.SettingBtn:
                mainVp.setCurrentItem(3);
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if(!frgEQCstm.isAdded())
                ;
            else{
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.container, frgEQCstm).hide(frgEQCstm).commit();
                getSupportFragmentManager().beginTransaction().remove(frgEQCstm).commit();
                return false;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
