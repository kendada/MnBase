package cc.mnbase.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User: (1203596603@qq.com)
 * Date: 2015-10-22
 * Time: 15:53
 * Version 1.0
 */

public class Emm implements Parcelable{

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String litpic;
    private int click;
    private String description;
    private int lit_width;
    private int lit_height;

    public Emm(){

    }

    public Emm(String id, String title, String litpic, int click, String description,
               int lit_width, int lit_height){
        this.setClick(click);
        this.setDescription(description);
        this.setId(id);
        this.setLit_height(lit_height);
        this.setLit_width(lit_width);
        this.setLitpic(litpic);
        this.setTitle(title);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLitpic() {
        return litpic;
    }
    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }
    public int getClick() {
        return click;
    }
    public void setClick(int click) {
        this.click = click;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getLit_width() {
        return lit_width;
    }
    public void setLit_width(int lit_width) {
        this.lit_width = lit_width;
    }
    public int getLit_height() {
        return lit_height;
    }
    public void setLit_height(int lit_height) {
        this.lit_height = lit_height;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.litpic);
        dest.writeInt(this.click);
        dest.writeString(this.description);
        dest.writeInt(this.lit_width);
        dest.writeInt(this.lit_height);
    }

    protected Emm(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.litpic = in.readString();
        this.click = in.readInt();
        this.description = in.readString();
        this.lit_width = in.readInt();
        this.lit_height = in.readInt();
    }

    public static final Creator<Emm> CREATOR = new Creator<Emm>() {
        public Emm createFromParcel(Parcel source) {
            return new Emm(source);
        }

        public Emm[] newArray(int size) {
            return new Emm[size];
        }
    };
}
