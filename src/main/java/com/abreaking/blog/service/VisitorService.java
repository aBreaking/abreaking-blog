package com.abreaking.blog.service;

import com.abreaking.blog.utils.visitor.Visitor;

import java.util.List;

/**
 * liwei
 * @author liwei_paas
 * @date 2019/10/14
 */
public interface VisitorService {

    void insert(Visitor visitor);

    int insertBatch(List<Visitor> visitors);
}
