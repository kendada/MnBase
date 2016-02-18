package cc.mnbase.zhujie;

import android.os.Bundle;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cc.mn.BaseActivity;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-15
 * Time: 09:24
 * Version 1.0
 *
 * 注解
 */

public abstract class AbsBaseActivity extends BaseActivity {

    private Class<?> clazz;

    protected void onCreate(Bundle savedInstanceState, int layoutResID) {
        super.onCreate(savedInstanceState);

        setContentView(layoutResID);
        clazz = this.getClass();

        byIdContentView();
        byIdViews();
        findViews();
        setListeners();

    }


    private void byIdContentView(){
        FindViewById view = clazz.getAnnotation(FindViewById.class);
        if(view != null){
            int id = view.id();
            boolean clicklis = view.click();
            try {
                Method method = clazz.getMethod("ContentView", int.class);
                method.setAccessible(true);
                method.invoke(this, id);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    private void byIdViews(){
        Field[] fields = clazz.getDeclaredFields();
        if(fields != null){
            for(Field field : fields){
                FindViewById viewById = field.getAnnotation(FindViewById.class);
                int id = viewById.id();

                try {
                    Method method = clazz.getMethod("findViewById", int.class); // 反射
                    Object obj = method.invoke(this, id);
                    field.setAccessible(true);
                    field.set(this, obj);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public abstract void findViews();

    public abstract void setListeners();


}
