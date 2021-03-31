package com.example.dailyfoodplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dailyfoodplan.Args;
import com.example.dailyfoodplan.model.Tweak;


public abstract class TweakLoadingActivity extends AppCompatActivity {
    private Tweak tweak;

    public Tweak getTweak() {
        return tweak;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();

        loadFoodFromIntent();

        if (tweak == null) {
            finish();
        }
    }

    private void initActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadFoodFromIntent() {
        final Intent intent = getIntent();
        if (intent != null) {
            tweak = Tweak.getById(intent.getLongExtra(Args.TWEAK_ID, -1));

            if (tweak != null) {
                setTitle(tweak.getName());
            }
        }
    }
}
