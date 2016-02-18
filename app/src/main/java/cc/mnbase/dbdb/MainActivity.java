package cc.mnbase.dbdb;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import cc.mnbase.dbdb.db.DBManager;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-02-16
 * Time: 11:28
 * Version 1.0
 */

public class MainActivity extends AppCompatActivity {

    private String tag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBManager dbManager = new DBManager(this, "db_test_name", 1, People.class);

        Log.i(tag, " *** " + dbManager.findAllData(People.class));


        People people = new People();
        people.name = "山野书生";
        people.age = 26;
        people.sex = "男";
        people.money = 999887777;
        people.isGradu = true;

        dbManager.insert(people);

        List<People> list = dbManager.findAllData(People.class);
        for(People p : list){
            Log.i(tag, " ### " + p.name);
        }

        Handler handler = new Handler();


    }


}
