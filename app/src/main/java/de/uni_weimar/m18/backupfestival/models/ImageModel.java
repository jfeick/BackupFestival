package de.uni_weimar.m18.backupfestival.models;

import android.support.v7.graphics.Palette;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
public class ImageModel implements Serializable{
    private static final DateFormat sdf = SimpleDateFormat.getDateInstance();

    private String color;
    private String image_src;
    private String author;
    private Date date;
    private Date modified_date;
    private float ratio;
    private int width;
    private int height;
    private int featured;
    private int category;

    transient private Palette.Swatch swatch;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUrl() {
        return image_src;
    }

    public String getHighResImage(int minWidth, int minHeight) {
        String url = image_src + "?fm=png";

        // minimize processing costs of unsplash image hosting
        // try to eliminate the white line on top
        if (minWidth > 0 && minHeight > 0) {
            float phoneRatio = (1.0f * minWidth) / minHeight;
            if (phoneRatio < getRatio() ) {
                if (minHeight < 1080) {
                    url = url + "&h=" + 1080;
                }
            } else {
                if (minWidth < 1920) {
                    url = url + "&w=" + 1920;
                }
            }
        }
        return url;
    }

    public String getImageSrc(int screenWidth) {
        return image_src + "?q=75&fm=jpg&w=" + 500; // TODO: bullshit value - see original
    }

    public void setImageSrc(String image_src) {
        this.image_src = image_src;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public String getReadableDate() {
        if (date != null) {
            return sdf.format(date);
        } else {
            return "";
        }
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getModifiedDate() {
        return modified_date;
    }

    public String getReadableModified_Date() {
        if (modified_date != null) {
            return sdf.format(modified_date);
        } else {
            return "";
        }
    }

    public void setModifiedDate(Date modified_date) {
        this.modified_date = modified_date;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight() {
        this.height = height;
    }

    public Palette.Swatch getSwatch() {
        return swatch;
    }

    public void setSwatch(Palette.Swatch swatch) {
        this.swatch = swatch;
    }

    public int getFeatured() {
        return featured;
    }

    public void setFeatured(int featured) {
        this.featured = featured;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
