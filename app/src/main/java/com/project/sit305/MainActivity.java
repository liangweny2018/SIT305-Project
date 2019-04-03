package com.project.sit305;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mzule.fantasyslide.SideBar;
import com.github.mzule.fantasyslide.SimpleFantasyListener;
import com.github.mzule.fantasyslide.Transformer;
import com.project.sit305.adapter.ParkingListAdapter;
import com.project.sit305.bean.ParkingDataBean;
import com.project.sit305.utils.DistanceSort;
import com.project.sit305.utils.PriceSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private DrawerLayout drawerLayout;


    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView       mRecyclerView;
    DrawerLayout mDrawerLayout;
    private LinearLayoutManager mLayoutManager;
    private ParkingListAdapter  mAdapter;
    int sort = 0;       //0代表正常  1代表价格排序  2代表距离排序

    List<ParkingDataBean> parkingDataBeanList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final DrawerArrowDrawable indicator = new DrawerArrowDrawable(this);
        indicator.setColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(indicator);

        setTransformer();
        // setListener();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (((ViewGroup) drawerView).getChildAt(1).getId() == R.id.leftSideBar) {
                    indicator.setProgress(slideOffset);
                }
            }
        });
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
            Intent intent = new Intent(MainActivity.this, ParkingDetailsActivity.class);
            intent.putExtra("news", news);
            intent.putExtra("parkingDetails", parkingDataBeanList.get(position));


            View transitionView = view.findViewById(R.id.ivNews);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                            transitionView, getString(R.string.transition_news_img));

            ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.option_normal_1:
//                sort = 1;
//                getData();
//                return true;
//            case R.id.option_normal_2:
//                sort = 2;
//                getData();
//                return true;
//            default_pic:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }

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


    private void setListener() {
        final TextView tipView = (TextView) findViewById(R.id.tipView);
        SideBar leftSideBar = (SideBar) findViewById(R.id.leftSideBar);
        leftSideBar.setFantasyListener(new SimpleFantasyListener() {
            @Override
            public boolean onHover(@Nullable View view, int index) {
                tipView.setVisibility(View.VISIBLE);
                if (view instanceof TextView) {
                    tipView.setText(String.format("%s at %d", ((TextView) view).getText().toString(), index));
                } else if (view != null && view.getId() == R.id.userInfo) {
                    tipView.setText(String.format("个人中心 at %d", index));
                } else {
                    tipView.setText(null);
                }
                return false;

            }

            @Override
            public boolean onSelect(View view, int index) {
                tipView.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, String.format("%d selected", index), Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onCancel() {
                tipView.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setTransformer() {
        final float spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
//        SideBar rightSideBar = (SideBar) findViewById(R.id.rightSideBar);
//        rightSideBar.setTransformer(new Transformer() {
//            private View lastHoverView;
//
//            @Override
//            public void apply(ViewGroup sideBar, View itemView, float touchY, float slideOffset, boolean isLeft) {
//                boolean hovered = itemView.isPressed();
//                if (hovered && lastHoverView != itemView) {
//                    animateIn(itemView);
//                    animateOut(lastHoverView);
//                    lastHoverView = itemView;
//                }
//            }
//
//            private void animateOut(View view) {
//                if (view == null) {
//                    return;
//                }
//                ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", -spacing, 0);
//                translationX.setDuration(200);
//                translationX.start();
//            }
//
//            private void animateIn(View view) {
//                ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", 0, -spacing);
//                translationX.setDuration(200);
//                translationX.start();
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                drawerLayout.closeDrawer(GravityCompat.START);
//            } else {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//        }

        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                break;
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
        return true;
    }

    public void onClick(View view) {
        if (view instanceof TextView) {
            String title = ((TextView) view).getText().toString();
            if (title.startsWith("about")) {
//                Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            } else if (title.equals("feedback")) {
//                startActivity(UniversalActivity.newIntent(this, title));
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        } else if (view.getId() == R.id.userInfo) {
//            startActivity(UniversalActivity.newIntent(this, "个人中心"));
        }
    }
}
