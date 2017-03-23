package com.home.quhong.quhong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.quhong.quhong.TV.network.RetrofitHelper;
import com.home.quhong.quhong.TV.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class TestActivity extends AppCompatActivity  {
    private static final String TAG = "TestActivity";
    private CompositeSubscription mSubscription = new CompositeSubscription();
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.text_ll_contains)
    LinearLayout textLlContains;
    private LayoutInflater inflater;
    private List<TestCategory.CategoryBean> mCBList = new ArrayList<>();
    private HashMap<String, String> map = new LinkedHashMap<>();
    private List<String> mList = new ArrayList<>();
    private int llCount;
    private View mTextView;
    List<String> list;
    public TestActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initGetData();
        list = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            String str = "标签" + i;
            list.add(str);
        }
    }

    private void traverseData() {
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry next = iterator.next();
            Object key = next.getKey();
            Object value = next.getValue();
            mList.add(String.valueOf(value));
            Log.d(TAG, "analyzeData: "+"----->"+key + "----->"+value);
        }
    }

    private void analyzeData() {
        if (mCBList != null) {
            for (int i = 0; i < mCBList.size(); i++) {
                String k = mCBList.get(i).getK();
                String v = mCBList.get(i).getV();
                map.put(k,v);
            }
            calData();
            traverseData();
        }
    }

    private void calData() {
        if (map.size()%5 != 0) {
            llCount = map.size() / 5 + 1;
        }else{
            llCount = map.size() / 5;
        }
    }

    private void initGetData() {
        Observable<TestCategory> seriesUrlAgain = RetrofitHelper.getTestApi().getTestFiltrateDetail("drama","remance","KR","hot",null);
        mSubscription.add(seriesUrlAgain.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TestCategory>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(TestActivity.this, "完成", Toast.LENGTH_SHORT).show();
                        analyzeData();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TestCategory rs) {
                        mCBList = rs.getCategory();
                    }
                }));
    }

    @OnClick({R.id.btn1, R.id.btn2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                textLlContains.removeAllViews();
                inflater = LayoutInflater.from(this);
                if(map.size() > 0 && llCount >0) {
                    for (int i = 0; i < llCount; i++) {
                        LinearLayout linearLayout = new LinearLayout(this);
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        for (int j = 0; j < 5; j++) {
                            if (mList.size() > (i * 5 + j)) {
                                mTextView = inflater.inflate(R.layout.test_text_show, null);
                                ((TextView) mTextView.findViewById(R.id.keyword)).setText(mList.get(i * 5 + j));
                                View view11 = inflater.inflate(R.layout.test_splite_line, null);
                                linearLayout.addView(mTextView);
                                linearLayout.addView(view11);
                                view.setTag(i * 5 + j,mTextView);
                            }
                        }
                        textLlContains.addView(linearLayout);
                    }
                }
                break;
            case R.id.btn2:
                textLlContains.removeAllViews();
                initMarksView();
                break;
        }
    }
    private void initMarksView() {
        for (int i = 0; i < list.size(); i++) {
            View view = View.inflate(this, R.layout.test_text_show, null);
            TextView tv = (TextView) view.findViewById(R.id.keyword);
            tv.setText(list.get(i));
            tv.setTag(i);
            view.setTag(false);
            // 设置view的点击事件，与onClick中的View一致
            //否则需要在onClick中，去findViewById，找出设置点击事件的控件进行操作
            //若不如此，则无法触发点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    TextView tv = (TextView) v.findViewById(R.id.keyword);
                    Log.i("dxl", "TextView click");
                    if ((Boolean) v.getTag()) {
                        v.setTag(false);
                        tv.setEnabled(false);
                        Toast.makeText(TestActivity.this, "你取消了选择标签" + tv.getTag(), Toast.LENGTH_SHORT).show();
                    } else {
                        v.setTag(true);
                        tv.setEnabled(true);
                        Toast.makeText(TestActivity.this, "你选择了标签" + tv.getTag(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            textLlContains.addView(view);
        }
    }

}
