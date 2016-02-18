package cc.mnbase.gson.model;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-13
 * Time: 16:21
 * Version 1.0
 */

public class Cache {

    private String name; // 缓存文件名

    private String path; // 缓存路径

    private long time; // 缓存时间

    public Cache(String name, String path, long time) {
        this.name = name;
        this.path = path;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
