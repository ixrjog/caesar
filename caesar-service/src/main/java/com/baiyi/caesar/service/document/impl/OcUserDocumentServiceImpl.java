package com.baiyi.caesar.service.document.impl;

import com.baiyi.caesar.domain.generator.caesar.OcUserDocument;
import com.baiyi.caesar.mapper.caesar.OcUserDocumentMapper;
import com.baiyi.caesar.service.document.OcUserDocumentService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/13 2:07 下午
 * @Version 1.0
 */
@Service
public class OcUserDocumentServiceImpl implements OcUserDocumentService {

    @Resource
    private OcUserDocumentMapper ocUserDocumentMapper;

    @Override
    public OcUserDocument queryOcUserDocumentByUserIdAndType(int userId, int docType) {
        Example example = new Example(OcUserDocument.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("docType", docType);
        PageHelper.startPage(1, 1);
        return ocUserDocumentMapper.selectOneByExample(example);
    }

    @Override
    public OcUserDocument queryOcUserDocumentById(int id) {
        return ocUserDocumentMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcUserDocument(OcUserDocument ocUserDocument) {
        ocUserDocumentMapper.insert(ocUserDocument);
    }

    @Override
    public void updateOcUserDocument(OcUserDocument ocUserDocument) {
        ocUserDocumentMapper.updateByPrimaryKey(ocUserDocument);
    }

}
