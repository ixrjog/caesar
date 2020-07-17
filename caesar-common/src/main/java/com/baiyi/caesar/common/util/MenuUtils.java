package com.baiyi.caesar.common.util;

import com.baiyi.caesar.domain.generator.caesar.OcAuthMenu;
import com.baiyi.caesar.domain.vo.auth.menu.MenuVO;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * @Author baiyi
 * @Date 2020/4/23 9:59 上午
 * @Version 1.0
 */
public class MenuUtils {

    public static ArrayList<MenuVO> buildMenu(OcAuthMenu ocAuthMenu) {
        return new GsonBuilder().create().fromJson(ocAuthMenu.getMenu(), new TypeToken<ArrayList<MenuVO>>() {
        }.getType());
    }
}
