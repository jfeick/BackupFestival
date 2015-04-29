package de.uni_weimar.m18.backupfestival.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import de.uni_weimar.m18.backupfestival.R;
import de.uni_weimar.m18.backupfestival.models.ImageModel;
import de.uni_weimar.m18.backupfestival.network.UnsplashApi;
import tr.xip.errorview.ErrorView;

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

    private UnsplashApi mApi = new UnsplashApi();

    private ArrayList<ImageModel> mImages;
    private ArrayList<ImageModel> mCurrentImages;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ErrorView mErrorView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_films, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_last_films_recycler);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.fragment_films_progress);
        mErrorView = (ErrorView) rootView.findViewById(R.id.fragment_films_error_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        // TODO: set image adapter stuff

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showAll() {
        if (mImages != null) {
            updateAdapter(mImages);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.GONE);


        }
    }
}
