package com.project.sit305;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * DESC:
 * Date: 2019/4/2  9:03 PM
 * Time: 下午4:29
 * author: liang
 */
public class AboutActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
