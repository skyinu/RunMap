package com.stdnull.v2api.mode;

import android.os.Parcel;
import android.os.Parcelable;

public class MemberBean implements Parcelable{
        /**
         * id : 85472
         * username : dingzi
         * tagline : 
         * avatar_mini : //v2ex.assets.uxengine.net/avatar/18e8/9ec0/85472_mini.png?m=1431938120
         * avatar_normal : //v2ex.assets.uxengine.net/avatar/18e8/9ec0/85472_normal.png?m=1431938120
         * avatar_large : //v2ex.assets.uxengine.net/avatar/18e8/9ec0/85472_large.png?m=1431938120
         */

        private int id;
        private String username;
        private String tagline;
        private String avatar_mini;
        private String avatar_normal;
        private String avatar_large;

    protected MemberBean(Parcel in) {
        id = in.readInt();
        username = in.readString();
        tagline = in.readString();
        avatar_mini = in.readString();
        avatar_normal = in.readString();
        avatar_large = in.readString();
    }

    public static final Creator<MemberBean> CREATOR = new Creator<MemberBean>() {
        @Override
        public MemberBean createFromParcel(Parcel in) {
            return new MemberBean(in);
        }

        @Override
        public MemberBean[] newArray(int size) {
            return new MemberBean[size];
        }
    };

    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getTagline() {
            return tagline;
        }

        public void setTagline(String tagline) {
            this.tagline = tagline;
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
        parcel.writeString(username);
        parcel.writeString(tagline);
        parcel.writeString(avatar_mini);
        parcel.writeString(avatar_normal);
        parcel.writeString(avatar_large);
    }
}