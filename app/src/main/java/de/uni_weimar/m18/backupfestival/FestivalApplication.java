package de.uni_weimar.m18.backupfestival;

import android.app.Application;
import android.content.Context;

import com.mikepenz.materialdrawer.Drawer;
import com.squareup.leakcanary.LeakCanary;

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
public class FestivalApplication extends Application {
    private static Context context;

    private static Drawer.Result drawer = null;


    public static Context getContext() {
        return context;
    }

    public static Drawer.Result getDrawer() {
        return drawer;
    }

    public static void setDrawer(Drawer.Result drawer) {
        FestivalApplication.drawer = drawer;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LeakCanary.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
