package de.uni_weimar.m18.backupfestival.views.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import de.uni_weimar.m18.backupfestival.fragments.FilmsFragment;
import de.uni_weimar.m18.backupfestival.models.FilmModel;

/**
 * Created by ike on 21.05.15.
 */
public class FilmsPagerAdapter extends FragmentStatePagerAdapter {


    private List<FilmModel> mFilms;
    private ArrayList<List<FilmModel>> mFilmsDataModels;
    private ArrayList<FilmsFragment> mFilmsFragments;

    public FilmsPagerAdapter(android.support.v4.app.FragmentManager fragmentManager) {
        super(fragmentManager);
        mFilmsFragments = new ArrayList<>();
        mFilmsDataModels = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mFilmsFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        if (position >= mFilmsDataModels.size())
            return null;
        FilmsFragment filmsFragment = mFilmsFragments.get(position);
        if (filmsFragment == null) {
            filmsFragment = FilmsFragment.newInstance();
            filmsFragment.updateFilms(mFilms);
            mFilmsFragments.set(position, filmsFragment);
        }
        return filmsFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return super.getPageTitle(position);
        return "Page " + position;
    }

    public void updateData(List<FilmModel> films) {
        mFilms = films;
        //if (mFilmsFragments.size() == 0) {
        //    mFilmsFragments.add(FilmsFragment.newInstance());
        //    mFilmsFragments.add(FilmsFragment.newInstance());
        //}
        //mFilmsFragments.get(0).updateFilms(mFilms);
        //mFilmsFragments.get(1).updateFilms(mFilms);

        // Add for each fragment a "null" entry in the FragmentArrayList
        mFilmsDataModels.add(mFilms);
        mFilmsDataModels.add(mFilms);
        mFilmsFragments.add(null);
        mFilmsFragments.add(null);
        notifyDataSetChanged();
    }
}
