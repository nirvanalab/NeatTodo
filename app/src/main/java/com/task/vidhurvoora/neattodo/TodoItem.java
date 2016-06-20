package com.task.vidhurvoora.neattodo;

import java.io.Serializable;

/**
 * Created by vidhurvoora on 6/19/16.
 */
public class TodoItem implements Serializable {
    public String itemContent;
    public int itemPosition;

    public TodoItem(String content, int pos) {
        this.itemContent = content;
        this.itemPosition = pos;
    }
}
