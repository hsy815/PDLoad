package com.hsy.pdload;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.hsy.pdload.adapter.RecyclerAdapter;
import com.hsy.pdload.util.PDLSetting;
import com.hsy.pdload.view.MyRecyclerView;
import com.hsy.pdload.view.OrdinaryPDLView;
import com.hsy.pdload.view.PullDownLoadView;

import java.util.ArrayList;
import java.util.List;

import static com.hsy.pdload.R.id.pullDownLoadView;

public class MainActivity extends AppCompatActivity {

    private MyRecyclerView recyclerView;
    //    private PullDownLoadView pullDownLoadView;
    private OrdinaryPDLView ordinaryPDLView;
    private RecyclerAdapter viewAdapter;

    private int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
    }

    private void initview() {
        recyclerView = (MyRecyclerView) findViewById(R.id.recycler);
//        pullDownLoadView = (PullDownLoadView) findViewById(R.id.pullDownLoadView);
//        pullDownLoadView.setMoveDistanceTop(pullDownLoadView.MoveDistanceAll);

        //普通加载View
        ordinaryPDLView = (OrdinaryPDLView) findViewById(R.id.pullDownLoadView);

//-------------------以下为自定义内容（都有默认）---------------------------------------------------
        //头部下拉滑动距离 默认是头部View的高度、 ordinaryPDLView.MoveDistanceAll全屏、  ordinaryPDLView.MoveDistanceIn 半屏
        ordinaryPDLView.setMoveDistanceTop(ordinaryPDLView.MoveDistanceAll);
        //底部上拉滑动距离 默认是底部View的高度、 ordinaryPDLView.MoveDistanceAll全屏、  ordinaryPDLView.MoveDistanceIn 半屏
        ordinaryPDLView.setMoveDistanceBtn(ordinaryPDLView.MoveDistanceAll);
        //添加头部拖动箭头
        ordinaryPDLView.setDrawable_h(R.mipmap.down);
        //添加底部拖动箭头
        ordinaryPDLView.setDrawable_f(R.mipmap.pulls);
        //头部加载时文本View、文本内容以及属性自己设置
        ordinaryPDLView.ordinary_pdl_tv_h.setText("正在加载");
        //底部加载时文本View、文本内容以及属性自己设置
        ordinaryPDLView.ordinary_pdl_tv_f.setText("正在加载");
        //设置加载文本本内容
        PDLSetting pdlSetting = new PDLSetting("松开刷新", "松开加载", "继续向下拉", "继续向上拉");
        ordinaryPDLView.setPdlSetting(pdlSetting);
        //头部加载时旋转的图片
        ordinaryPDLView.ordinary_pdl_load_h.setImageResource(R.mipmap.loading_dian);
        //底部加载时旋转的图片
        ordinaryPDLView.ordinary_pdl_load_f.setImageResource(R.mipmap.loading);
        //旋转动画
        Animation operatingAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.ordinary_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        //添加旋转动画
        ordinaryPDLView.setOperatingAnim(operatingAnim);
//--------------------------------------------------------------------------------------------------

        //刷新、上拉加载监听
        ordinaryPDLView.setOnRefreshListener(new OrdinaryPDLView.onRefreshListener() {
            @Override
            public void onRefresh() {
                a = 1;
                timer.start();
            }

            @Override
            public void onLoadMore() {
                a = 2;
                timer.start();
            }
        });

        //RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        viewAdapter = new RecyclerAdapter(this, getData());
        recyclerView.setAdapter(viewAdapter);
    }

    private CountDownTimer timer = new CountDownTimer(3000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            switch (a) {
                case 1:
                    //停止刷新
                    ordinaryPDLView.stopRefresh();
                    break;
                case 2:
                    //停止加载
                    ordinaryPDLView.stopLoadMore();
                    break;
            }
        }
    };

    public List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            list.add("item" + i);
        }
        return list;
    }
}
