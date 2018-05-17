package com.example.hole1.mynewsagg.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.hole1.mynewsagg.R;
import com.example.hole1.mynewsagg.adapter.DataAdapter;
import com.example.hole1.mynewsagg.model.ArticleStructure;
import com.example.hole1.mynewsagg.model.Constants;
import com.example.hole1.mynewsagg.model.NewsResponse;
import com.example.hole1.mynewsagg.network.ApiClient;
import com.example.hole1.mynewsagg.network.ApiInterface;
import com.example.hole1.mynewsagg.network.interceptors.ResponseCacheInterceptor;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private String[] SOURCE_ARRAY = {"google-news", "bbc-news",
            "buzzfeed", "mtv-news", "bbc-sport",  "talksport", "medical-news-today",
            "national-geographic"};
    private String SOURCE;


    private ArrayList<ArticleStructure> articleStructure = new ArrayList<>();
    private DataAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Drawer result;
    private AccountHeader accountHeader;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Parcelable listState;

    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createToolbar();
        createRecyclerView();
        SOURCE = SOURCE_ARRAY[0];
        mTitle.setText(R.string.toolbar_default_text);
        onLoadingSwipeRefreshLayout();
        createDrawer(savedInstanceState, toolbar);
    }

    private void createToolbar() {
        toolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTitle = findViewById(R.id.toolbar_title);

    }

    private void createRecyclerView() {
        recyclerView = findViewById(R.id.card_recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    private void createDrawer(Bundle savedInstanceState, final Toolbar toolbar) {
        PrimaryDrawerItem item0 = new PrimaryDrawerItem().withIdentifier(0).withName("GENERAL")
                .withSelectable(false);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Google News")
                .withIcon(R.drawable.ic_googlenews);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("BBC News")
                .withIcon(R.drawable.ic_bbcnews);

        SectionDrawerItem item3 = new SectionDrawerItem().withIdentifier(3).withName("ENTERTAINMENT");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("Buzzfeed")
                .withIcon(R.drawable.ic_buzzfeednews);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName("MTV News")
                .withIcon(R.drawable.ic_mtvnews);
        SectionDrawerItem item6 = new SectionDrawerItem().withIdentifier(6).withName("SPORTS");
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(7).withName("BBC Sports")
                .withIcon(R.drawable.ic_bbcsports);
        PrimaryDrawerItem item8 = new PrimaryDrawerItem().withIdentifier(8).withName("TalkSport")
                .withIcon(R.drawable.ic_talksport);
        SectionDrawerItem item9 = new SectionDrawerItem().withIdentifier(9).withName("SCIENCE");
        PrimaryDrawerItem item10 = new PrimaryDrawerItem().withIdentifier(10).withName("Medical News Today")
                .withIcon(R.drawable.ic_medicalnewstoday);
        PrimaryDrawerItem item11 = new PrimaryDrawerItem().withIdentifier(11).withName("National Geographic")
                .withIcon(R.drawable.ic_nationalgeographic);
        SecondaryDrawerItem item12 = new SecondaryDrawerItem().withIdentifier(12).withName("Powered by newsapi.org");


        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSelectedItem(1)
                .addDrawerItems(item0, item1, item2, item3, item4, item5, item6,
                        item7, item8, item9, item10, item11,  item12)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        int selected = (int) (long) drawerItem.getIdentifier();
                        switch (selected) {
                            case 1:
                                SOURCE = SOURCE_ARRAY[0];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 2:
                                SOURCE = SOURCE_ARRAY[1];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 4:
                                SOURCE = SOURCE_ARRAY[2];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 5:
                                SOURCE = SOURCE_ARRAY[3];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 7:
                                SOURCE = SOURCE_ARRAY[4];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 8:
                                SOURCE = SOURCE_ARRAY[5];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 10:
                                SOURCE = SOURCE_ARRAY[6];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 11:
                                SOURCE = SOURCE_ARRAY[7];
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 12:
                                Intent browserAPI = new Intent(Intent.ACTION_VIEW, Uri.parse("https://newsapi.org/"));
                                startActivity(browserAPI);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }
    private void loadJSON() {
        swipeRefreshLayout.setRefreshing(true);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addNetworkInterceptor(new ResponseCacheInterceptor());
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);

        ApiInterface request = ApiClient.getClient(httpClient).create(ApiInterface.class);

        Call<NewsResponse> call = request.getHeadlines(SOURCE, Constants.API_KEY);
        call.enqueue(new Callback<NewsResponse>() {

            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {

                if (response.isSuccessful() && response.body().getArticles() != null) {

                    if (!articleStructure.isEmpty()) {
                        articleStructure.clear();
                    }

                    articleStructure = response.body().getArticles();

                    adapter = new DataAdapter(MainActivity.this, articleStructure);
                    recyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }


            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadJSON();
    }
    private void onLoadingSwipeRefreshLayout() {

        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        loadJSON();
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_newsapi:
                Intent browserAPI = new Intent(Intent.ACTION_VIEW, Uri.parse("https://newsapi.org/"));
                startActivity(browserAPI);
                break;
            case R.id.action_search:
                openSearchActivity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void openSearchActivity() {
        Intent searchIntent = new Intent(this, SearchActivity.class);
        startActivity(searchIntent);
    }


    public void onBackPressed() {
        if (result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher_round);
            builder.setMessage("Do you want to Exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle = result.saveInstanceState(bundle);
        bundle = accountHeader.saveInstanceState(bundle);

        super.onSaveInstanceState(bundle);
        listState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundle.putParcelable(Constants.RECYCLER_STATE_KEY, listState);
        bundle.putString(Constants.SOURCE, SOURCE);
        bundle.putString(Constants.TITLE_STATE_KEY, mTitle.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            SOURCE = savedInstanceState.getString(Constants.SOURCE);
            createToolbar();
            mTitle.setText(savedInstanceState.getString(Constants.TITLE_STATE_KEY));
            listState = savedInstanceState.getParcelable(Constants.RECYCLER_STATE_KEY);
            createDrawer(savedInstanceState, toolbar);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}