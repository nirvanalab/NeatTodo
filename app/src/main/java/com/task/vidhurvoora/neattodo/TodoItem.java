package com.task.vidhurvoora.neattodo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by vidhurvoora on 6/19/16.
 * Class for each Todoitem
 */
public class TodoItem implements Serializable {
    public String itemContent;
    public int itemPosition;
    public long priority;
    public long itemId;
    public Date itemDate;

    public TodoItem(String content){
        this.itemContent = content;
    }

    public TodoItem(String content,Date date,long priority){
        this.itemContent = content;
        this.itemDate = date;
        this.priority = priority;
    }

    public TodoItem(String content, long id,Date date,long priority){
        this.itemContent = content;
        this.itemId = id;
        this.itemDate = date;
        this.priority = priority;
    }

    public TodoItem(String content, int pos) {
        this.itemContent = content;
        this.itemPosition = pos;
    }
}
