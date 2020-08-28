package com.baiyi.caesar.common.base;

/**
 * @Author baiyi
 * @Date 2020/8/27 2:06 下午
 * @Version 1.0
 */
public enum AndroidReinforceChannelType {

    yyb("yyb", "腾讯应用宝"),
    huawei("huawei", "华为"),
    xiaomi("xiaomi", "小米"),
    oppo("oppo", "oppo手机"),
    vivo("vivo", "vivo"),
    baidu("baidu", "百度"),
    bmhy("bmhy", "斑马会员"),
    ceshi("ceshi", "测试渠道");

    private String code;
    private String desc;

    AndroidReinforceChannelType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getName(String code) {
        for (AndroidReinforceChannelType channelType : AndroidReinforceChannelType.values()) {
            if (channelType.getCode().equals(code)) {
                return channelType.getDesc();
            }
        }
        return "undefined";
    }
}
