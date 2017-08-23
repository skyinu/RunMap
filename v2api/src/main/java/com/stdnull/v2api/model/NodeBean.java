package com.stdnull.v2api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NodeBean implements Parcelable{
        /**
         * id : 108
         * name : bb
         * title : 宽带症候群
         * title_alternative : Broadband Symptom Complex
         * url : http://www.v2ex.com/go/bb
         * topics : 3207
         * avatar_mini : //v2ex.assets.uxengine.net/navatar/a3c6/5c29/108_mini.png?m=1501496420
         * avatar_normal : //v2ex.assets.uxengine.net/navatar/a3c6/5c29/108_normal.png?m=1501496420
         * avatar_large : //v2ex.assets.uxengine.net/navatar/a3c6/5c29/108_large.png?m=1501496420
         */

        private @SerializedName("id") int id;
        private @SerializedName("name") String name;
        private @SerializedName("title") String title;
        private @SerializedName("title_alternative") String title_alternative;
        private @SerializedName("url") String url;
        private @SerializedName("topics") int topics;
        private @SerializedName("avatar_mini") String avatar_mini;
        private @SerializedName("avatar_normal") String avatar_normal;
        private @SerializedName("avatar_large") String avatar_large;

    protected NodeBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        title = in.readString();
        title_alternative = in.readString();
        url = in.readString();
        topics = in.readInt();
        avatar_mini = in.readString();
        avatar_normal = in.readString();
        avatar_large = in.readString();
    }

    public static final Creator<NodeBean> CREATOR = new Creator<NodeBean>() {
        @Override
        public NodeBean createFromParcel(Parcel in) {
            return new NodeBean(in);
        }

        @Override
        public NodeBean[] newArray(int size) {
            return new NodeBean[size];
        }
    };

    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle_alternative() {
            return title_alternative;
        }

        public void setTitle_alternative(String title_alternative) {
            this.title_alternative = title_alternative;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getTopics() {
            return topics;
        }

        public void setTopics(int topics) {
            this.topics = topics;
        }

        public String getAvatar_mini() {
            return avatar_mini;
        }

        public void setAvatar_mini(String avatar_mini) {
            this.avatar_mini = avatar_mini;
        }

        public String getAvatar_normal() {
            return avatar_normal;
        }

        public void setAvatar_normal(String avatar_normal) {
            this.avatar_normal = avatar_normal;
        }

        public String getAvatar_large() {
            return avatar_large;
        }

        public void setAvatar_large(String avatar_large) {
            this.avatar_large = avatar_large;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(title);
        parcel.writeString(title_alternative);
        parcel.writeString(url);
        parcel.writeInt(topics);
        parcel.writeString(avatar_mini);
        parcel.writeString(avatar_normal);
        parcel.writeString(avatar_large);
    }
}