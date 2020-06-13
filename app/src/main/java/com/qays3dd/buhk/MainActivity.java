package com.qays3dd.buhk;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    ListView listView_main;
    ArrayList<List_itme_Index> listIndex = new ArrayList<>();

    ArrayList<List_itme_Index> listIndex_Search = new ArrayList<>();

    DatabaseHelper db_index = new DatabaseHelper(this);
    DB_Sqlit db_fav = new DB_Sqlit(this);
    GlobalVar globalVar = new GlobalVar();
    private static long time;
    TextView textView_Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView_main = findViewById(R.id.listView_main);
        textView_Title = findViewById(R.id.textView_Title);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3406286961979146~5664818716");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        globalVar = (GlobalVar) getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        if (globalVar.getListType().equals("Sup_index")
                || globalVar.getListType().equals("favorite")
                || globalVar.getListType().equals("search")
                ) {
            globalVar.setListType("index");
            onResume();
        } else {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                if (time + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                } else {
                    Toast.makeText(MainActivity.this, R.string.Click_again_to_close, Toast.LENGTH_LONG).show();
                }
                time = System.currentTimeMillis();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_searchable));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.help) {
            try {
                InputStream inputStream = getAssets().open("help.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader BR = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder msg = new StringBuilder();
                while ((line = BR.readLine()) != null) {
                    msg.append(line + "\n");
                }
                AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
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
            return true;
        }
        if (id == R.id.Close) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_index) {
            globalVar.setListType("index");
            import_Main_listIndex();
            textView_Title.setText(getString(R.string.index));
        }
        if (id == R.id.nav_Favorite) {
            globalVar.setListType("favorite");
            import_from_favorite();

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


    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search(newText);
        return false;
    }

    public void search(String word) {
        if (word.length() > 1) {
            listIndex_Search.clear();
            listIndex_Search = db_index.get_Search(word);
            search_listIndex();
        }

    }

    public void search_listIndex() {
        globalVar.setListType("search");
        textView_Title.setText("مجموع كلمات البحث : " + listIndex_Search.size());
        listAdapter adapter1 = new listAdapter(listIndex_Search);
        listView_main.setAdapter(adapter1);
    }

    private void import_from_favorite() {
        textView_Title.setText(getString(R.string.favorite));
        ArrayList<List_itme_Index> arrayList = db_fav.getAllList_Favorite();
        if (arrayList.size() == 0) {
            Toast.makeText(MainActivity.this, R.string.favorite_is_Empty, Toast.LENGTH_SHORT).show();
            globalVar.setListType("index");
            import_Main_listIndex();
            textView_Title.setText(getString(R.string.index));
        } else {
            listAdapter adapter = new listAdapter(arrayList);
            listView_main.setAdapter(adapter);
        }

    }

    public void import_Main_listIndex() {

        listIndex = db_index.get_All_Main_Titles();
        listAdapter adapter1 = new listAdapter(listIndex);
        listView_main.setAdapter(adapter1);
    }

    public void import_Sup_listIndex(String main_title) {
        textView_Title.setText(globalVar.getMain_Title());
        listIndex = db_index.get_All_Sub_Titles(main_title);
        listAdapter adapter1 = new listAdapter(listIndex);
        listView_main.setAdapter(adapter1);
    }


    @Override
    protected void onResume() {
        super.onResume();
        String indexType = globalVar.getListType();
        switch (indexType) {
            case "index":
                import_Main_listIndex();
                textView_Title.setText(getString(R.string.index));
                break;
            case "Sup_index":

                import_Sup_listIndex(globalVar.getMain_Title());
                break;
            case "favorite":

                import_from_favorite();
                break;

            case "search":
                search_listIndex();
                break;
        }
    }

    public void text_title(View view) {
    }


    class listAdapter extends BaseAdapter {
        ArrayList<List_itme_Index> mlistItmes = new ArrayList<List_itme_Index>();

        listAdapter(ArrayList<List_itme_Index> listdata) {
            this.mlistItmes = listdata;
        }


        @Override
        public int getCount() {
            return mlistItmes.size();
        }

        @Override
        public Object getItem(int position) {
            return mlistItmes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = View.inflate(getApplicationContext(), R.layout.row_itme, null);

            final TextView Title = v.findViewById(R.id.textView_row_imte);
            final ImageView img_fav = v.findViewById(R.id.img_fav);

            if (globalVar.getListType().equals("favorite") || globalVar.getListType().equals("search")) {
                Title.setText(Html.fromHtml("<font color=\"#ec4f00\"><b>"+mlistItmes.get(position).getMain_Title() +"</b></font>"+ " | " + mlistItmes.get(position).getSub_Title()));
            } else if (globalVar.getListType().equals("index")) {
                Title.setText(mlistItmes.get(position).getMain_Title());
            } else if (globalVar.getListType().equals("Sup_index")) {
                Title.setText(mlistItmes.get(position).getSub_Title());
            }

            final String page_id = mlistItmes.get(position).getPage_id();


            Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "font.otf");
            Title.setTypeface(typeface);
           // Title.setTextColor(Integer.parseInt(db_fav.get_text_color()));

            Title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (globalVar.getListType().equals("index")) {
                        globalVar.setListType("Sup_index");
                        textView_Title.setText(mlistItmes.get(position).getMain_Title());
                        globalVar.setMain_Title(mlistItmes.get(position).getMain_Title());
                        import_Sup_listIndex(mlistItmes.get(position).getMain_Title());
                    } else if (globalVar.getListType().equals("Sup_index")) {
                        globalVar.setSub_Title(mlistItmes.get(position).getSub_Title());
                        Intent intpage = new Intent(MainActivity.this, Show_Text_Activity.class);
                        intpage.putExtra("page_id", page_id);
                        startActivity(intpage);
                    } else if (globalVar.getListType().equals("favorite")) {
                        globalVar.setSub_Title(mlistItmes.get(position).getSub_Title());
                        Intent intpage = new Intent(MainActivity.this, Show_Text_Activity.class);
                        intpage.putExtra("page_id", page_id);
                        startActivity(intpage);
                    } else if (globalVar.getListType().equals("search")) {

                        globalVar.setSub_Title(mlistItmes.get(position).getSub_Title());
                        globalVar.setMain_Title(mlistItmes.get(position).getMain_Title());
                        Intent intpage = new Intent(MainActivity.this, Show_Text_Activity.class);
                        intpage.putExtra("page_id", page_id);
                        startActivity(intpage);
                    }

                }
            });

            Title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (globalVar.getListType().equals("favorite")) {
                        AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
                        build.setMessage(R.string.Delete_MessagText)
                                .setIcon(R.drawable.logo128)
                                .setTitle(R.string.Delete_dilogTitle)
                                .setPositiveButton(R.string.dilog_yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        db_fav.Delete(mlistItmes.get(position).getId(), "favorite");
                                        import_from_favorite();
                                    }
                                })
                                .setNegativeButton(R.string.dilog_no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Negative
                                    }
                                }).show();
                    }
                    return false;
                }


            });

            return v;
        }
    }
}
