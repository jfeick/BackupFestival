package de.uni_weimar.m18.backupfestival.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.uni_weimar.m18.backupfestival.other.GradientTransformation;
import de.uni_weimar.m18.backupfestival.other.OnItemClickListener;
import de.uni_weimar.m18.backupfestival.R;
import de.uni_weimar.m18.backupfestival.models.FilmModel;
import de.uni_weimar.m18.backupfestival.other.PaletteTransformation;

/**
 * Created by Jan Frederick Eick on 29.04.2015.
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
public class FilmAdapter extends RecyclerView.Adapter<ImagesViewHolder> {

    private Context mContext;

    private List<FilmModel> mImages;
    private int mScreenWidth;
    private int mDefaultTextColor;
    private int mDefaultBackgroundColor;

    private OnItemClickListener onItemClickListener;
    private int mDefaultShadowColor;
    private int mDefaultSecondaryTextColor;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FilmAdapter() {
    }

    public FilmAdapter(List<FilmModel> images) {
        this.mImages = images;

    }

    public void updateData(List<FilmModel> images) {
        this.mImages = images;
        notifyDataSetChanged();
    }

    @Override
    public ImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);

        // set the context
        this.mContext = parent.getContext();

        // get the colors
        mDefaultTextColor = mContext.getResources().getColor(R.color.text_without_palette);
        mDefaultSecondaryTextColor = mContext.getResources().getColor(R.color.secondary_text_without_palette);
        mDefaultBackgroundColor = mContext.getResources().getColor(R.color.image_without_palette);
        mDefaultShadowColor = mContext.getResources().getColor(R.color.primary);

        // get the screen width
        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;

        return new ImagesViewHolder(rowView, onItemClickListener);
    }



    @Override
    public void onBindViewHolder(final ImagesViewHolder holder, final int position) {

        final FilmModel currentFilm = mImages.get(position);
        holder.imageAuthor.setText(Html.fromHtml(currentFilm.getTitle()));
        holder.imageDate.setText(currentFilm.getSpecial());
        holder.imageView.setImageBitmap(null);

        // reset colors
        holder.imageAuthor.setTextColor(mDefaultTextColor);
        holder.imageDate.setTextColor(mDefaultSecondaryTextColor);
        holder.imageTextContainer.setBackgroundColor(mDefaultBackgroundColor);

        int gradientColor = mContext.getResources().getColor(R.color.primary);
        gradientColor = Color.argb((int)(255/1.5), Color.red(gradientColor), Color.green(gradientColor),
                Color.blue(gradientColor));

        // cancel any loading on this view
        Picasso.with(mContext).cancelRequest(holder.imageView);
        // load image
        Picasso.with(mContext).load(mImages.get(position).getImageSrc())
                //.transform(new GradientTransformation(gradientColor))
                .transform(PaletteTransformation.instance()).into(holder.imageView,
                new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) holder.imageView.getDrawable())
                                .getBitmap();
                        if (bitmap != null && !bitmap.isRecycled()) {
                            Palette palette = PaletteTransformation.getPalette(bitmap);

                            if (palette != null) {
                                Palette.Swatch s= palette.getVibrantSwatch();
                                if (s == null) {
                                    s = palette.getDarkVibrantSwatch();
                                }
                                if (s == null) {
                                    s = palette.getLightVibrantSwatch();
                                }
                                if (s == null) {
                                    s = palette.getMutedSwatch();
                                }

                                if (s != null && position >= 0 && position < mImages.size()) {
                                    if (mImages.get(position) != null) {
                                        mImages.get(position).setSwatch(s);
                                    }

                                    //holder.imageAuthor.setTextColor(s.getTitleTextColor());
                                    holder.imageAuthor.setShadowLayer(1.0f, 2.0f, 2.0f, mDefaultShadowColor);
                                    //holder.imageDate.setTextColor(s.getTitleTextColor());
                                    holder.imageDate.setShadowLayer(1.0f, 2.0f, 2.0f, mDefaultShadowColor);
                                    // TODO: Utils animateViewColor()
                                    holder.imageTextContainer.setBackgroundColor(s.getRgb());
                                }
                            }
                        }

                        // delete the reference
                        bitmap = null;

                        if (Build.VERSION.SDK_INT >= 21) {
                            holder.imageView.setTransitionName("cover" + position);
                        }
                        holder.imageTextContainer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onItemClickListener.onClick(view, position);
                            }
                        });
                    }
                });

        // calculate height of list item so we dont have jumps in the view
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        //int finalHeight = (int) (displayMetrics.widthPixels / currentFilm.getRatio());
        //holder.imageView.setMinimumHeight(finalHeight);
    }

    @Override
    public int getItemCount() {
        //if(mImages == null)
        //    return 0;

        // NEVER CALLED???
        return mImages.size();
    }


}

class ImagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected final FrameLayout imageTextContainer;
    protected final ImageView imageView;
    protected final TextView imageAuthor;
    protected final TextView imageDate;
    private final OnItemClickListener onItemClickListener;

    public ImagesViewHolder(View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        this.onItemClickListener = onItemClickListener;

        imageTextContainer = (FrameLayout) itemView.findViewById(R.id.item_image_text_container);
        imageView = (ImageView) itemView.findViewById(R.id.item_image_img);
        imageAuthor = (TextView) itemView.findViewById(R.id.item_image_author);
        imageDate = (TextView) itemView.findViewById(R.id.item_image_date);

        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemClickListener.onClick(view, getPosition());
    }
}
