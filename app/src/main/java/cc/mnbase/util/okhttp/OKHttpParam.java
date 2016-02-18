package cc.mnbase.util.okhttp;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-14
 * Time: 15:40
 * Version 1.0
 */

public class OKHttpParam {

    private Map<String, String> paramMap;

    private Map<String, File> fileParamMap;

    public OKHttpParam(){
        paramMap = new HashMap<>();
        fileParamMap = new HashMap<>();
    }

    public void putParam(String key, String value){
        paramMap.put(key, value);
    }

    public void putParam(String key, File value){
        fileParamMap.put(key, value);
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public Map<String, File> getFileParamMap() {
        return fileParamMap;
    }
}
