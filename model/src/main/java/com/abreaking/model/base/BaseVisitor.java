package com.abreaking.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.abreaking.model.core.JModel;

import java.util.Date;

public class BaseVisitor <M extends BaseVisitor<M>> extends JModel<M> implements IBean {
    public static final String CACHE_NAME = "visitor";
    public static final String METADATA_TYPE = "visitor";

    public void removeCache(Object key){
        if(key == null) return;
        CacheKit.remove(CACHE_NAME, key);
    }

    public void putCache(Object key,Object value){
        CacheKit.put(CACHE_NAME, key, value);
    }

    public M getCache(Object key){
        return CacheKit.get(CACHE_NAME, key);
    }

    public M getCache(Object key,IDataLoader dataloader){
        return CacheKit.get(CACHE_NAME, key, dataloader);
    }



    @Override
    public boolean equals(Object o) {
        if(o == null){ return false; }
        if(!(o instanceof BaseVisitor<?>)){return false;}

        BaseVisitor<?> m = (BaseVisitor<?>) o;
        if(m.getId() == null){return false;}

        return m.getId().compareTo(this.getId()) == 0;
    }


    public void setId(String id) {
        set("id", id);
    }

    public String getId() {
        return get("id");
    }

    public void setCookieId(String cookieId){
        set("cookie_id",cookieId);
    }
    public String getCookieId(){
        return get("cookie_id");
    }
    public void setIp(String ip){
        set("ip",ip);
    }

    public String getIp(){
        return get("ip");
    }

    public void setRegion(String region){
        set("region",region);
    }

    public String getRegion(){
        return get("region");
    }

    public void setCity(String city){
        set("city",city);
    }

    public String getCity(){
        return get("city");
    }

    public void setAgent(String agent){
        set("agent",agent);
    }
    public String getAgent(){
        return get("agent");
    }

    public void setVisitTime(Date visit_time){
        set("visit_time",visit_time);
    }
    public Date getVisitTime(){
        return get("visit_time");
    }

    public void setPages(String page){
        set("pages",page);
    }

    public String getPages(){
        return get("pages");
    }

}
