package com.stdnull.v2api.mode;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chen on 2017/8/20.
 */

public class V2ExBean implements Parcelable{
    private @SerializedName("id") int id;
    private @SerializedName("title") String title;
    private @SerializedName("url") String url;
    private @SerializedName("content") String content;
    private @SerializedName("content_rendered") String content_rendered;
    private @SerializedName("replies") int replies;
    private @SerializedName("member") MemberBean member;
    private @SerializedName("node") NodeBean node;
    private @SerializedName("created") int created;
    private @SerializedName("last_modified") int last_modified;
    private @SerializedName("last_touched")  int last_touched;

    protected V2ExBean(Parcel in) {
        id = in.readInt();
        title = in.readString();
        url = in.readString();
        content = in.readString();
        content_rendered = in.readString();
        replies = in.readInt();
        created = in.readInt();
        last_modified = in.readInt();
        last_touched = in.readInt();
    }

    public static final Creator<V2ExBean> CREATOR = new Creator<V2ExBean>() {
        @Override
        public V2ExBean createFromParcel(Parcel in) {
            return new V2ExBean(in);
        }

        @Override
        public V2ExBean[] newArray(int size) {
            return new V2ExBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent_rendered() {
        return content_rendered;
    }

    public void setContent_rendered(String content_rendered) {
        this.content_rendered = content_rendered;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public NodeBean getNode() {
        return node;
    }

    public void setNode(NodeBean node) {
        this.node = node;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(int last_modified) {
        this.last_modified = last_modified;
    }

    public int getLast_touched() {
        return last_touched;
    }

    public void setLast_touched(int last_touched) {
        this.last_touched = last_touched;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeString(content);
        parcel.writeString(content_rendered);
        parcel.writeInt(replies);
        parcel.writeInt(created);
        parcel.writeInt(last_modified);
        parcel.writeInt(last_touched);
    }
}
