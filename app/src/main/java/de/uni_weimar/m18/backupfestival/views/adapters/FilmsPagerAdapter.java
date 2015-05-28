package de.uni_weimar.m18.backupfestival.views.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPager;

import java.util.ArrayList;
import java.util.List;

import de.uni_weimar.m18.backupfestival.FestivalApplication;
import de.uni_weimar.m18.backupfestival.R;
import de.uni_weimar.m18.backupfestival.fragments.FilmsRecyclerViewFragment;
import de.uni_weimar.m18.backupfestival.models.FilmModel;

/**
 * Created by ike on 21.05.15.
 */
public class FilmsPagerAdapter extends FragmentStatePagerAdapter {


    private List<FilmModel> mFilms;
    private SparseArray<Fragment> mRegisteredFragments = new SparseArray<>();
    private ArrayList<List<FilmModel>> mFilmsDataModels = new ArrayList<>();
    private ArrayList<String> mTabTitles = new ArrayList<>();
    private ArrayList<Integer> mTabColors = new ArrayList<>();
    private ArrayList<Integer> mTabImages = new ArrayList<>();

    private ArrayList<FilmsRecyclerViewFragment> mFilmsRecyclerViewFragments = new ArrayList<>();
    private MaterialViewPager mViewPager;
    private int mOldPosition = -1;

    public FilmsPagerAdapter(android.support.v4.app.FragmentManager fragmentManager,
                             MaterialViewPager viewPager) {
        super(fragmentManager);
        mViewPager = viewPager;
        //mFilmsRecyclerViewFragments = new ArrayList<>();
        //mFilmsDataModels = new ArrayList<>();
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
        return mFilmsRecyclerViewFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        if (position >= mFilmsDataModels.size())
            return null;
        /*
        FilmsRecyclerViewFragment filmsRecyclerViewFragment = mFilmsRecyclerViewFragments.get(position);
        if (filmsRecyclerViewFragment == null) {
            filmsRecyclerViewFragment = FilmsRecyclerViewFragment.newInstance();
            filmsRecyclerViewFragment.updateFilms(mFilmsDataModels.get(position));
            mFilmsRecyclerViewFragments.set(position, filmsRecyclerViewFragment);
        }
        return filmsRecyclerViewFragment;
        */
        FilmsRecyclerViewFragment filmsRecyclerViewFragment = FilmsRecyclerViewFragment.newInstance();
        filmsRecyclerViewFragment.updateFilms(mFilmsDataModels.get(position));
        return filmsRecyclerViewFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if(position == mOldPosition
                || position >= mTabColors.size()
                || position >= mTabImages.size())
            return;
        mOldPosition = position;

        int color = 0;
        color = FestivalApplication.getContext().getResources().getColor(mTabColors.get(position));

        //Drawable drawable = FestivalApplication.getContext().getResources().getDrawable(mTabImages.get(position));
                //getDrawable(mTabImages.get(position));
        // TODO: just for debug purpose:
        //Drawable drawable = FestivalApplication.getContext().getResources().getDrawable(R.drawable.standard);

        final int fadeDuration = 400;

        // change header's color and image
        //mViewPager.setImageUrl(imageUrl, fadeDuration);



        //mViewPager.setImageDrawable(drawable, fadeDuration);
        mViewPager.setColor(color, fadeDuration);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return super.getPageTitle(position);
        if(position < mTabTitles.size())
            return mTabTitles.get(position);
        else
            return "tab";
    }

    public void updateData(List<FilmModel> films) {
        mFilms = films;
        mFilmsDataModels.add(mFilms);
        mTabTitles.add("all");
        mTabColors.add(R.color.films_standard);
        mTabImages.add(R.drawable.standard);
        // filter films



        ArrayList<FilmModel> awardBlauModel = new ArrayList<>();
        mTabTitles.add("award blau");
        ArrayList<FilmModel> awardGelbModel = new ArrayList<>();
        mTabTitles.add("award gelb");
        ArrayList<FilmModel> awardGruenModel = new ArrayList<>();
        mTabTitles.add("award grün");
        ArrayList<FilmModel> awardLilaModel = new ArrayList<>();
        mTabTitles.add("award lila");
        ArrayList<FilmModel> awardPinkModel = new ArrayList<>();
        mTabTitles.add("award pink");
        ArrayList<FilmModel> awardRotModel  = new ArrayList<>();
        mTabTitles.add("award rot");



        for (FilmModel filmModel : films) {
            String award = filmModel.getAward();
            if (award != null) {
                switch (award) {
                    case "Award Blau":
                        awardBlauModel.add(filmModel);
                        break;
                    case "Award Gelb":
                        awardGelbModel.add(filmModel);
                        break;
                    case "Award Grün":
                        awardGruenModel.add(filmModel);
                        break;
                    case "Award Lila":
                        awardLilaModel.add(filmModel);
                        break;
                    case "Award Pink":
                        awardPinkModel.add(filmModel);
                        break;
                    case "Award Rot":
                        awardRotModel.add(filmModel);
                        break;
                    default:
                        break;
                }
            }
        }
        mFilmsDataModels.add(awardBlauModel);
        mTabColors.add(R.color.films_award_blue);
        mTabImages.add(R.drawable.blue);
        mFilmsDataModels.add(awardGelbModel);
        mTabColors.add(R.color.films_award_yellow);
        mTabImages.add(R.drawable.yellow);
        mFilmsDataModels.add(awardGruenModel);
        mTabColors.add(R.color.films_award_green);
        mTabImages.add(R.drawable.green);
        mFilmsDataModels.add(awardLilaModel);
        mTabColors.add(R.color.films_award_purple);
        mTabImages.add(R.drawable.purple);
        mFilmsDataModels.add(awardPinkModel);
        mTabColors.add(R.color.films_award_pink);
        mTabImages.add(R.drawable.pink);
        mFilmsDataModels.add(awardRotModel);
        mTabColors.add(R.color.films_award_red);
        mTabImages.add(R.drawable.red);

        mFilmsRecyclerViewFragments.clear();
        for (int i = 0; i < mFilmsDataModels.size(); ++i) {
            mFilmsRecyclerViewFragments.add(null);
        }
        notifyDataSetChanged();
    }
}
