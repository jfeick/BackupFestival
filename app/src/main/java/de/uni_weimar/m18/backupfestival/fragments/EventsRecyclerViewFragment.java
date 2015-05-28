package de.uni_weimar.m18.backupfestival.fragments;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.florent37.materialviewpager.MaterialViewPagerAnimator;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import java.util.List;

import de.uni_weimar.m18.backupfestival.activities.FilmDetailActivity;
import de.uni_weimar.m18.backupfestival.models.EventModel;
import de.uni_weimar.m18.backupfestival.other.OnItemClickListener;
import de.uni_weimar.m18.backupfestival.R;
import de.uni_weimar.m18.backupfestival.views.adapters.EventsAdapter;

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

public class EventsRecyclerViewFragment extends Fragment {

    private List<EventModel> mEvents;

    private List<EventModel> mCurrentEvents;
    private EventsAdapter mEventsAdapter;

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
        View rootView = inflater.inflate(R.layout.fragment_events_recyclerview, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_last_events_recycler);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        mEventsAdapter = new EventsAdapter(getActivity());
        // TODO: readd this for row click listener
        mEventsAdapter.setOnItemClickListener(recyclerRowClickListener);

        mRecyclerView.setHasFixedSize(true);

        // TODO: I have no idea why, but this fixes the issue of the recycler view rendered behind the MaterialViewPager
        mRecyclerView.setAdapter(new RecyclerViewMaterialAdapter(mEventsAdapter, 1));
        //mRecyclerView.setAdapter(mEventsAdapter);

        //MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
        MaterialViewPagerAnimator animator = MaterialViewPagerHelper.getAnimator(getActivity());
        animator.registerRecyclerView(mRecyclerView, null);


        updateAdapter();
        return rootView;
    }

    @Override
    public void onDetach() {
        MaterialViewPagerAnimator animator = MaterialViewPagerHelper.getAnimator(getActivity());
        if(animator != null) {
            animator.unregisterRecyclerView(mRecyclerView);
        }
        mRecyclerView = null;
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static EventsRecyclerViewFragment newInstance() {
        EventsRecyclerViewFragment fragment = new EventsRecyclerViewFragment();
        return fragment;
    }

    private OnItemClickListener recyclerRowClickListener = new OnItemClickListener() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v, int position) {
            EventModel selectedEvent = mCurrentEvents.get(position - 1);

            /*
            Intent filmDetailIntent = new Intent(getActivity(), FilmDetailActivity.class);
            filmDetailIntent.putExtra("position", position);
            filmDetailIntent.putExtra("selected_film", selectedEvent);

            if (selectedEvent.getSwatch() != null) {
                filmDetailIntent.putExtra("swatch_title_text_color",
                        selectedEvent.getSwatch().getTitleTextColor());
                filmDetailIntent.putExtra("swatch_body_text_color",
                        selectedEvent.getSwatch().getBodyTextColor());
                filmDetailIntent.putExtra("swatch_rgb",
                        selectedEvent.getSwatch().getRgb());
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

                    // setup transition to detail activity
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                    coverImage, "cover");

                    //startActivity(filmDetailIntent, options.toBundle());
                    // TODO: fix this!
                    ActivityCompat.startActivity(getActivity(), filmDetailIntent, options.toBundle());
                }
            }
            */
        }
    };


    public void updateEvents(List<EventModel> events) {
        mCurrentEvents = events;
    }

    public void updateAdapter() {
        mEventsAdapter.updateData(mCurrentEvents);
        //mEventsAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }
}
