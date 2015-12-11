package cc.mnbase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import cc.mn.BaseActivity;
import cc.mnbase.R;
import cc.mnbase.fragment.AsyncTaskFragment;
import cc.mnbase.fragment.CircleProgressBarFragment;
import cc.mnbase.fragment.LruImageFragment;
import cc.mnbase.fragment.MainFragment;
import cc.mnbase.fragment.SocketDemoFragment;
import cc.mnbase.fragment.TestQueueFragment;
import cc.mnbase.fragment.comm.CommentIconViewFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) !=0){
            finish();
            return;
        }

        getSupportFragmentManager().beginTransaction().add(R.id.container, new LruImageFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void t(){

    }
}
