package com.abreaking.model.query;

import com.jfinal.plugin.activerecord.Page;
import com.abreaking.model.Visitor;

import java.util.List;

public class VisitorQuery extends JBaseQuery{
    private static final Visitor DAO = new Visitor();
    private static final VisitorQuery QUERY = new VisitorQuery();

    public static VisitorQuery me(){
        return QUERY;
    }

    public Visitor findByCookieId(String cookieId){
        List<Visitor> visitors = DAO.find("select * from blog_visitor where cookie_id=?" , cookieId);
        return visitors.size()>0?visitors.get(0):null;
    }

    public Page<Visitor> paginateByVisitTimeDesc(int pageNumber, int pageSize){
        String select = "select id,region,city,visit_time,pages";
        String where = " from blog_visitor order by visit_time desc";
        return DAO.paginate(pageNumber,pageSize,select,where);
    }
}
