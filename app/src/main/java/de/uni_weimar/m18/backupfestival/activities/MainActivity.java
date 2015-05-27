package de.uni_weimar.m18.backupfestival.activities;

import android.animation.LayoutTransition;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

import de.uni_weimar.m18.backupfestival.R;
import de.uni_weimar.m18.backupfestival.models.FilmModel;
import de.uni_weimar.m18.backupfestival.network.WpJsonApi;
import de.uni_weimar.m18.backupfestival.views.adapters.FilmsPagerAdapter;
import retrofit.RetrofitError;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tr.xip.errorview.ErrorView;
import tr.xip.errorview.RetryListener;


public class MainActivity extends AppCompatActivity {

    private MaterialViewPager mViewPager;

    private WpJsonApi mApi = new WpJsonApi();
    private List<FilmModel> mImages;

    private ProgressBar mProgressBar;
    private ErrorView mErrorView;

    public enum Category {
        EVENTS(1000),
        FILMS(1001);

        public final int id;
        private Category(int id) {
            this.id = id;
        }
    }

    public Drawer.Result result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        mProgressBar = (ProgressBar) findViewById(R.id.fragment_films_progress);
        mErrorView = (ErrorView) findViewById(R.id.fragment_films_error_view);

        Toolbar toolbar = mViewPager.getToolbar();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        FilmsPagerAdapter adapterViewPager = new FilmsPagerAdapter(getSupportFragmentManager(), mViewPager);
        mViewPager.getViewPager().setAdapter(adapterViewPager);
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());


        result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.header_drawer)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_events)
                                .withIdentifier(Category.EVENTS.id)
                                .withIcon(GoogleMaterial.Icon.gmd_schedule),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_films)
                                .withIdentifier(Category.FILMS.id)
                                .withIcon(GoogleMaterial.Icon.gmd_theaters)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        if (iDrawerItem != null) {
                            //toolbar.setTitle(((Nameable) iDrawerItem).getNameRes());
                        }
                    }
                })
                .build();

        // disable scrollbar
        result.getListView().setVerticalScrollBarEnabled(false);

        showAll();
    }

    private void showAll() {
        if (mImages != null) {
            updateViewPager(mImages);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            //mRecyclerView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.GONE);

            // fetch images from API
            mApi.fetchFilms().cache().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    private Observer<List<FilmModel>> observer = new Observer<List<FilmModel>>() {
        @Override
        public void onCompleted() {
            mProgressBar.setVisibility(View.GONE);
            //mRecyclerView.setVisibility(View.VISIBLE);
            mErrorView.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable error) {
            if (error instanceof RetrofitError) {
                RetrofitError e = (RetrofitError) error;
                if (e.getKind() == RetrofitError.Kind.NETWORK) {
                    mErrorView.setErrorTitle("No connection");
                    mErrorView.setErrorSubtitle("It seems you have no network connection.");
                } else if (e.getKind() == RetrofitError.Kind.HTTP) {
                    mErrorView.setErrorTitle("Server Exception");
                    mErrorView.setErrorSubtitle("The server returned an exception");
                } else {
                    mErrorView.setErrorTitle("WTF?");
                    mErrorView.setErrorSubtitle("There was an exception which should not happen.");
                }
            }

            mProgressBar.setVisibility(View.GONE);
            //mRecyclerView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.VISIBLE);


            mErrorView.setOnRetryListener(new RetryListener() {
                @Override
                public void onRetry() {
                    showAll();
                }
            });
        }

        @Override
           public void onNext(List<FilmModel> filmModelList) {
            mImages = null;
            mImages = filmModelList;
            updateViewPager(mImages);

            //if (FilmsFragment.this.getActivity() instanceof MainActivity) {
                // TODO: set category count?
            //}

        }
    };

    private void updateViewPager(List<FilmModel> images) {
        FilmsPagerAdapter adapter = (FilmsPagerAdapter)(mViewPager.getViewPager().getAdapter());
        adapter.updateData(images);

        //mCurrentImages = images;
        //mFilmAdapter.updateData(mCurrentImages);
        //mRecyclerView.scrollToPosition(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_films, menu);
        /*
        menu.findItem(R.id.action_films_search).setIcon(
                new IconicsDrawable(this, GoogleMaterial.Icon.gmd_search)
                        .color(Color.LTGRAY)
                        .actionBarSize()
        );
        */

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_films_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        //SearchView searchView =
        //        (SearchView) menu.findItem(R.id.action_films_search).getActionView();
        //searchView.setSearchableInfo(
        //        searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
