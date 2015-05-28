package de.uni_weimar.m18.backupfestival.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.List;

import de.uni_weimar.m18.backupfestival.FestivalApplication;
import de.uni_weimar.m18.backupfestival.R;
import de.uni_weimar.m18.backupfestival.models.EventModel;
import de.uni_weimar.m18.backupfestival.models.FilmModel;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

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
public class WpJsonApi {
    // TODO: set correct endpoint for final version!!!
    public static final String ENDPOINT =
            FestivalApplication.getContext().getString(R.string.API_ENDPOINT);
            //"http://backup-festival.de/2014/wp-json";
    private static final String LOG_TAG = WpJsonApi.class.getSimpleName();

    private final WpJsonService mWebService;

    public static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create(); // 2014-05-30T11:05:21

    public WpJsonApi() {
        Cache cache = null;
        OkHttpClient okHttpClient = null;

        try {
            File cacheDir = new File(
                    FestivalApplication.getContext().getCacheDir().getPath(), "data.json");
            cache = new Cache(cacheDir, 10 * 1024 * 1024);
            okHttpClient = new OkHttpClient();
            okHttpClient.setCache(cache);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error during OkHttpClient init: " + e.getMessage());
            // TODO: do something meaningful? - File error handling?
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setClient(new OkClient(okHttpClient))
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Cache-Control", "public, max-age=" + 60 * 60 * 4);
                    }
                })
                .build();
        mWebService = restAdapter.create(WpJsonService.class);
    }


    public interface WpJsonService {
        @GET("/posts")
        Observable<List<FilmModel>> getFilms(@Query("type[]") String type,
                                             @Query("filter[posts_per_page]") int posts_per_page);

        @GET("/posts")
        Observable<List<EventModel>> getEvents(@Query("type[]") String type,
                                               @Query("filter[posts_per_page]") int posts_per_page);
    }





    public Observable<List<FilmModel>> fetchFilms() {
        return mWebService.getFilms("film", 300);
    }
    public Observable<List<EventModel>> fetchEvents() {
        return mWebService.getEvents("event", 300);
    }
}
