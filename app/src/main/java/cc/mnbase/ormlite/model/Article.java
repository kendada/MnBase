package cc.mnbase.ormlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-16
 * Time: 16:07
 * Version 1.0
 */

@DatabaseTable(tableName = "tab_article")
public class Article {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String title;

    @DatabaseField
    private String content;

    @DatabaseField(canBeNull = true, foreign = true, columnName = "user_id")
    private User user;

    public Article() {

    }

    public Article(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Article [" +
                " id = " + id +
                " title = " + title +
                " content = " + content +
                " User = " + user +
                " ]";
    }
}
