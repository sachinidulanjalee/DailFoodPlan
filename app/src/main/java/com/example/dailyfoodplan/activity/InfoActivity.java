package com.example.dailyfoodplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dailyfoodplan.Args;
import com.example.dailyfoodplan.R;
import com.example.dailyfoodplan.model.Food;
import com.example.dailyfoodplan.model.Tweak;


public abstract class InfoActivity extends AppCompatActivity {
    private Food food;
    private Tweak tweak;

    public Food getFood() {
        return food;
    }

    public Tweak getTweak() {
        return tweak;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();

        loadFoodOrTweakFromIntent();

        if (food == null && tweak == null) {
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

    private void loadFoodOrTweakFromIntent() {
        final Intent intent = getIntent();
        if (intent != null) {
            food = Food.getById(intent.getLongExtra(Args.FOOD_ID, -1));
            if (food != null) {
                setTitle(food.getName());
                return;
            }

            tweak = Tweak.getById(intent.getLongExtra(Args.TWEAK_ID, -1));
            setTitle(R.string.about_tweak);
        }
    }
}
