package de.uni_weimar.m18.backupfestival.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.mikepenz.materialdrawer.Drawer;

import java.util.List;

import de.uni_weimar.m18.backupfestival.FestivalApplication;
import de.uni_weimar.m18.backupfestival.R;
import de.uni_weimar.m18.backupfestival.models.EventModel;
import de.uni_weimar.m18.backupfestival.network.WpJsonApi;
import de.uni_weimar.m18.backupfestival.views.adapters.EventsPagerAdapter;
import retrofit.RetrofitError;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tr.xip.errorview.ErrorView;
import tr.xip.errorview.RetryListener;


public class EventsFragment extends Fragment {
    private MaterialViewPager mViewPager;

    private WpJsonApi mApi = new WpJsonApi();
    private List<EventModel> mEventList;

    private ProgressBar mProgressBar;
    private ErrorView mErrorView;

    private Toolbar mToolbar;

    public static EventsFragment newInstance() {
        EventsFragment fragment = new EventsFragment();
        return fragment;
    }

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        setHasOptionsMenu(true);

        mViewPager = (MaterialViewPager) rootView.findViewById(R.id.materialViewPager);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.fragment_events_progress);
        mErrorView = (ErrorView) rootView.findViewById(R.id.fragment_events_error_view);

        mToolbar = mViewPager.getToolbar();


        if (mToolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        EventsPagerAdapter adapterViewPager = new EventsPagerAdapter(getActivity().getSupportFragmentManager(), mViewPager);
        mViewPager.getViewPager().setAdapter(adapterViewPager);
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        setToolbar();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        showAll();
    }

    private void showAll() {
        if (mEventList != null) {
            updateViewPager(mEventList);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            //mRecyclerView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.GONE);

            // fetch images from API
            mApi.fetchEvents().cache().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    private Observer<List<EventModel>> observer = new Observer<List<EventModel>>() {
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
        public void onNext(List<EventModel> eventModelList) {
            mEventList = null;
            mEventList = eventModelList;
            updateViewPager(mEventList);

        }
    };

    private void updateViewPager(List<EventModel> events) {
        EventsPagerAdapter adapter = (EventsPagerAdapter)(mViewPager.getViewPager().getAdapter());
        adapter.updateData(events);

        //mCurrentEvents = events;
        //mEventsAdapter.updateData(mCurrent);
        //mRecyclerView.scrollToPosition(0);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //inflater.inflate(R.menu.menu_events, menu);

    }

    private void setToolbar() {
        //set the navigationOnClickListener
        final Drawer.Result drawer = FestivalApplication.getDrawer();
        View.OnClickListener toolbarNavigationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean handled = false;
                if (!handled) {
                    if (drawer.getDrawerLayout().isDrawerOpen(drawer.getSlider())){
                        drawer.getDrawerLayout().closeDrawer(drawer.getSlider());
                    }
                }
            }
        };

        //if we got a toolbar set a toolbarNavigationListener
        if (mToolbar != null) {
            this.mToolbar.setNavigationOnClickListener(toolbarNavigationListener);
        }
        ActionBarDrawerToggle actionBarDrawerToggle = null;
        // create the ActionBarDrawerToggle if not set and enabled and if we have a toolbar
        if (mToolbar != null) {
            actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawer.getDrawerLayout(),
                    mToolbar, R.string.drawer_open, R.string.drawer_close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    if (!true) {
                        super.onDrawerSlide(drawerView, 0);
                    } else {
                        super.onDrawerSlide(drawerView, slideOffset);
                    }
                }
            };
            actionBarDrawerToggle.syncState();

        }

        //handle the ActionBarDrawerToggle
        if (actionBarDrawerToggle != null) {
            actionBarDrawerToggle.setToolbarNavigationClickListener(toolbarNavigationListener);
            drawer.getDrawerLayout().setDrawerListener(actionBarDrawerToggle);
        } else {

        }
    }
}
