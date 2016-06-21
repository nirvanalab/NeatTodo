package com.task.vidhurvoora.neattodo;

import java.io.Serializable;

/**
 * Created by vidhurvoora on 6/19/16.
 * Class for each Todoitem
 */
public class TodoItem implements Serializable {
    public String itemContent;
    public int itemPosition;
    public long itemId;

    public TodoItem(String content,int pos, long id){
        this.itemContent = content;
        this.itemPosition = pos;
        this.itemId = id;
    }
    public TodoItem(String content, int pos) {
        this.itemContent = content;
        this.itemPosition = pos;
    }
}
