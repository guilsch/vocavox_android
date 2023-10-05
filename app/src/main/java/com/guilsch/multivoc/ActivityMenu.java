package com.guilsch.multivoc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.pixelcan.inkpageindicator.InkPageIndicator;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityMenu extends AppCompatActivity  {

    private static boolean isZoomed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);

        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tabs);

        AdapterMenuPager adapter = new AdapterMenuPager(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Tab 1");
        adapter.addFragment(new Tab2Fragment(), "Tab 2");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(viewPager);

        Utils.printNBCards();
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
            ImageView menuImageView = view.findViewById(R.id.menu_image_view);

            // Set variables
            flag.setImageDrawable(Param.FLAG_ICON_TARGET);
            cardsToTrainNB.setText(String.valueOf(Param.GLOBAL_DECK.getCardsToReviewNb()));

            // On clicks
            settingsIm.setOnClickListener(v -> parentActivity.changeActivity(ActivitySettings.class));
            trainLayout.setOnClickListener(v -> parentActivity.preChangeToTrainActivity());
            learnLayout.setOnClickListener(v -> parentActivity.preChangeToLearnActivity());
            flag.setOnClickListener(v -> parentActivity.changeActivity(ActivityMain.class));

            // Image zoom init
            isZoomed = false;

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

            menuImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // L'utilisateur a appuyé sur l'image
                            if (!isZoomed) {
                                zoomImageIn(menuImageView, 1.2f, 1000);
                                isZoomed = true;
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            // L'utilisateur a relâché l'image
                            if (isZoomed) {
                                zoomImageOut(menuImageView, 1.2f, 300);
                                isZoomed = false;
                            }
                            break;
                    }
                    return true;
                }
            });

//            trainLayout.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            // L'utilisateur a appuyé sur l'image
//                            if (!isZoomed) {
//                                zoomImageIn(trainLayout, 1.05f, 100);
//                                isZoomed = true;
//                            }
//                            break;
//                        case MotionEvent.ACTION_UP:
//                        case MotionEvent.ACTION_CANCEL:
//                            // L'utilisateur a relâché l'image
//                            if (isZoomed) {
//                                zoomImageOut(trainLayout, 1.05f, 100);
//                                isZoomed = false;
//                            }
//                            break;
//                    }
//                    return true;
//                }
//            });

//            trainLayout.setOnTouchListener(new View.OnTouchListener() {
//                private float startX, startY;
//                private boolean isZoomed = false;
//
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            startX = event.getX();
//                            startY = event.getY();
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            float endX = event.getX();
//                            float endY = event.getY();
//
//                            float deltaX = Math.abs(endX - startX);
//                            float deltaY = Math.abs(endY - startY);
//
//                            // Vérifiez si le mouvement est suffisamment petit pour être considéré comme un clic
//                            if (deltaX < 10 && deltaY < 10) {
//                                // C'est un clic
//                                if (!isZoomed) {
//                                    // Exécutez votre logique de zoom ici si nécessaire
//                                    zoomImageIn(trainLayout, 1.05f, 100);
//                                    isZoomed = true;
//                                } else {
//                                    // Si déjà agrandi, exécutez votre logique de clic ici
//                                    parentActivity.preChangeToTrainActivity();
//                                }
//                            } else {
//                                // C'est un mouvement tactile, par exemple un balayage
//                                if (isZoomed) {
//                                    // Exécutez votre logique de zoom-out ici si nécessaire
//                                    zoomImageOut(trainLayout, 1.05f, 100);
//                                    isZoomed = false;
//                                }
//                            }
//                            break;
//                    }
//                    return true;
//                }
//            });

            return view;
        }
    }

    private static void zoomImageIn(View view, float scale, int speed) {
        Animation animation = new ScaleAnimation(
                1f, scale,
                1f, scale,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(speed);
        animation.setFillAfter(true);

        view.startAnimation(animation);
    }

    private static void zoomImageOut(View view, float scale, int speed) {
        Animation animation = new ScaleAnimation(
                scale, 1f,
                scale, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(speed);
        animation.setFillAfter(true);

        view.startAnimation(animation);
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