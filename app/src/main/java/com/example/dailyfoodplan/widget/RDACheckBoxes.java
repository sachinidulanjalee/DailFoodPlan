package com.example.dailyfoodplan.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.dailyfoodplan.R;
import com.example.dailyfoodplan.model.DDServings;
import com.example.dailyfoodplan.model.Day;
import com.example.dailyfoodplan.model.Food;
import com.example.dailyfoodplan.model.Tweak;
import com.example.dailyfoodplan.model.TweakServings;
import com.example.dailyfoodplan.task.CalculateStreakTask;
import com.example.dailyfoodplan.task.CalculateTweakStreakTask;
import com.example.dailyfoodplan.task.StreakTaskInput;
import com.example.dailyfoodplan.view.ServingCheckBox;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RDACheckBoxes extends LinearLayout {
    @BindView(R.id.food_check_boxes_container)
    protected ViewGroup vgContainer;

    private List<ServingCheckBox> checkBoxes;

    private com.example.dailyfoodplan.RDA rda;
    private Day day;

    public RDACheckBoxes(Context context) {
        this(context, null);
    }

    public RDACheckBoxes(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RDACheckBoxes(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.food_check_boxes, this);
        ButterKnife.bind(this);
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public void setRDA(com.example.dailyfoodplan.RDA rda) {
        this.rda = rda;
    }

    public void setServings(final com.example.dailyfoodplan.Servings servings) {
        final int numServings = servings != null ? servings.getServings() : 0;
        checkBoxes = new ArrayList<>();
        createCheckBox(checkBoxes, numServings, rda.getRecommendedAmount());

        vgContainer.removeAllViews();

        for (ServingCheckBox checkBox : checkBoxes) {
            vgContainer.addView(checkBox);
        }
    }

    private ServingCheckBox createCheckBox(List<ServingCheckBox> checkBoxes, Integer currentServings, Integer maxServings) {
        final ServingCheckBox checkBox = new ServingCheckBox(getContext());
        checkBox.setChecked(currentServings > 0);
        checkBox.setOnCheckedChangeListener(getOnCheckedChangeListener(checkBox));
        if (maxServings > 1)
            checkBox.setNextServing(createCheckBox(checkBoxes, --currentServings, --maxServings));
        checkBoxes.add(checkBox);
        return checkBox;
    }

    private CompoundButton.OnCheckedChangeListener getOnCheckedChangeListener(final ServingCheckBox checkBox) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBox.onCheckChange(isChecked);

                if (rda instanceof Food) {
                    if (isChecked) {
                        handleServingChecked();
                    } else {
                        handleServingUnchecked();
                    }
                } else if (rda instanceof Tweak) {
                    if (isChecked) {
                        handleTweakChecked();
                    } else {
                        handleTweakUnchecked();
                    }
                }
            }
        };
    }

    private Integer getNumberOfCheckedBoxes() {
        Integer numChecked = 0;
        for (ServingCheckBox checkbox : checkBoxes) {
            if (checkbox.isChecked()) {
                numChecked++;
            }
        }
        return numChecked;
    }

    private void handleServingChecked() {
        day = Day.createDayIfDoesNotExist(day);

        final DDServings servings = DDServings.createServingsIfDoesNotExist(day, (Food)rda);
        final Integer numberOfCheckedBoxes = getNumberOfCheckedBoxes();

        if (servings != null && servings.getServings() != numberOfCheckedBoxes) {
            servings.setServings(numberOfCheckedBoxes);

            servings.save();
            onServingsChanged();
            Timber.d("Increased Servings for %s", servings);
        }
    }

    private void handleServingUnchecked() {
        final DDServings servings = DDServings.getByDateAndFood(day, (Food) rda);
        final Integer numberOfCheckedBoxes = getNumberOfCheckedBoxes();

        if (servings != null && servings.getServings() != numberOfCheckedBoxes) {
            servings.setServings(numberOfCheckedBoxes);

            if (servings.getServings() > 0) {
                servings.save();
                Timber.d("Decreased Servings for %s", servings);
            } else {
                Timber.d("Deleting %s", servings);
                servings.delete();
            }

            onServingsChanged();
        }
    }

    private void handleTweakChecked() {
        day = Day.createDayIfDoesNotExist(day);

        final TweakServings servings = TweakServings.createServingsIfDoesNotExist(day, (Tweak)rda);
        final Integer numberOfCheckedBoxes = getNumberOfCheckedBoxes();

        if (servings != null && servings.getServings() != numberOfCheckedBoxes) {
            servings.setServings(numberOfCheckedBoxes);

            servings.save();
            onTweakServingsChanged();
            Timber.d("Increased TweakServings for %s", servings);
        }
    }

    private void handleTweakUnchecked() {
        final TweakServings servings = TweakServings.getByDateAndTweak(day, (Tweak) rda);
        final Integer numberOfCheckedBoxes = getNumberOfCheckedBoxes();

        if (servings != null && servings.getServings() != numberOfCheckedBoxes) {
            servings.setServings(numberOfCheckedBoxes);

            if (servings.getServings() > 0) {
                servings.save();
                Timber.d("Decreased TweakServings for %s", servings);
            } else {
                Timber.d("Deleting %s", servings);
                servings.delete();
            }

            onTweakServingsChanged();
        }
    }

    private void onServingsChanged() {
        new CalculateStreakTask(getContext()).execute(new StreakTaskInput(day, rda));
    }

    private void onTweakServingsChanged() {
        new CalculateTweakStreakTask(getContext()).execute(new StreakTaskInput(day, rda));
    }
}
