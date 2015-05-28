package de.uni_weimar.m18.backupfestival.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.provider.CalendarContract;
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

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.uni_weimar.m18.backupfestival.FestivalApplication;
import de.uni_weimar.m18.backupfestival.models.EventModel;
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
public class EventsAdapter extends RecyclerView.Adapter<EventsViewHolder> {

    private Context mContext;
    private List<EventModel> mEvents;
    private int mDefaultTextColor;
    private int mDefaultBackgroundColor;

    private OnItemClickListener onItemClickListener;
    private int mDefaultShadowColor;
    private int mDefaultSecondaryTextColor;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public EventsAdapter(Context context) {
        this.mContext = context;
    }
    public EventsAdapter(Context context, List<EventModel> events) {
        this.mContext = context;
        this.mEvents = events;

    }

    public void updateData(List<EventModel> events) {
        this.mEvents = events;
        notifyDataSetChanged();
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);

        // set the context
        this.mContext = parent.getContext();

        // get the colors
        mDefaultTextColor = mContext.getResources().getColor(R.color.text_without_palette);
        mDefaultSecondaryTextColor = mContext.getResources().getColor(R.color.secondary_text_without_palette);
        mDefaultBackgroundColor = mContext.getResources().getColor(R.color.image_without_palette);
        mDefaultShadowColor = mContext.getResources().getColor(R.color.primary);

        return new EventsViewHolder(rowView, onItemClickListener);
    }



    @Override
    public void onBindViewHolder(final EventsViewHolder holder, final int position) {

        final EventModel currentEvent = mEvents.get(position);
        //holder.imageAuthor.setText(Html.fromHtml(currentEvent.getTitle()));
        //holder.imageDate.setText(currentEvent.getSpecial());

        holder.eventTitle.setText(Html.fromHtml(currentEvent.getTitle()));
        holder.eventTime.setText(currentEvent.getStartTimeString() + " - " + currentEvent.getEndTimeString());
        holder.eventLocation.setText(currentEvent.getLocationString());
        // reset colors
        //holder.imageAuthor.setTextColor(mDefaultTextColor);
        //holder.imageDate.setTextColor(mDefaultSecondaryTextColor);
        //holder.imageTextContainer.setBackgroundColor(mDefaultBackgroundColor);
        holder.eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateString = currentEvent.getDateString();
                String startString = currentEvent.getStartTimeString();
                String endString = currentEvent.getEndTimeString();
                String dayString = dateString.replaceAll("\\D+", "");
                int day = Integer.parseInt(dayString);
                DateFormat formatter = new SimpleDateFormat("hh:mm");

                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI);

                try {
                    Date startTimeDate = formatter.parse(startString);
                    Calendar beginTime = Calendar.getInstance();
                    beginTime.setTime(startTimeDate);
                    beginTime.set(Calendar.YEAR, 2015);
                    beginTime.set(Calendar.MONDAY, Calendar.MAY);
                    beginTime.set(Calendar.DAY_OF_MONTH, day);
                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                            beginTime.getTimeInMillis());
                } catch (ParseException e) {

                }
                try {
                    Date endTimeDate = formatter.parse(endString);
                    Calendar endTime = Calendar.getInstance();
                    endTime.setTime(endTimeDate);
                    endTime.set(Calendar.YEAR, 2015);
                    endTime.set(Calendar.MONDAY, Calendar.MAY);
                    endTime.set(Calendar.DAY_OF_MONTH, day);
                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                            endTime.getTimeInMillis());
                } catch (ParseException e) {

                }
                intent.putExtra(CalendarContract.Events.TITLE, currentEvent.getTitle());
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, currentEvent.getLocationString());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //if(mEvents == null)
        //    return 0;

        // NEVER CALLED???
        return mEvents.size();
    }


}

class EventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected final TextView eventTitle;
    protected final TextView eventTime;
    protected final TextView eventLocation;
    protected final IconicsImageView iconTime;
    protected final IconicsImageView iconLocation;

    private final OnItemClickListener onItemClickListener;

    public EventsViewHolder(View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        this.onItemClickListener = onItemClickListener;

        eventTitle = (TextView) itemView.findViewById(R.id.eventTitle);
        eventTime  = (TextView) itemView.findViewById(R.id.eventTime);
        eventLocation = (TextView) itemView.findViewById(R.id.eventLocation);

        iconTime = (IconicsImageView) itemView.findViewById(R.id.iconTime);
        iconTime.setIcon(GoogleMaterial.Icon.gmd_schedule);
        iconTime.setColor(Color.LTGRAY);

        iconLocation = (IconicsImageView) itemView.findViewById(R.id.iconLocation);
        iconLocation.setIcon(GoogleMaterial.Icon.gmd_place);
        iconLocation.setColor(Color.LTGRAY);

        //imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemClickListener.onClick(view, getPosition());
    }
}
