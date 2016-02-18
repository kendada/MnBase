package cc.mnbase.dbdb;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-02-16
 * Time: 10:50
 * Version 1.0
 */

public class People {

    public int id;

    public String name;

    public int age;

    public String sex;

    public long money;

    public boolean isGradu; //是否大学毕业

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public boolean isGradu() {
        return isGradu;
    }

    public void setGradu(boolean isGradu) {
        this.isGradu = isGradu;
    }
}
