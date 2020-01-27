package com.chathead;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.chathead.fragments.Home;
import com.google.android.material.tabs.TabLayout;
import com.nex3z.notificationbadge.NotificationBadge;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

public class MainActivity extends AppCompatActivity {

    private BubblesManager bubblesManager;
    private NotificationBadge mBadge;

    private int MY_PERMISSION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdd = (Button) findViewById(R.id.btnAddBubble);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewBubble();
            }
        });

        //Check permission
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, MY_PERMISSION);
            }
        } else {
            Intent intent = new Intent(MainActivity.this, Service.class);
            startService(intent);
        }

        initBubble();


    }

    private void initBubble() {
        bubblesManager = new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.bubble_remove)
                .setInitializationCallback(new OnInitializedCallback() {
                    @Override
                    public void onInitialized() {
                        addNewBubble();
                    }
                }).build();
        bubblesManager.initialize();
    }

    private void addNewBubble() {
        final BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.bubble_layout, null);
        mBadge = (NotificationBadge) bubbleView.findViewById(R.id.count);
        mBadge.setNumber(88);

        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
                Toast.makeText(MainActivity.this, "Removed", Toast.LENGTH_SHORT).show();
            }
        });

        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {
            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                //Show Hide content layout
                toggleContentVisibility(bubbleView.findViewById(R.id.layout_content));
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = bubbleView.findViewById(R.id.myviewpager);

        // Create an adapter that knows which fragment should be shown on each page
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getApplicationContext(), getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout =  bubbleView.findViewById(R.id.mytablayout);
        tabLayout.setupWithViewPager(viewPager);

        bubbleView.setShouldStickToWall(true);
        bubblesManager.addBubble(bubbleView, 60, 20);
    }

    private void toggleContentVisibility(View v) {
        if (v.getVisibility() == View.VISIBLE) {
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bubblesManager.recycle();
    }
}
