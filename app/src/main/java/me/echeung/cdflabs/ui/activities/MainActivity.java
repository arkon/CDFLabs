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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up app bar
        setSupportActionBar((Toolbar) findViewById(R.id.appbar));

        // Set up ViewPager and adapter
        final ViewPager mViewPager =
                (ViewPager) findViewById(R.id.pager);
        final ViewPagerAdapter mViewPagerAdapter =
                new ViewPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mViewPagerAdapter);

        // Set up tabs
        final TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
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

    private void showHelpDialog() {
        String version;
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "";
        }

        final CharSequence content = Html.fromHtml(getString(R.string.help_content, version));

        final AlertDialog dialog =
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                        .setTitle(R.string.help_and_feedback)
                        .setMessage(content)
                        .setPositiveButton(R.string.OK, null)
                        .show();

        final TextView textContent = (TextView) dialog.findViewById(android.R.id.message);
        textContent.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
