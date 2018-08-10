package io.michalzuk.horton.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toolbar;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.michalzuk.horton.R;
import io.michalzuk.horton.fragments.BlankFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.fragment_container) FrameLayout fragmentContainter;
    @BindString(R.string.action_settings) String drawerStringSettings;
    @BindString(R.string.navigation_feed) String drawerStringFeed;
    @BindString(R.string.navigation_charts) String drawerStringCharts;
    @BindString(R.string.navigation_home) String drawerStringHome;
    @BindString(R.string.navigation_contact_us) String drawerStringContactUs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (toolbar != null) {
            toolbar.setBackground(getResources().getDrawable(R.color.colorAccent));
            setSupportActionBar(toolbar);
        }

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(MainActivity.this)
                .withTranslucentStatusBar(true)
                .build();

        PrimaryDrawerItem home = new PrimaryDrawerItem().withName(drawerStringHome);
        PrimaryDrawerItem feed = new PrimaryDrawerItem().withName(drawerStringFeed);
        PrimaryDrawerItem charts = new PrimaryDrawerItem().withName(drawerStringCharts);
        SecondaryDrawerItem contactUs = new SecondaryDrawerItem().withName(drawerStringContactUs);
        PrimaryDrawerItem settings = new PrimaryDrawerItem().withName(drawerStringSettings);


        final Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        home,
                        new DividerDrawerItem(),
                        feed,
                        charts,
                        contactUs
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    //TODO make switch case work properly
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position){
                            case 1:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new BlankFragment())
                                        .commit();
                                break;
                            case 2:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new BlankFragment())
                                        .commit();
                                break;
                            default:
                        }
                        return false;
                    }
                })
                .build();

        result.addStickyFooterItem(settings);


    }
}
