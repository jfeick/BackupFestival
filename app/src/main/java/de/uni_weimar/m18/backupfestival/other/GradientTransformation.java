package de.uni_weimar.m18.backupfestival.other;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/**
 * Created by Jan Frederick Eick on 30.04.2015.
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
public class GradientTransformation implements Transformation {

    int startColor = Color.argb(240, 0, 0, 0);
    int endColor = Color.TRANSPARENT;

    public GradientTransformation() {

    }

    public GradientTransformation(int startColor) {
        this.startColor = startColor;
    }

    @Override public Bitmap transform(Bitmap source) {

        int x = source.getWidth();
        int y = source.getHeight();

        Bitmap grandientBitmap = source.copy(source.getConfig(), true);
        Canvas canvas = new Canvas(grandientBitmap);
        //left-top == (0,0) , right-bottom(x,y);
        LinearGradient grad =
                new LinearGradient(x/2, y, x/2, y - y/4, startColor, endColor, Shader.TileMode.CLAMP);
        Paint p = new Paint(Paint.DITHER_FLAG);
        p.setShader(null);
        p.setDither(true);
        p.setFilterBitmap(true);
        p.setShader(grad);
        canvas.drawPaint(p);
        source.recycle();
        return grandientBitmap;
    }

    @Override public String key() {
        return "Gradient";
    }
}