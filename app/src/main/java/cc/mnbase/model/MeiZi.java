package cc.mnbase.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MeiZi implements Parcelable {
    private String who;

    private String publishedAt;

    private String desc;

    private String type;

    private String url;

    private boolean used;

    private String objectId;

    private String createdAt;

    private String updatedAt;

    public MeiZi(String who, String publishedAt, String desc, String type, String url,
                 boolean used, String objectId, String createdAt, String updatedAt) {
        this.who = who;
        this.publishedAt = publishedAt;
        this.desc = desc;
        this.type = type;
        this.url = url;
        this.used = used;
        this.objectId = objectId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setWho(String who){
        this.who = who;
    }
    public String getWho(){
        return this.who;
    }
    public void setPublishedAt(String publishedAt){
        this.publishedAt = publishedAt;
    }
    public String getPublishedAt(){
        return this.publishedAt;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public String getDesc(){
        return this.desc;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setUsed(boolean used){
        this.used = used;
    }
    public boolean getUsed(){
        return this.used;
    }
    public void setObjectId(String objectId){
        this.objectId = objectId;
    }
    public String getObjectId(){
        return this.objectId;
    }
    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }
    public String getCreatedAt(){
        return this.createdAt;
    }
    public void setUpdatedAt(String updatedAt){
        this.updatedAt = updatedAt;
    }
    public String getUpdatedAt(){
        return this.updatedAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.who);
        dest.writeString(this.publishedAt);
        dest.writeString(this.desc);
        dest.writeString(this.type);
        dest.writeString(this.url);
        dest.writeByte(used ? (byte) 1 : (byte) 0);
        dest.writeString(this.objectId);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
    }

    protected MeiZi(Parcel in) {
        this.who = in.readString();
        this.publishedAt = in.readString();
        this.desc = in.readString();
        this.type = in.readString();
        this.url = in.readString();
        this.used = in.readByte() != 0;
        this.objectId = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Creator<MeiZi> CREATOR = new Creator<MeiZi>() {
        public MeiZi createFromParcel(Parcel source) {
            return new MeiZi(source);
        }

        public MeiZi[] newArray(int size) {
            return new MeiZi[size];
        }
    };
}