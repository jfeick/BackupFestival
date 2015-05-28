package de.uni_weimar.m18.backupfestival.views.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import de.uni_weimar.m18.backupfestival.FestivalApplication;
import de.uni_weimar.m18.backupfestival.R;
import de.uni_weimar.m18.backupfestival.fragments.EventsRecyclerViewFragment;
import de.uni_weimar.m18.backupfestival.models.EventModel;
import de.uni_weimar.m18.backupfestival.models.EventModelComparer;
import de.uni_weimar.m18.backupfestival.models.FilmModel;

public class EventsPagerAdapter extends FragmentStatePagerAdapter {


    private List<EventModel> mEvents;
    private SparseArray<Fragment> mRegisteredFragments = new SparseArray<>();
    private ArrayList<List<EventModel>> mEventsDataModels = new ArrayList<>();
    private ArrayList<String> mTabTitles = new ArrayList<>();
    private ArrayList<Integer> mTabColors = new ArrayList<>();
    private ArrayList<Integer> mTabImages = new ArrayList<>();

    private ArrayList<EventsRecyclerViewFragment> mEventsRecyclerViewFragments = new ArrayList<>();
    private MaterialViewPager mViewPager;
    private int mOldPosition = -1;

    public EventsPagerAdapter(android.support.v4.app.FragmentManager fragmentManager,
                             MaterialViewPager viewPager) {
        super(fragmentManager);
        mViewPager = viewPager;
        //mFilmsRecyclerViewFragments = new ArrayList<>();
        //mEventsDataModels = new ArrayList<>();
        //mTabTitles = new ArrayList<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mRegisteredFragments.put(position, fragment);
        return fragment;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //object = null; // without this, the FilmFragment is not getting garbagecollected
        mRegisteredFragments.remove(position);
        super.destroyItem(container, position, object);
        //System.gc();
    }

    // Returns the fragment for the position (if instantiated)
    public Fragment getRegisteredFragment(int position) {
        return mRegisteredFragments.get(position);
    }

    @Override
    public int getCount() {
        return mEventsRecyclerViewFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        if (position >= mEventsDataModels.size())
            return null;
        /*
        FilmsRecyclerViewFragment filmsRecyclerViewFragment = mFilmsRecyclerViewFragments.get(position);
        if (filmsRecyclerViewFragment == null) {
            filmsRecyclerViewFragment = FilmsRecyclerViewFragment.newInstance();
            filmsRecyclerViewFragment.updateFilms(mEventsDataModels.get(position));
            mFilmsRecyclerViewFragments.set(position, filmsRecyclerViewFragment);
        }
        return filmsRecyclerViewFragment;
        */
        EventsRecyclerViewFragment eventsRecyclerViewFragment = EventsRecyclerViewFragment.newInstance();
        eventsRecyclerViewFragment.updateEvents(mEventsDataModels.get(position));
        return eventsRecyclerViewFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if(position == mOldPosition)
            return;
        mOldPosition = position;

        //int color = 0;
        //color = FestivalApplication.getContext().getResources().getColor(mTabColors.get(position));

        //Drawable drawable = FestivalApplication.getContext().getResources().getDrawable(mTabImages.get(position));
        //getDrawable(mTabImages.get(position));
        // TODO: just for debug purpose:
        //Drawable drawable = FestivalApplication.getContext().getResources().getDrawable(R.drawable.standard);

        final int fadeDuration = 400;

        // change header's color and image
        //mViewPager.setImageUrl(imageUrl, fadeDuration);



        //mViewPager.setImageDrawable(drawable, fadeDuration);
        //mViewPager.setColor(color, fadeDuration);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return super.getPageTitle(position);
        if(position < mTabTitles.size())
            return mTabTitles.get(position);
        else
            return "tab";
    }

    public void updateData(List<EventModel> events) {
        //mEvents = events;

        // here we need to sort the list of events for date and time
        HashSet<String> dateStringSet = new HashSet<>();
        for (EventModel eventModel : events) {
            String dateString = eventModel.getDateString();
            if(dateString != null)
                dateStringSet.add(dateString);
        }
        mTabTitles = new ArrayList<>(dateStringSet);
        Collections.sort(mTabTitles);

        for(int i = 0; i < mTabTitles.size(); ++i) {
            mEventsDataModels.add(new ArrayList<EventModel>());
        }

        for(EventModel eventModel : events) {
            String dateString = eventModel.getDateString();
            if(dateString != null)
                mEventsDataModels.get(mTabTitles.indexOf(dateString)).add(eventModel);
        }

        for (List<EventModel> eventDataModel : mEventsDataModels) {
            Collections.sort(eventDataModel, new EventModelComparer());
        }

        mEventsRecyclerViewFragments.clear();
        for (int i = 0; i < mEventsDataModels.size(); ++i) {
            mEventsRecyclerViewFragments.add(null);
        }

        notifyDataSetChanged();
    }
}
