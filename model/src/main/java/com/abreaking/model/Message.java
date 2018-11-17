package com.abreaking.model;

import com.abreaking.model.base.BaseComment;
import com.abreaking.model.core.Table;
import com.abreaking.model.query.CommentQuery;

/**
 * created by liwei
 * 博客上的留言
 * 跟Commment内的属性差不多，多了一个son_id
 */
@Table(tableName = "message", primaryKey = "id")
public class Message extends BaseComment<Message> {
    private static final long serialVersionUID = 1L;

    public static final String TYPE_MESSAGE = "message";
    public static String STATUS_DELETE = "delete";
    public static String STATUS_DRAFT = "draft";
    public static String STATUS_NORMAL = "normal";

    public Integer getSonNumber(){
        return get("son_number");
    }
    public void setSonNumber(int sonNumber){
        set("son_number",sonNumber);
    }

    public String getUsername() {
        return get("username");
    }

    public boolean isDelete() {
        return STATUS_DELETE.equals(getStatus());
    }


    public boolean updateCommentCount() {
        long count = CommentQuery.me().findCountByParentIdInNormal(getId());
        if (count > 0) {
            setCommentCount(count);
            return this.update();
        }
        return false;
    }

    public static void main(String args[])	{


    }
}
