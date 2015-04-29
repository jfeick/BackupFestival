package de.uni_weimar.m18.backupfestival.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import de.uni_weimar.m18.backupfestival.FestivalApplication;
import de.uni_weimar.m18.backupfestival.models.ImageListModel;
import de.uni_weimar.m18.backupfestival.models.ImageModel;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import rx.Observable;

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
public class UnsplashApi {
    public static final String ENDPOINT = "http://wallsplash.lanora.io";
    private final UnsplashService mWebService;

    public static Gson gson = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .create(); // 2015-01-18 15:48:56

    public UnsplashApi() {
        Cache cache = null;
        OkHttpClient okHttpClient = null;

        try {
            File cacheDir = new File(
                    FestivalApplication.getContext().getCacheDir().getPath(), "pictures.json");
            cache = new Cache(cacheDir, 10 * 1024 * 1024);
            okHttpClient = new OkHttpClient();
            okHttpClient.setCache(cache);
        } catch (Exception e) {
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
        mWebService = restAdapter.create(UnsplashService.class);
    }

    public interface UnsplashService {
        @GET("/pictures")
        Observable<ImageListModel> listImages();
    }

    public interface RandomUnsplashService {
        @GET("/random")
        ImageModel random();
    }

    public Observable<ImageListModel> fetchImages() {
        return mWebService.listImages();
    }

    // keep the filtered array so we can reuse it
    private ArrayList<ImageModel> featured = null;

    public ArrayList<ImageModel> filterFeatured(ArrayList<ImageModel> images) {
        if (featured == null) {
            ArrayList<ImageModel> list = new ArrayList<ImageModel>(images);
            for (Iterator<ImageModel> it = list.iterator(); it.hasNext(); ) {
                if (it.next().getFeatured() != 1) {
                    it.remove();
                }
            }
            featured = list;
        }
        return featured;
    }


    public static int countFeatured(ArrayList<ImageModel> images) {
        int count = 0;
        for (ImageModel image : images) {
            if (image.getFeatured() == 1) {
                count = count + 1;
            }
        }
        return count;
    }

    public ArrayList<ImageModel> filterCategory(ArrayList<ImageModel> images, int filter) {
        ArrayList<ImageModel> list = new ArrayList<ImageModel>(images);
        for (Iterator<ImageModel> it = list.iterator(); it.hasNext(); ) {
            if ((it.next().getCategory() & filter) != filter) {
                it.remove();
            }
        }
        return list;
    }

    public static int countCategory(ArrayList<ImageModel> images, int filter) {
        int count = 0;
        for (ImageModel image : images) {
            if ((image.getCategory() & filter) == filter) {
                count = count + 1;
            }
        }
        return count;
    }
}
