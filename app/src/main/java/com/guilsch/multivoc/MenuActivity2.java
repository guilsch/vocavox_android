package com.guilsch.multivoc;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//import com.guilsch.multivoc.ui.main.SectionsPagerAdapter;

public class MenuActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);

        ViewPager viewPager = findViewById(R.id.view_pager);

        FragmentPagerAdapter adapter = createFragmentPagerAdapter();
        viewPager.setAdapter(adapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    public static class Tab1Fragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tab1, container, false);

            ImageView flag = view.findViewById(R.id.lang_flag);
            flag.setImageDrawable(Param.FLAG_ICON_TARGET);
            flag.setOnClickListener((v -> {
                MenuActivity2 parentActivity = (MenuActivity2) getActivity();
                parentActivity.changeActivity(MainActivity.class);
            }));

            return view;
        }
    }

    public static class Tab2Fragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tab2, container, false);
            // TODO: Ajoutez votre code pour créer le contenu de l'onglet 2 ici
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