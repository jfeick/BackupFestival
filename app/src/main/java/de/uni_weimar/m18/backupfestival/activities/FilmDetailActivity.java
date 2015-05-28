package de.uni_weimar.m18.backupfestival.activities;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Transition;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import de.uni_weimar.m18.backupfestival.R;
import de.uni_weimar.m18.backupfestival.fragments.FilmsRecyclerViewFragment;
import de.uni_weimar.m18.backupfestival.models.FilmModel;
import de.uni_weimar.m18.backupfestival.other.CustomAnimatorListener;
import de.uni_weimar.m18.backupfestival.other.CustomTransitionListener;
import de.uni_weimar.m18.backupfestival.other.PaletteTransformation;

/**
 * Created by Jan Frederick Eick on 01.05.2015.
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
public class FilmDetailActivity extends AppCompatActivity {
    private static final int ACTIVITY_CROP = 13451;
    private static final int ACTIVITY_SHARE = 13452;

    private static final int ANIMATION_DURATION_SHORT = 150;
    private static final int ANIMATION_DURATION_MEDIUM = 300;
    private static final int ANIMATION_DURATION_LONG = 450;
    private static final int ANIMATION_DURATION_EXTRA_LONG = 850;

    private View mTitleContainer;
    private View mTitlesContainer;
    private FilmModel mSelectedImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_film_detail);

        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // items from intent
        final int position = getIntent().getIntExtra("position", 0);
        mSelectedImage = (FilmModel) getIntent().getSerializableExtra("selected_film");

        mTitlesContainer = findViewById(R.id.activity_detail_titles);

        // title container
        mTitleContainer  = findViewById(R.id.activity_detail_title_container);
        mTitleContainer.setScaleY(0);
        mTitleContainer.setPivotY(0);

        // define toolbar as the shared element
        final Toolbar toolbar = (Toolbar) findViewById(R.id.activity_detail_toolbar);
        setSupportActionBar(toolbar);

        // get the imageHeader and set the coverImage
        final ImageView image = (ImageView) findViewById(R.id.activity_detail_image);
        Bitmap imageCoverBitmap = FilmsRecyclerViewFragment.photoCache.get(position);
        //safety check to prevent nullPointer in the palette if the detailActivity was in the background for too long
        if (imageCoverBitmap == null || imageCoverBitmap.isRecycled()) {
            this.finish();
            return;
        }
        image.setImageBitmap(imageCoverBitmap);

        //override text
        setTitle("");

        if (Build.VERSION.SDK_INT >= 21) {
            image.setTransitionName("cover");
            // Add a listener to get noticed when the transition ends to animate the fab button
            getWindow().getSharedElementEnterTransition().addListener(new CustomTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    super.onTransitionEnd(transition);
                    animateActivityStart();
                }
            });
        } else {
            ViewPropertyAnimator propertyAnimator = image.animate().setStartDelay(0)
                    .scaleX(1).scaleY(1);
            propertyAnimator.setDuration(ANIMATION_DURATION_LONG).start();
            animateActivityStart();
        }

        //check if we already had the colors during click
        int swatch_title_text_color = getIntent().getIntExtra("swatch_title_text_color", -1);
        int swatch_body_text_color = getIntent().getIntExtra("swatch_body_text_color", -1);
        int swatch_rgb = getIntent().getIntExtra("swatch_rgb", -1);

        if (swatch_rgb != -1 && swatch_title_text_color != -1 && swatch_body_text_color != -1) {
            setColors(swatch_title_text_color, swatch_body_text_color, swatch_rgb);
        } else {
            // Generate palette colors
            Palette palette = PaletteTransformation.getPalette(imageCoverBitmap);
            if (palette != null) {
                Palette.Swatch s = palette.getVibrantSwatch();
                if (s == null) {
                    s = palette.getDarkVibrantSwatch();
                }
                if (s == null) {
                    s = palette.getLightVibrantSwatch();
                }
                if (s == null) {
                    s = palette.getMutedSwatch();
                }

                if (s != null) {
                    setColors(s.getTitleTextColor(), s.getBodyTextColor(), s.getRgb());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean success = resultCode == -1;

        if (requestCode == ACTIVITY_CROP) {
            //animate the first elements
            //animateCompleteFirst(success);
        } else if (requestCode == ACTIVITY_SHARE) {
            //animate the first elements
            //animateCompleteFirst(success);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * animate the start of the activity
     */
    private void animateActivityStart() {
        ViewPropertyAnimator showTitleAnimator = mTitleContainer.animate().setStartDelay(0)
                .scaleX(1).scaleY(1);
        showTitleAnimator.setListener(new CustomAnimatorListener() {

            @Override
            public void onAnimationEnd(Animator animation) {

                super.onAnimationEnd(animation);
                mTitlesContainer.startAnimation(AnimationUtils.loadAnimation(FilmDetailActivity.this, R.anim.alpha_on));
                mTitlesContainer.setVisibility(View.VISIBLE);

            }
        });

        showTitleAnimator.start();
    }

    private void setColors(int titleTextColor, int bodyTextColor, int rgb) {
        mTitleContainer.setBackgroundColor(rgb);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(titleTextColor);
        }
        //getWindow().setNavigationBarColor(vibrantSwatch.getRgb());

        TextView titleTV = (TextView) mTitleContainer.findViewById(R.id.activity_detail_title);
        titleTV.setTextColor(titleTextColor);
        titleTV.setText(mSelectedImage.getTitle());

        TextView subtitleTV = (TextView) mTitleContainer.findViewById(R.id.activity_detail_subtitle);
        subtitleTV.setTextColor(bodyTextColor);
        //subtitleTV.setText(mSelectedImage.getSpecial());
        subtitleTV.setText(Html.fromHtml(mSelectedImage.getContent(), null, null));

        ((TextView) mTitleContainer.findViewById(R.id.activity_detail_subtitle))
                .setTextColor(bodyTextColor);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
