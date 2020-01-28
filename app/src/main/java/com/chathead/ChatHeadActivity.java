package com.chathead;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nex3z.notificationbadge.NotificationBadge;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatHeadActivity extends AppCompatActivity {

    CircleImageView imageView;
    ViewPager viewPager;
    private BubblesManager bubblesManager;
    private NotificationBadge mBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.chathead_activity);

        initBubble();

        imageView = findViewById(R.id.avatar2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatHeadActivity.this, MainActivity.class);
                intent.putExtra("show", "show");
                startActivity(intent);
                finish();
            }
        });

        // Find the view pager that will allow the user to swipe between fragments
        viewPager = findViewById(R.id.my2viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getApplicationContext(), getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.my2tablayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void initBubble() {
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
        final BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(ChatHeadActivity.this)
                .inflate(R.layout.bubble_layout, null);
        mBadge = (NotificationBadge) bubbleView.findViewById(R.id.count);
        mBadge.setNumber(88);

        bubbleView.setShouldStickToWall(true);
        bubblesManager.addBubble(bubbleView, 60, 20);

    }

    @Override
    public void onBackPressed() {
        addNewBubble();
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}
