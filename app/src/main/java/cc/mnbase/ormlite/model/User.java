package cc.mnbase.ormlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-16
 * Time: 14:26
 * Version 1.0
 */

@DatabaseTable(tableName = "tab_user")
public class User {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "signature")
    private String signature;

    @DatabaseField(columnName = "sex")
    private String sex;

    @DatabaseField(columnName = "age")
    private int age;

    @DatabaseField(columnName = "phoneNum")
    private int phoneNum;

    @DatabaseField(columnName = "icon")
    private String icon;

    public User(){

    }

    public User(String name) {
        this.name = name;
    }

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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "[User id = " + id + ", name = " + name + ", age = " + age +
                ", signature = " + signature + ", phoneNum = " + phoneNum +
                ", icon = " + icon +
                "]";
    }
}
