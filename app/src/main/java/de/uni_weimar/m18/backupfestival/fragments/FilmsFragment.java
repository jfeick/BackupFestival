package de.uni_weimar.m18.backupfestival.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.florent37.materialviewpager.MaterialViewPagerHeaderView;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.util.List;

import de.uni_weimar.m18.backupfestival.other.OnItemClickListener;
import de.uni_weimar.m18.backupfestival.R;
import de.uni_weimar.m18.backupfestival.activities.FilmDetailActivity;
import de.uni_weimar.m18.backupfestival.activities.MainActivity;
import de.uni_weimar.m18.backupfestival.models.FilmModel;
import de.uni_weimar.m18.backupfestival.network.WpJsonApi;
import de.uni_weimar.m18.backupfestival.views.adapters.FilmAdapter;
import retrofit.RetrofitError;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tr.xip.errorview.ErrorView;
import tr.xip.errorview.RetryListener;

/**
 * Created by Jan Frederick Eick on 28.04.2015.
 * Copyright 2015 Jan Frederick Eick
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class FilmsFragment extends Fragment {

    public static SparseArray<Bitmap> photoCache = new SparseArray<>(1);

    private WpJsonApi mApi = new WpJsonApi();
    private List<FilmModel> mImages;

    private List<FilmModel> mCurrentImages;
    private FilmAdapter mFilmAdapter;

    private RecyclerView mRecyclerView;

    //private ProgressBar mProgressBar;
    //private ErrorView mErrorView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_films, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_last_films_recycler);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        mFilmAdapter = new FilmAdapter();
        // TODO: readd this for row click listener
        mFilmAdapter.setOnItemClickListener(recyclerRowClickListener);

        mRecyclerView.setHasFixedSize(true);

        // TODO: I have no idea why, but this fixes the issue of the recycler view rendered behind the MaterialViewPager
        mRecyclerView.setAdapter(new RecyclerViewMaterialAdapter(mFilmAdapter, 1));
        //mRecyclerView.setAdapter(mFilmAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

        updateAdapter();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static FilmsFragment newInstance() {
        FilmsFragment fragment = new FilmsFragment();
        return fragment;
    }


    private OnItemClickListener recyclerRowClickListener = new OnItemClickListener() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v, int position) {
            FilmModel selectedFilm = mCurrentImages.get(position - 1);

            Intent filmDetailIntent = new Intent(getActivity(), FilmDetailActivity.class);
            filmDetailIntent.putExtra("position", position);
            filmDetailIntent.putExtra("selected_film", selectedFilm);

            if (selectedFilm.getSwatch() != null) {
                filmDetailIntent.putExtra("swatch_title_text_color",
                        selectedFilm.getSwatch().getTitleTextColor());
                filmDetailIntent.putExtra("swatch_body_text_color",
                        selectedFilm.getSwatch().getBodyTextColor());
                filmDetailIntent.putExtra("swatch_rgb",
                        selectedFilm.getSwatch().getRgb());
            }

            ImageView coverImage = (ImageView) v.findViewById(R.id.item_image_img);
            if(coverImage == null) {
                coverImage = (ImageView) ((View) v.getParent()).findViewById(R.id.item_image_img);
            }

            if (Build.VERSION.SDK_INT >= 21) {
                if (coverImage.getParent() != null) {
                    ((ViewGroup) coverImage.getParent()).setTransitionGroup(false);
                }
            }

            if (coverImage != null && coverImage.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) coverImage.getDrawable()).getBitmap(); // wtf? ew
                if (bitmap != null && !bitmap.isRecycled()) {
                    photoCache.put(position, bitmap);

                    // setup transition to detail activity
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                    coverImage, "cover");

                    //startActivity(filmDetailIntent, options.toBundle());
                    // TODO: fix this!
                    ActivityCompat.startActivity(getActivity(), filmDetailIntent, options.toBundle());
                }
            }
        }
    };


    public void updateFilms(List<FilmModel> films) {
        mCurrentImages = films;
    }

    public void updateAdapter() {
        mFilmAdapter.updateData(mCurrentImages);
        mFilmAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }
}
