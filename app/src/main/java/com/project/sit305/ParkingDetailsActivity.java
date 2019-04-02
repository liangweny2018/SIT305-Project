package com.project.sit305;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.project.sit305.bean.ParkingDataBean;
import com.project.sit305.utils.ImageLoaderUtils;
import com.project.sit305.utils.NormalUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * DESC:
 * Date: 2019/4/2  11:16 AM
 * Time: 下午4:29
 * author: liang
 */
public class ParkingDetailsActivity extends SwipeBackActivity {

    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";

    static final String ROUTE_PLAN_NODE = "routePlanNode";

    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    private String mSDCardPath = null;

    private static final int authBaseRequestCode = 1;

    private boolean hasInitSuccess = false;

    private BNRoutePlanNode mStartNode = null;


    private Button navigation;


    private double longtitude, latitude;

    private TextView detailsparking,
            detailsdistance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details_layout);

        ImageLoaderUtils.display(getApplicationContext(), (ImageView) findViewById(R.id.ivImage), "http://cdn-img.q-media.cn/tes/whkm/i/5c7f3b329972f40cec698637.jpg?auth_key=4636195200-0-0-e637817a9531bbbcbe4bc074f3e5681c&x-oss-process=image/resize,m_lfit,h_400,w_400");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.back_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        navigation = (Button) findViewById(R.id.navigation_btn);
        detailsparking = (TextView) findViewById(R.id.details_title);
        detailsdistance = (TextView) findViewById(R.id.details_distance);

        Intent intent = getIntent();
        if (intent != null) {
            ParkingDataBean parkingDataBean = (ParkingDataBean) intent.getSerializableExtra("parkingDetails");
            detailsparking.setText(parkingDataBean.getName());
            detailsdistance.setText(parkingDataBean.getDistance() + " areas left");
            longtitude = parkingDataBean.getLongitude();
            latitude = parkingDataBean.getLatitude();
            Log.e("ParkingDataBean", parkingDataBean.toString());
        }

        initListener();
        if (initDirs()) {
            initNavi();
        }

    }

    private void initListener() {
        if (navigation != null) {
            navigation.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
                        routeplanToNavi(BNRoutePlanNode.CoordinateType.WGS84);
                    } else {
                        Log.e("WGS84", "WGS84: " + "失败");
                    }
                }

            });
        }
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void initNavi() {
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }

        BaiduNaviManagerFactory.getBaiduNaviManager().init(this,
                mSDCardPath, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        String result;
                        if (0 == status) {
                            result = "key校验成功!";
                        } else {
                            result = "key校验失败, " + msg;
                            Toast.makeText(ParkingDetailsActivity.this, "init failed", Toast.LENGTH_LONG).show();

                        }
//                        Toast.makeText(ParkingDetailsActivity.this, result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void initStart() {
//                        Toast.makeText(ParkingDetailsActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void initSuccess() {
//                        Toast.makeText(ParkingDetailsActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                        hasInitSuccess = true;
                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed() {
//                        Toast.makeText(ParkingDetailsActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    // 外置tts时需要实现的接口回调
    private IBNTTSManager.IBNOuterTTSPlayerCallback mTTSCallback = new IBNTTSManager.IBNOuterTTSPlayerCallback() {

        @Override
        public int getTTSState() {
//            /** 播放器空闲 */
//            int PLAYER_STATE_IDLE = 1;
//            /** 播放器正在播报 */
//            int PLAYER_STATE_PLAYING = 2;
            return PLAYER_STATE_IDLE;
        }

        @Override
        public int playTTSText(String text, String s1, int i, String s2) {
            Log.e("BNSDKDemo", "playTTSText:" + text);
            return 0;
        }

        @Override
        public void stopTTS() {
            Log.e("BNSDKDemo", "stopTTS");
        }
    };

    private void initTTS() {
        // 使用内置TTS
        BaiduNaviManagerFactory.getTTSManager().initTTS(getApplicationContext(),
                getSdcardDir(), APP_FOLDER_NAME, NormalUtils.getTTSAppID());

        // 不使用内置TTS
//         BaiduNaviManagerFactory.getTTSManager().initTTS(mTTSCallback);

        // 注册同步内置tts状态回调
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayStart");
                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayEnd");
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayError");
                    }
                }
        );

        // 注册内置tts 异步状态消息
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.e("BNSDKDemo", "ttsHandler.msg.what=" + msg.what);
                    }
                }
        );
    }


    private void routeplanToNavi(final int coType) {
        if (!hasInitSuccess) {
//            Toast.makeText(ParkingDetailsActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
            Toast.makeText(ParkingDetailsActivity.this, "not init yet!", Toast.LENGTH_SHORT).show();
        }

        BNRoutePlanNode sNode = new BNRoutePlanNode(116.30142, 40.05087, "百度大厦", "百度大厦", coType);
        BNRoutePlanNode eNode = new BNRoutePlanNode(116.39750, 39.90882, "北京天安门", "北京天安门", coType);
        switch (coType) {
            case BNRoutePlanNode.CoordinateType.WGS84: {
//                sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436, "百度大厦", "百度大厦", coType);
//                eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085, "北京天安门", "北京天安门", coType);


                sNode = new BNRoutePlanNode(114.324078, 30.461584, "快媒数字", "快媒数字", coType);
                eNode = new BNRoutePlanNode(longtitude, latitude, "世茂林屿岸", "世茂林屿岸", coType);

                break;
            }
            default:
                break;
        }

        mStartNode = sNode;

        List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
        list.add(sNode);
        list.add(eNode);

        BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
//                                Toast.makeText(ParkingDetailsActivity.this, "算路开始", Toast.LENGTH_SHORT)
//                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
//                                Toast.makeText(ParkingDetailsActivity.this, "算路成功", Toast.LENGTH_SHORT)
//                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                Toast.makeText(ParkingDetailsActivity.this, "Navigation failed", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
//                                Toast.makeText(ParkingDetailsActivity.this, "算路成功准备进入导航", Toast.LENGTH_SHORT)
//                                        .show();
                                Intent intent = new Intent(ParkingDetailsActivity.this,
                                        DemoGuideActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(ROUTE_PLAN_NODE, mStartNode);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            default:
                                // nothing
                                break;
                        }
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    Toast.makeText(ParkingDetailsActivity.this, "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            initNavi();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
