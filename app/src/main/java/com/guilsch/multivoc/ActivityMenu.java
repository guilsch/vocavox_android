package com.guilsch.multivoc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityMenu extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);

        ViewPager viewPager = findViewById(R.id.view_pager);

        FragmentPagerAdapter adapter = createFragmentPagerAdapter();
        viewPager.setAdapter(adapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Utils.printNBCards();

        // Fragments management
        if (getIntent().hasExtra("FRAG_INDEX")) {
            int fragmentIndex = getIntent().getIntExtra("FRAG_INDEX", 1);
            viewPager.setCurrentItem(fragmentIndex);
        }
    }


    public static class Tab1Fragment extends Fragment {
        @SuppressLint("ClickableViewAccessibility")
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tab1, container, false);

            ActivityMenu parentActivity = (ActivityMenu) getActivity();

            CircleImageView flag = view.findViewById(R.id.lang_flag);
            ImageView settingsIm = view.findViewById(R.id.setting_im);
            LinearLayout trainLayout = view.findViewById(R.id.train_layout);
            LinearLayout learnLayout = view.findViewById(R.id.learn_layout);
            TextView cardsToTrainNB = view.findViewById(R.id.cards_to_train_nb);

            // Set variables
            flag.setImageDrawable(Param.FLAG_ICON_TARGET);
            cardsToTrainNB.setText(String.valueOf(Param.GLOBAL_DECK.getCardsToReviewNb()));

            // On clicks
            settingsIm.setOnClickListener(v -> parentActivity.changeActivity(ActivitySettings.class));
            trainLayout.setOnClickListener(v -> parentActivity.preChangeToTrainActivity());
            learnLayout.setOnClickListener(v -> parentActivity.preChangeToLearnActivity());
            flag.setOnClickListener(v -> parentActivity.changeActivity(ActivityMain.class));

            // On touch
            flag.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            flag.setBorderColor(getContext().getColor(R.color.white));
                            break;
                        case MotionEvent.ACTION_UP:
                            flag.setBorderColor(getContext().getColor(R.color.grey_font));
                            break;
                    }
                    return false;
                }
            });

            return view;
        }
    }

    public static class Tab2Fragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tab2, container, false);

            ActivityMenu parentActivity = (ActivityMenu) getActivity();

            LinearLayout translationLayout = view.findViewById(R.id.translation_layout);
            LinearLayout newCardLayout = view.findViewById(R.id.new_card_layout);
            LinearLayout exploreLayout = view.findViewById(R.id.explore_layout);

            translationLayout.setOnClickListener(v -> parentActivity.preChangeToTranslationActivity());
            newCardLayout.setOnClickListener(v -> parentActivity.changeActivity(ActivityNewCard.class));
            exploreLayout.setOnClickListener(v -> parentActivity.changeActivity(ActivityExplore.class));

            return view;
        }
    }

    public void preChangeToLearnActivity(){
        if (Param.CARDS_TO_LEARN_NB == 0) {
            Utils.showToast(ActivityMenu.this, getString(R.string.toast_msg_no_cards_to_learn));
        }
        else {
            changeActivity(ActivityLearn.class);
        }
    }

    public void preChangeToTrainActivity(){
        if (Param.CARDS_TO_REVIEW_NB == 0) {
            Utils.showToast(ActivityMenu.this, getString(R.string.toast_msg_no_cards_to_train));
        }
        else {
            changeActivity(ActivityTrain.class);
        }
    }

    public void preChangeToTranslationActivity(){
        if (Utils.checkConnexion(getApplicationContext())) {
            changeActivity(ActivityTranslation.class);
        }
        else {
            Utils.showToast(ActivityMenu.this, getString(R.string.toast_msg_no_connexion));
        }
    }

    public FragmentPagerAdapter createFragmentPagerAdapter() {
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new Tab1Fragment();
                    case 1:
                        return new Tab2Fragment();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

        };

        return adapter;
    }

    /**
     * Called when pressing somewhere to change activity
     */
    public void changeActivity(Class newActivityClass) {
        Intent newActivity = new Intent(this, newActivityClass);
        startActivity(newActivity);
        finish();
    }
}