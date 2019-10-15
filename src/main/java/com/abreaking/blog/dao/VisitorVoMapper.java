package com.abreaking.blog.dao;

import com.abreaking.blog.utils.visitor.Visitor;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author liwei_paas
 * @date 2019/10/14
 */
@Mapper
public interface VisitorVoMapper {
    int insert(Visitor visitor);
}
