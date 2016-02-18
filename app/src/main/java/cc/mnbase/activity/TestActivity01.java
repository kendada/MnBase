package cc.mnbase.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-02-15
 * Time: 09:13
 * Version 1.0
 */

public class TestActivity01 extends AppCompatActivity {

    private String tag = TestActivity01.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onData();

        onMoreData();
    }

    public void onData(){
        String path = Environment.DIRECTORY_RINGTONES;
        Log.i(tag, "**** " + path);
    }

    public void onMoreData(){

        if(Environment.getExternalStorageState().endsWith(Environment.MEDIA_MOUNTED)){
            Log.i(tag, " *** SD卡可用 *** ");
        } else {
            Log.i(tag, " &&& SD卡不可用 &&& ");
        }

    }


}
