package com.abreaking.blog;


import com.abreaking.blog.constant.WebConst;
import com.abreaking.blog.dto.MetaDto;
import com.abreaking.blog.dto.Types;
import com.abreaking.blog.model.Bo.ArchiveBo;
import com.abreaking.blog.model.Vo.OptionVo;
import com.abreaking.blog.service.IMetaService;
import com.abreaking.blog.service.IOptionService;
import com.abreaking.blog.service.ISiteService;
import com.abreaking.blog.utils.MapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 程序启动后，初始化一系列的缓存
 * @author liwei_paas
 * @date 2019/6/14
 */
@Component
public class ApplicationCache implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationCache.class);

    private MapCache mapCache = MapCache.single();
    @Resource
    IMetaService metaService;
    @Resource
    private ISiteService siteService;
    @Resource
    private IOptionService optionService;

    @Override
    public void afterPropertiesSet()  {
        initMetas();
        initSysOptions();
    }

    /**
     * 初始化t_options的数据
     */
    public void initSysOptions(){
        List<OptionVo> options = optionService.getOptions();
        for (OptionVo vo: options){
            WebConst.initConfig.put(vo.getName(),vo.getValue());
        }

    }

    /**
     * 初始化t_metas的数据
     */
    public void initMetas(){
        mapCache.hset("meta",Types.COLUMN.getType(),metaService.getMetaList(Types.COLUMN.getType(), null, WebConst.MAX_POSTS));
        mapCache.hset("meta",Types.CATEGORY.getType(),metaService.getMetaList(Types.CATEGORY.getType(), null, WebConst.MAX_POSTS));
        mapCache.hset("meta",Types.TAG.getType(),metaService.getMetaList(Types.TAG.getType(), null, WebConst.MAX_POSTS));
        mapCache.hset("meta",Types.ARCHIVE.getType(),getArchives());
    }

    /**
     * 初始化归档数据
     */
    public List<MetaDto> getArchives(){
        List<ArchiveBo> archives = siteService.getArchives();
        List<MetaDto> metaList = new ArrayList<>();
        //归档的日期，最多只展示10个
        int size = archives.size()>10?10:archives.size();

        for (int i=0;i<size;i++){
            ArchiveBo archiveBo = archives.get(i);
            MetaDto meta = new MetaDto();
            meta.setName(archiveBo.getDate());
            meta.setSlug(archiveBo.getDate());
            meta.setType(Types.ARCHIVE.getType());
            meta.setSort(i);
            meta.setCount(Integer.parseInt(archiveBo.getCount()));
            metaList.add(meta);
        }
        //归档多于10个，就写成 xxxx年xx月以前
        if (archives.size()>10){
            MetaDto meta = new MetaDto();
            meta.setName(archives.get(10).getDate()+"以前");
            meta.setSlug(archives.get(10).getDate()+"以前");
            meta.setType(Types.ARCHIVE.getType());
            int count = 0;
            for (int i=10;i<archives.size();i++){
                ArchiveBo bo = archives.get(i);
                count += Integer.parseInt(bo.getCount());
            }
            meta.setCount(count);
            meta.setSort(size+1);
            metaList.add(meta);
        }
        return metaList;
    }
}
