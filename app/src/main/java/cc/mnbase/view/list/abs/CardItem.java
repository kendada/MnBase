package cc.mnbase.view.list.abs;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-08
 * Time: 10:22
 * Version 1.0
 */

public class CardItem {

    String imagePath;
    String userName;
    int likeNum;
    int imageNum;

    public CardItem(String userName, String imagePath, int likeNum, int imageNum) {
        this.imagePath = imagePath;
        this.userName = userName;
        this.likeNum = likeNum;
        this.imageNum = imageNum;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getImageNum() {
        return imageNum;
    }

    public void setImageNum(int imageNum) {
        this.imageNum = imageNum;
    }
}
