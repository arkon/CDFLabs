package me.echeung.cdflabs.ui.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.adapters.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private AlertDialog mHelpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up app bar
        setSupportActionBar((Toolbar) findViewById(R.id.appbar));

        // Set up tab bar
        setupTabs();

        // App version number for help dialog
        String version;
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "";
        }

        mHelpDialog = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                .setTitle(R.string.help_and_feedback)
                .setMessage(Html.fromHtml(getString(R.string.help_content, version)))
                .setPositiveButton(R.string.OK, null)
                .create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                ViewPagerAdapter.getLabsFragment().fetchData();
                ViewPagerAdapter.getPrintersFragment().fetchData();
                return true;

            case R.id.action_help:
                showHelpDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Initializes everything for the tabs: the adapter, icons, and title handler
     */
    private void setupTabs() {
        // Set up ViewPager and adapter
        final ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        final ViewPagerAdapter mViewPagerAdapter =
                new ViewPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mViewPagerAdapter);

        // Set up tabs
        final TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        // App bar title
        getSupportActionBar().setTitle(mViewPagerAdapter.getTitle(0));

        // Change app bar title on tab change
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                getSupportActionBar().setTitle(mViewPagerAdapter.getTitle(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Tab icons
        final int tabCount = mTabLayout.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            mTabLayout.getTabAt(i).setIcon(mViewPagerAdapter.getIcon(i));
        }
    }

    /**
     * Displays the help dialog and activates the anchor links.
     */
    private void showHelpDialog() {
        mHelpDialog.show();

        final TextView textContent = (TextView) mHelpDialog.findViewById(android.R.id.message);
        textContent.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
