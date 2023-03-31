package com.guilsch.multivoc;

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
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

//import com.guilsch.multivoc.ui.main.SectionsPagerAdapter;

public class MenuActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Deck filteredDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);

        ViewPager viewPager = findViewById(R.id.view_pager);

        FragmentPagerAdapter adapter = createFragmentPagerAdapter();
        viewPager.setAdapter(adapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        utils.printNBCards();

        // Search bar
        filteredDeck = (Deck) Param.GLOBAL_DECK.clone();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String keyWord) {

        filteredDeck.clear();

        if (keyWord == null || keyWord.length() == 0) {
            filteredDeck.addAll(Param.GLOBAL_DECK);
        } else {
            final String filterPattern = keyWord.toString().toLowerCase().trim();

            for (Card card : Param.GLOBAL_DECK) {
                if (card.getItem1().toLowerCase().contains(filterPattern) || card.getItem2().toLowerCase().contains(filterPattern)) {
                    filteredDeck.add(card);
                }
            }
        }

        return false;
    }

    public static class Tab1Fragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tab1, container, false);

            MenuActivity parentActivity = (MenuActivity) getActivity();

            ImageView flag = view.findViewById(R.id.lang_flag);
            ImageView settingsIm = view.findViewById(R.id.setting_im);
            LinearLayout trainLayout = view.findViewById(R.id.train_layout);
            LinearLayout learnLayout = view.findViewById(R.id.learn_layout);
            TextView cardsToTrainNB = view.findViewById(R.id.cards_to_train_nb);

            flag.setImageDrawable(Param.FLAG_ICON_TARGET);
            cardsToTrainNB.setText(String.valueOf(Param.GLOBAL_DECK.getCardsToReviewNb()));
            flag.setOnClickListener(v -> parentActivity.changeActivity(MainActivity.class));
            settingsIm.setOnClickListener(v -> parentActivity.changeActivity(SettingsActivity.class));
            trainLayout.setOnClickListener(v -> parentActivity.changeActivity(RevisionActivity.class));
            learnLayout.setOnClickListener(v -> parentActivity.changeActivity(LearnActivity.class));

            return view;
        }
    }

    public static class Tab2Fragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tab2, container, false);

            MenuActivity parentActivity = (MenuActivity) getActivity();

            SearchView menuSearchBar = view.findViewById(R.id.menu_search_bar);
            LinearLayout translationLayout = view.findViewById(R.id.translation_layout);
            LinearLayout newCardLayout = view.findViewById(R.id.new_card_layout);
            LinearLayout exploreLayout = view.findViewById(R.id.explore_layout);

            translationLayout.setOnClickListener(v -> parentActivity.changeActivity(TranslationActivity.class));
            newCardLayout.setOnClickListener(v -> parentActivity.changeActivity(NewCardActivity.class));
            exploreLayout.setOnClickListener(v -> parentActivity.changeActivity(ExploreActivity.class));

            // Search bar
            SearchDeckAdapter adapter = new SearchDeckAdapter(getContext(), Param.GLOBAL_DECK, parentActivity);
            menuSearchBar.setOnQueryTextListener(parentActivity);

            return view;
        }
    }

    public static class Tab3Fragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tab3, container, false);
            // TODO: Ajoutez votre code pour créer le contenu de l'onglet 3 ici
            return view;
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
                    case 2:
                        return new Tab3Fragment();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

//            @Override
//            public CharSequence getPageTitle(int position) {
//                switch (position) {
//                    case 0:
//                        return "Page 1";
//                    case 1:
//                        return "Page 2";
//                    case 2:
//                        return "Page 3";
//                    default:
//                        return null;
//                }
//            }
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