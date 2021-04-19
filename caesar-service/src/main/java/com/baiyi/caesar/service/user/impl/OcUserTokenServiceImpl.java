package com.baiyi.caesar.service.user.impl;

import com.baiyi.caesar.domain.generator.caesar.OcUserToken;
import com.baiyi.caesar.mapper.caesar.OcUserTokenMapper;
import com.baiyi.caesar.service.user.OcUserTokenService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/16 10:22 上午
 * @Version 1.0
 */
@Service
public class OcUserTokenServiceImpl implements OcUserTokenService {

    @Resource
    private OcUserTokenMapper ocUserTokenMapper;

    @Override
    public void addOcUserToken(OcUserToken ocUserToken) {
        ocUserTokenMapper.insert(ocUserToken);
    }

    @Override
    public OcUserToken queryOcUserTokenByTokenAndValid(String token) {
        Example example = new Example(OcUserToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true);
        criteria.andEqualTo("token", token);
        PageHelper.startPage(1, 1);
        return ocUserTokenMapper.selectOneByExample(example);
    }

    @Override
    public int countValidOcUserTokenByUsername(String username) {
        Example example = new Example(OcUserToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true);
        criteria.andEqualTo("username", username);
        return ocUserTokenMapper.selectCountByExample(example);
    }

    @Override
    public List<OcUserToken> queryValidOcUserTokenByUsername(String username, int size) {
        Example example = new Example(OcUserToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true);
        criteria.andEqualTo("username", username);
        example.setOrderByClause("create_time");
        PageHelper.startPage(1, size);
        return ocUserTokenMapper.selectByExample(example);
    }

    @Override
    public List<OcUserToken> queryOcUserTokenByValid() {
        Example example = new Example(OcUserToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true);
        return ocUserTokenMapper.selectByExample(example);
    }

    @Override
    public List<OcUserToken> queryOcUserTokenByUsername(String username) {
        Example example = new Example(OcUserToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true);
        criteria.andEqualTo("username", username);
        return ocUserTokenMapper.selectByExample(example);
    }

    @Override
    public void updateOcUserTokenInvalid(OcUserToken ocUserToken) {
        ocUserToken.setValid(false);
        ocUserTokenMapper.updateByPrimaryKey(ocUserToken);
    }

    @Override
    public int checkUserHasResourceAuthorize(String token, String resourceName) {
        return ocUserTokenMapper.checkUserHasResourceAuthorize(token, resourceName);
    }

    @Override
    public int checkUserHasRole(String token, String roleName) {
        return ocUserTokenMapper.checkUserHasRole(token, roleName);
    }


}
