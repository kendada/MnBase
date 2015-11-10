package cc.mnbase.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * User: 靳世坤(1203596603@qq.com)
 * Date: 2015-08-17
 * Time: 21:55
 * Version 1.0
 */

public class Girl implements Parcelable {

    private String avatarUrl;
    private String cardUrl;
    private String city;
    private int height;
    private List<String> imgList;
    private String link; //主页
    private String realName; //名字
    private int totalFanNum; //粉丝数
    private int totalFavorNum;
    private String type;
    private int userId;
    private int weight; //体重

    public Girl(String avatarUrl, String cardUrl, String city, int height, List<String> imgList, String link,
                String realName, int totalFanNum, int totalFavorNum, String type, int userId, int weight) {
        this.avatarUrl = avatarUrl;
        this.cardUrl = cardUrl;
        this.city = city;
        this.height = height;
        this.imgList = imgList;
        this.link = link;
        this.realName = realName;
        this.totalFanNum = totalFanNum;
        this.totalFavorNum = totalFavorNum;
        this.type = type;
        this.userId = userId;
        this.weight = weight;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCardUrl() {
        return cardUrl;
    }

    public void setCardUrl(String cardUrl) {
        this.cardUrl = cardUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getTotalFanNum() {
        return totalFanNum;
    }

    public void setTotalFanNum(int totalFanNum) {
        this.totalFanNum = totalFanNum;
    }

    public int getTotalFavorNum() {
        return totalFavorNum;
    }

    public void setTotalFavorNum(int totalFavorNum) {
        this.totalFavorNum = totalFavorNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatarUrl);
        dest.writeString(this.cardUrl);
        dest.writeString(this.city);
        dest.writeInt(this.height);
        dest.writeStringList(this.imgList);
        dest.writeString(this.link);
        dest.writeString(this.realName);
        dest.writeInt(this.totalFanNum);
        dest.writeInt(this.totalFavorNum);
        dest.writeString(this.type);
        dest.writeInt(this.userId);
        dest.writeInt(this.weight);
    }

    protected Girl(Parcel in) {
        this.avatarUrl = in.readString();
        this.cardUrl = in.readString();
        this.city = in.readString();
        this.height = in.readInt();
        this.imgList = in.createStringArrayList();
        this.link = in.readString();
        this.realName = in.readString();
        this.totalFanNum = in.readInt();
        this.totalFavorNum = in.readInt();
        this.type = in.readString();
        this.userId = in.readInt();
        this.weight = in.readInt();
    }

    public static final Creator<Girl> CREATOR = new Creator<Girl>() {
        public Girl createFromParcel(Parcel source) {
            return new Girl(source);
        }

        public Girl[] newArray(int size) {
            return new Girl[size];
        }
    };
}
