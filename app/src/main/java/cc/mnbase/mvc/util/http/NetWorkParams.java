package cc.mnbase.mvc.util.http;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-01-18
 * Time: 18:37
 * Version 1.0
 */

public class NetWorkParams {

    private ConcurrentHashMap<String, String> strParams;

    private ConcurrentHashMap<String, File> fileParams;

    public NetWorkParams(){
        strParams = new ConcurrentHashMap<>();
        fileParams = new ConcurrentHashMap<>();
    }

    public void addParams(String key, String value){
        strParams.put(key, value);
    }

    public void addParams(String key, File value){
        fileParams.put(key, value);
    }
}
