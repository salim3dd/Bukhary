package com.qays3dd.buhk;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Show_Text_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView textView_show_Text;
    DatabaseHelper db_index = new DatabaseHelper(this);
    DB_Sqlit db_fav = new DB_Sqlit(this);
    Animation animS, tran, tran2, tran_up, tran_down, rotate, rotate2, click, fade_in_text, fade_out_text;

    Button btn_control, btn_copy, btn_share, btn_home, btn_help, btn_fav;
    boolean show_btns = true;
    String page_id, Title_Text;
    int page_id_num;

    WebView webview;
    GlobalVar globalVar = new GlobalVar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);

        textView_show_Text = findViewById(R.id.textView_show_Text);


        webview = findViewById(R.id.webView);
        webview.getSettings().setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webview.getSettings().setDisplayZoomControls(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            webview.getSettings().setTextZoom(110);
        }

        btn_control = findViewById(R.id.btn_control);
        btn_copy = findViewById(R.id.btn_copy);
        btn_share = findViewById(R.id.btn_share);
        btn_home = findViewById(R.id.btn_home);
        btn_help = findViewById(R.id.btn_help);
        btn_fav = findViewById(R.id.btn_fav);

        btn_control.bringToFront();
        animS = AnimationUtils.loadAnimation(this, R.anim.fade_anim);
        tran = AnimationUtils.loadAnimation(this, R.anim.tran);
        tran2 = AnimationUtils.loadAnimation(this, R.anim.tran2);
        tran_up = AnimationUtils.loadAnimation(this, R.anim.tran_up);
        tran_down = AnimationUtils.loadAnimation(this, R.anim.tran_down);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotate2 = AnimationUtils.loadAnimation(this, R.anim.rotate2);
        click = AnimationUtils.loadAnimation(this, R.anim.click);
        fade_in_text = AnimationUtils.loadAnimation(this, R.anim.fade_in_text);
        fade_out_text = AnimationUtils.loadAnimation(this, R.anim.fade_out_text);

        globalVar = (GlobalVar) getApplicationContext();

        Intent intent = getIntent();
        page_id = intent.getStringExtra("page_id");
        Show_Full_Text();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3406286961979146~5664818716");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView;
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.show_text, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.add_fav) {
            add_fav();
            return true;
        }
        if (id == R.id.copy) {
            copy();
            return true;
        }
        if (id == R.id.share) {
            share();
            return true;
        }
        if (id == R.id.help) {
            help();
            return true;
        }
        if (id == R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_index) {
            globalVar.setListType("index");
            finish();
        }
        if (id == R.id.nav_Favorite) {
            globalVar.setListType("favorite");
            finish();

        }

        if (id == R.id.nav_More_app) {
            Intent morapp = new Intent(Intent.ACTION_VIEW);
            morapp.setData(Uri.parse("https://play.google.com/store/apps/developer?id=3DDOman"));
            if (morapp.resolveActivity(getPackageManager()) != null) {
                startActivity(morapp);
            }
        } else if (id == R.id.nav_send) {
            String[] to = {"qays3dd@gmail.com"};
            Intent sendemail = new Intent(Intent.ACTION_SEND);
            sendemail.putExtra(Intent.EXTRA_EMAIL, to);
            sendemail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            sendemail.putExtra(Intent.EXTRA_TEXT, "السلام عليكم ورحمة الله وبركاته معي اقتراح للتطبيق وهو :");
            sendemail.setType("message/rfc822");
            if (sendemail.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(sendemail, "Send Email"));
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Show_Full_Text() {
        //webview.startAnimation(fade_out_text);

        Title_Text = globalVar.getMain_Title();
        this.setTitle(Title_Text);

        StringBuilder sb = new StringBuilder();
        String line;
        try {
            InputStream inputStream = getAssets().open("html/page_" + page_id + ".html");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + " ");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String finlcode = "<html dir=\"rtl\">" + sb;
        String fullTXT = finlcode
                .replace("<p>", "<p align=\"justify\">")
                .replace("<p>", "<p align=\"justify\">")
                .replace("<br><br>", "<br>")
                .replace("FF0000", "ec8200")
                .replace("006600", "ec4f00")
                .replace("000099", "6666cc");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            webview.setScrollY(0);
        }
        webview.loadData(fullTXT, "text/html; charset=UTF-8", null);

    }

    public void btn_control(View view) {

        if (show_btns) {
            btn_control.startAnimation(rotate);

            btn_copy.startAnimation(tran_up);
            btn_share.startAnimation(tran_up);
            btn_home.startAnimation(tran_up);
            btn_help.startAnimation(tran_up);
            btn_fav.startAnimation(tran_up);

            btn_copy.setVisibility(View.VISIBLE);
            btn_home.setVisibility(View.VISIBLE);
            btn_share.setVisibility(View.VISIBLE);
            btn_help.setVisibility(View.VISIBLE);
            btn_fav.setVisibility(View.VISIBLE);
            show_btns = false;
        } else {
            btn_control.startAnimation(rotate2);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                btn_control.setRotation(0);
            }
            btn_copy.startAnimation(tran_down);
            btn_share.startAnimation(tran_down);
            btn_home.startAnimation(tran_down);
            btn_fav.startAnimation(tran_down);
            btn_help.startAnimation(tran_down);

            btn_copy.setVisibility(View.INVISIBLE);
            btn_share.setVisibility(View.INVISIBLE);
            btn_home.setVisibility(View.INVISIBLE);
            btn_fav.setVisibility(View.INVISIBLE);
            btn_help.setVisibility(View.INVISIBLE);

            show_btns = true;
        }
    }


    public void btn_copy(View view) {
        btn_copy.startAnimation(click);
        copy();
    }

    public void copy() {
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            InputStream inputStream = getAssets().open("html/page_" + page_id + ".html");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + " ");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String finlcode = "" + sb;
        String fullTXT = finlcode;
        textView_show_Text.setText(Html.fromHtml(fullTXT));
        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setText(globalVar.getMain_Title() + "\n" + textView_show_Text.getText().toString());
        Toast.makeText(Show_Text_Activity.this, getString(R.string.copy_text_to_clipboard), Toast.LENGTH_SHORT).show();
    }

    public void btn_help(View view) {
        btn_help.startAnimation(click);
        help();
    }

    public void help() {
        try {
            InputStream inputStream = getAssets().open("help.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader BR = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder msg = new StringBuilder();
            while ((line = BR.readLine()) != null) {
                msg.append(line + "\n");
            }
            AlertDialog.Builder build = new AlertDialog.Builder(Show_Text_Activity.this);
            build.setTitle(R.string.help);
            build.setIcon(R.drawable.logo128);
            build.setMessage(Html.fromHtml(msg + ""));
            build.setNegativeButton(R.string.dilog_close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    //Negative
                }
            }).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btn_share(View view) {
        btn_share.startAnimation(click);
        share();
    }

    public void share() {
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            InputStream inputStream = getAssets().open("html/page_" + page_id + ".html");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + " ");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String finlcode = "" + sb;
        String fullTXT = finlcode;
        textView_show_Text.setText(Html.fromHtml(fullTXT));
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + "\n" + globalVar.getMain_Title() + "\n" + textView_show_Text.getText().toString());
        if (share.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(share, "مشاركة "));
        }
    }

    public void btn_home(View view) {
        btn_home.startAnimation(click);
        finish();
    }

    public void btn_fav(View view) {
        btn_fav.startAnimation(click);
        add_fav();
    }

    public void add_fav() {

        ArrayList arrayList = db_fav.get_check_List_Favorite();
        int check = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (globalVar.getSub_Title().equals(arrayList.get(i))) {
                check = 1;
                break;
            } else {
                check = 0;
            }
        }

        if (check == 0) {
            db_fav.Insert_to_favorite(globalVar.getMain_Title(), globalVar.getSub_Title(), page_id);
            Toast.makeText(Show_Text_Activity.this, getString(R.string.add_to_favorite_toast), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(Show_Text_Activity.this, getString(R.string.Title_exists_in_the_Favorites), Toast.LENGTH_SHORT).show();
        }

    }
}
