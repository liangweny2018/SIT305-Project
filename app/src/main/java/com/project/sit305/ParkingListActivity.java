package com.project.sit305;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.project.sit305.adapter.ParkingListAdapter;
import com.project.sit305.bean.ParkingDataBean;
import com.project.sit305.utils.DistanceSort;
import com.project.sit305.utils.PriceSort;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DESC:
 * Date: 2019/4/2  10:21 AM
 * Time: 下午4:29
 * author: liang
 */
public class ParkingListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView       mRecyclerView;
    DrawerLayout mDrawerLayout;
    private LinearLayoutManager mLayoutManager;
    private ParkingListAdapter  mAdapter;
    int sort = 0;       //0代表正常  1代表价格排序  2代表距离排序

    List<ParkingDataBean> parkingDataBeanList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowHomeEnabled(false);


//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
//                R.string.drawer_close);
//        mDrawerToggle.syncState();
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
//        setupDrawerContent(mNavigationView);

        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.primary,
                R.color.primary_dark, R.color.primary_light,
                R.color.accent);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ParkingListAdapter(getApplicationContext());
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();
    }

    private ParkingListAdapter.OnItemClickListener mOnItemClickListener = new ParkingListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (parkingDataBeanList.size() <= 0) {
                return;
            }
            ParkingDataBean news = mAdapter.getItem(position);
            Intent intent = new Intent(ParkingListActivity.this, ParkingDetailsActivity.class);
            intent.putExtra("news", news);

            View transitionView = view.findViewById(R.id.ivNews);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(ParkingListActivity.this,
                            transitionView, getString(R.string.transition_news_img));

            ActivityCompat.startActivity(ParkingListActivity.this, intent, options.toBundle());
        }
    };




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_normal_1:
                sort = 1;
                getData();
                return true;
            case R.id.option_normal_2:
                sort = 2;
                getData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void getData() {
        parkingDataBeanList.clear();

        ParkingDataBean parkingDataBean1 = new ParkingDataBean();
        parkingDataBean1.setName("华科停车场");
        parkingDataBean1.setPrice(2);
        parkingDataBean1.setDistance(5);
        parkingDataBean1.setLongitude(114.419826);
        parkingDataBean1.setLatitude(30.518754);
        parkingDataBeanList.add(parkingDataBean1);

        ParkingDataBean parkingDataBean2 = new ParkingDataBean();
        parkingDataBean2.setName("武汉大学");
        parkingDataBean2.setPrice(3);
        parkingDataBean2.setDistance(2);
        parkingDataBean2.setLongitude(114.371605);
        parkingDataBean2.setLatitude(30.544165);
        parkingDataBeanList.add(parkingDataBean2);

        ParkingDataBean parkingDataBean3 = new ParkingDataBean();
        parkingDataBean3.setName("光谷广场");
        parkingDataBean3.setPrice(5);
        parkingDataBean3.setDistance(10);

        parkingDataBean3.setLongitude(114.405927);
        parkingDataBean3.setLatitude(30.512215);
        parkingDataBeanList.add(parkingDataBean3);

        ParkingDataBean parkingDataBean4 = new ParkingDataBean();
        parkingDataBean4.setName("街道口");
        parkingDataBean4.setPrice(4);
        parkingDataBean4.setDistance(15);
        parkingDataBean4.setLongitude(114.357185);
        parkingDataBean4.setLatitude(30.534332);
        parkingDataBeanList.add(parkingDataBean4);

        ParkingDataBean parkingDataBean5 = new ParkingDataBean();
        parkingDataBean5.setName("汉街");
        parkingDataBean5.setPrice(1);
        parkingDataBean5.setDistance(4);
        parkingDataBean5.setLongitude(114.353486);
        parkingDataBean5.setLatitude(30.559531);
        parkingDataBeanList.add(parkingDataBean5);

        if (sort == 1) {
            Collections.sort(parkingDataBeanList, new PriceSort());
        } else if (sort == 2) {
            Collections.sort(parkingDataBeanList, new DistanceSort());

        }

        mAdapter.setmDate(parkingDataBeanList);
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshWidget.setRefreshing(true);
        sort = 0;
        getData();

    }
}
