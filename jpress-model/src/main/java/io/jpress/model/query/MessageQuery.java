package io.jpress.model.query;

import com.jfinal.plugin.activerecord.Page;
import io.jpress.model.Message;

import java.math.BigInteger;

public class MessageQuery extends JBaseQuery {

    private static final Message DAO = new Message();
    private static final MessageQuery QUERY = new MessageQuery();

    public static MessageQuery me() {
        return QUERY;
    }

    /**
     * 分页查询，一个留言下面的所有son留言 循环迭代查出来嘛
     * @return
     */
    public Page<Message> paginatewithSon(){
        return null;
    }
    //查询出所有的没有 parent_id 的Message
    public Page<Message> paginatewithNoParent(int pageNumber, int pageSize){
        String select = "SELECT m.*  ";
        String where = " FROM blog_message m WHERE m.parent_id IS NULL AND m.status = 'normal' ORDER BY m.`created` DESC";
        Page<Message> paginate = DAO.paginate(pageNumber, pageSize, select, where);
        return paginate;
    }

    public Page<Message> paginatewithSon(int pageNumber, int pageSize, BigInteger parentId){
        String select = "SELECT m.*  ";
        String where = " FROM blog_message m WHERE m.parent_id = "+parentId+" AND m.status = 'normal' ORDER BY m.`created` DESC";
        Page<Message> paginate = DAO.paginate(pageNumber, pageSize, select, where);
        return paginate;
    }


}
