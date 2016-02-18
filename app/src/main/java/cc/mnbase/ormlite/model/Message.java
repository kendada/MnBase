package cc.mnbase.ormlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-17
 * Time: 11:25
 * Version 1.0
 */

@DatabaseTable(tableName = "tab_message")
public class Message {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String content;


}
