package com.abreaking.blog.service.impl;

import com.abreaking.blog.dao.VisitorVoMapper;
import com.abreaking.blog.service.VisitorService;
import com.abreaking.blog.utils.visitor.Visitor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author liwei_paas
 * @date 2019/10/14
 */
@Service
public class VisitorServiceImpl implements VisitorService {

    @Resource
    VisitorVoMapper visitorVoMapper;

    @Override
    public void insert(Visitor visitor) {

    }

    @Override
    @Transactional
    public int insertBatch(List<Visitor> visitors) {
        int i = 0;
        for (Visitor visitor : visitors){
            visitorVoMapper.insert(visitor);
            i++;
        }
        return i;
    }
}
