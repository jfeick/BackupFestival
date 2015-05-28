package de.uni_weimar.m18.backupfestival.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import java.util.List;

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
public class EventModel implements Serializable {

    private Number id;
    private String content;
    private String excerpt;
    private String title;
    private Terms terms;

    @SerializedName("wpcf-zeit-beginn") private List<String> zeit_beginn;
    @SerializedName("wpcf-zeit-ende") private List<String> zeit_ende;

    public String getDateString() {
        if (terms != null && terms.datum != null)
            return terms.datum.get(0).getName();
        return "";
    }

    public String getStartTimeString() {
        return zeit_beginn.get(0);
    }

    public String getEndTimeString() {
        return zeit_ende.get(0);
    }

    public String getLocationString() {
        if (terms != null && terms.location != null) {
            return terms.location.get(0).getName();
        }
        return "";
    }

    public Terms getTerms() {
        return terms;
    }

    public void setTerms(Terms terms) {
        this.terms = terms;
    }

    public Number getId() {
        return id;
    }

    public void setId(Number id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getZeit_beginn() {
        return zeit_beginn;
    }

    public void setZeit_beginn(List<String> zeit_beginn) {
        this.zeit_beginn = zeit_beginn;
    }

    public List<String> getZeit_ende() {
        return zeit_ende;
    }

    public void setZeit_ende(List<String> zeit_ende) {
        this.zeit_ende = zeit_ende;
    }

    public class Terms implements Serializable {
        private List<Datum> datum;
        private List<Location> location;

        public List<Datum> getDatum() {
            return datum;
        }

        public void setDatum(List<Datum> datum) {
            this.datum = datum;
        }

        public List<Location> getLocation() {
            return location;
        }

        public void setLocation(List<Location> location) {
            this.location = location;
        }
    }

    public class Datum implements Serializable {
        private String name;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public class Location implements Serializable {
        private String name;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

}

