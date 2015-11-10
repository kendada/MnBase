package cc.mnbase.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 12:42
 * Version 1.0
 */

public class GroupMeiZi implements Parcelable{

    private boolean error;

    private List<MeiZi> resultss;

    public GroupMeiZi(boolean error, List<MeiZi> resultss) {
        this.error = error;
        this.resultss = resultss;
    }

    public void setError(boolean error){
        this.error = error;
    }
    public boolean getError(){
        return this.error;
    }
    public void setResults(List<MeiZi> results){
        this.resultss = results;
    }
    public List<MeiZi> getResults(){
        return this.resultss;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(error ? (byte) 1 : (byte) 0);
        dest.writeList(this.resultss);
    }

    public GroupMeiZi() {
    }

    protected GroupMeiZi(Parcel in) {
        this.error = in.readByte() != 0;
        this.resultss = new ArrayList<MeiZi>();
        in.readList(this.resultss, List.class.getClassLoader());
    }

    public static final Creator<GroupMeiZi> CREATOR = new Creator<GroupMeiZi>() {
        public GroupMeiZi createFromParcel(Parcel source) {
            return new GroupMeiZi(source);
        }

        public GroupMeiZi[] newArray(int size) {
            return new GroupMeiZi[size];
        }
    };
}
