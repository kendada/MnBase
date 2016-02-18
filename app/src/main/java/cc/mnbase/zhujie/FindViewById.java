package cc.mnbase.zhujie;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-15
 * Time: 09:41
 * Version 1.0
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FindViewById {
    int id();
    boolean click() default false;
}
