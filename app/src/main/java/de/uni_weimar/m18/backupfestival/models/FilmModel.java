package de.uni_weimar.m18.backupfestival.models;

import android.support.v7.graphics.Palette;

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
public class FilmModel implements Serializable {

    private Number iD;
    private String content;
    private String excerpt;
    private String title;
    private String link;
    private Featured_image featured_image;
    private Terms terms;

    transient private Palette.Swatch swatch;

    public Palette.Swatch getSwatch() {
        return swatch;
    }

    public void setSwatch(Palette.Swatch swatch) {
        this.swatch = swatch;
    }


    public String getImageSrc() {
        if (featured_image != null
                && featured_image.attachment_meta != null
                && featured_image.attachment_meta.sizes != null) {
            if (featured_image.attachment_meta.sizes.medium != null) {
                return featured_image.attachment_meta.sizes.medium.getUrl();
            } else if (featured_image.attachment_meta.sizes.large != null) {
                return featured_image.attachment_meta.sizes.large.getUrl();
            }
        }
        if (featured_image != null && featured_image.source != null) {
            return featured_image.getSource();
        }
        return null;
    }

    public String getSpecial() {
        if(terms != null && terms.special != null) {
            try {
                return terms.special.get(0).getName();
            } catch (Exception e) {
                return "";
            }
        }
        return "";

    }

    public Featured_image getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(Featured_image featured_image) {
        this.featured_image = featured_image;
    }

    public Terms getTerms() {
        return terms;
    }

    public void setTerms(Terms terms) {
        this.terms = terms;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Number getiD() {
        return iD;
    }

    public void setiD(Number iD) {
        this.iD = iD;
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


    public class Terms implements Serializable {
        private List<Award> award;
        private List<Special> special;
        private List<Language> language;

        public List<Award> getAward() {
            return award;
        }

        public void setAward(List<Award> award) {
            this.award = award;
        }

        public List<Special> getSpecial() {
            return special;
        }

        public void setSpecial(List<Special> special) {
            this.special = special;
        }

        public List<Language> getLanguage() {
            return language;
        }

        public void setLanguage(List<Language> language) {
            this.language = language;
        }
    }

    public class Award implements Serializable {
        private String name;
        private String description;
        private String link;

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

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

    public class Special implements Serializable {
        private String name;
        private String description;
        private String link;

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

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

    public class Language implements Serializable {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class Featured_image implements Serializable {
        private String source;
        private Attachment_meta attachment_meta;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Attachment_meta getAttachment_meta() {
            return attachment_meta;
        }

        public void setAttachment_meta(Attachment_meta attachment_meta) {
            this.attachment_meta = attachment_meta;
        }
    }

    public class Attachment_meta implements Serializable {
        public Sizes getSizes() {
            return sizes;
        }

        public void setSizes(Sizes sizes) {
            this.sizes = sizes;
        }

        private Sizes sizes;
    }

    public class Sizes implements Serializable {
        private Thumbnail thumbnail;
        private Medium medium;
        private Large large;
        private Quad_600px quad_600px;
        private Quad_400px quad_400px;

        public Thumbnail getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(Thumbnail thumbnail) {
            this.thumbnail = thumbnail;
        }

        public Medium getMedium() {
            return medium;
        }

        public void setMedium(Medium medium) {
            this.medium = medium;
        }

        public Large getLarge() {
            return large;
        }

        public void setLarge(Large large) {
            this.large = large;
        }

        public Quad_600px getQuad_600px() {
            return quad_600px;
        }

        public void setQuad_600px(Quad_600px quad_600px) {
            this.quad_600px = quad_600px;
        }

        public Quad_400px getQuad_400px() {
            return quad_400px;
        }

        public void setQuad_400px(Quad_400px quad_400px) {
            this.quad_400px = quad_400px;
        }
    }

    public class Thumbnail implements Serializable {
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        private String url;
    }

    public class Medium implements Serializable {
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        private String url;
    }

    public class Large implements Serializable {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class Quad_600px implements Serializable {
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        private String url;
    }

    public class Quad_400px implements Serializable {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

