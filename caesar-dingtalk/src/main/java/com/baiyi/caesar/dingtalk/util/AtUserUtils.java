package com.baiyi.caesar.dingtalk.util;

import com.baiyi.caesar.common.util.RegexUtils;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.google.common.base.Joiner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/15 4:46 下午
 * @Version 1.0
 */
public class AtUserUtils {

    public static List<OcUser> acqAtUsers(OcUser ocUser, List<OcUser> userList) {
        List<OcUser> atUsers = userList.stream().filter(e -> tryAtUser(ocUser, e.getPhone())).collect(Collectors.toList());
        atUsers.add(ocUser);
        return atUsers;
    }

    private static boolean tryAtUser(OcUser ocUser, String phone) {
        if (!RegexUtils.isPhone(phone)) return false;
        return !phone.equals(ocUser.getPhone());
    }


    /**
     * 取 Dingtalk At 手机列表
     *
     * @param userList
     * @return @134********,@189******
     */
    public static String convertAt(List<OcUser> userList) {
        if (CollectionUtils.isEmpty(userList))
            return "";
        List<OcUser> atUserList = userList.stream().filter(e -> RegexUtils.isPhone(e.getPhone())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(atUserList))
            return "";
        return Joiner.on(",").join(atUserList.stream().map(e -> "@" + e.getPhone()).collect(Collectors.toList()));
    }


    /**
     * 转换 At 列表
     *
     * @param userList
     * @return "at": {
     * "atMobiles": [
     * "1825718XXXX"
     * ], "isAtAll": false
     * }
     */
    public static String convertAtMobiles(List<OcUser> userList) {
        if (CollectionUtils.isEmpty(userList))
            return "";
        List<OcUser> atUserList = userList.stream().filter(e -> RegexUtils.isPhone(e.getPhone())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(atUserList))
            return "";
        String atMobiles = Joiner.on(",").join(atUserList.stream().map(e -> Joiner.on(",").join("\"", e.getPhone(), "\"")).collect(Collectors.toList()));
        return Joiner.on("").join(",", "\"at\": {", "\"atMobiles\": [", atMobiles, "]", "}");
    }
}
