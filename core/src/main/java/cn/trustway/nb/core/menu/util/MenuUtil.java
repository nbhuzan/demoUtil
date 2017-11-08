package cn.trustway.nb.core.menu.util;

import android.support.annotation.Nullable;

import java.util.List;

import cn.trustway.nb.core.menu.model.MenuModel;

/**
 * Created by huzan on 2017/11/8.
 * 描述：
 */

public class MenuUtil {
    public static final String separator_line = "-";
    /**
     * 创建时间：2017/8/9
     * 创建者：huzan
     * 描述：格式化为menuModel.key+ 分隔符 +menuModel.value形式
     *
     * @param separator 分隔符
     * @param menuModel 实体类数据
     */
    public static String formatKeyValue(@Nullable MenuModel menuModel, String separator) {
        if (menuModel == null || menuModel.getKey() == null) {
            return "";
        }
        return menuModel.getKey() + separator + menuModel.getValue();
    }

    /**
     * 创建时间：2017/8/9
     * 创建者：huzan
     * 描述：格式化为module1.key...+分隔符+module2.value...
     *
     * @param list      menumodel 列表
     * @param separator 分隔符
     */
    public static String formatKeyValue(@Nullable List<MenuModel> list, String separator) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder sbKey = new StringBuilder();
        StringBuilder sbValue = new StringBuilder();
        for (MenuModel d : list) {
            sbKey.append(d.getKey());
            sbValue.append(d.getValue());
        }
        return sbKey.toString() + separator + sbValue.toString();
    }

    /**
     * 创建时间：2017/9/5
     * 创建者：huzan
     * 描述：格式化为module1.key...
     *
     * @param list menumodel 列表
     */
    public static String formatKey(@Nullable List<MenuModel> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder sbKey = new StringBuilder();
        for (MenuModel d : list) {
            sbKey.append(d.getKey());
        }
        return sbKey.toString();
    }


    /**
     * 创建时间：2017/7/24
     * 创建者：huzan
     * 描述：格式化为module1.key,module2.key...+分隔符+module2.value,module2.value...
     *
     * @param list      menumodel 列表
     * @param separator 分隔符
     */
    public static String list2keyValue(List<MenuModel> list, String separator) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder sbKey = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            MenuModel d = list.get(i);
            sb.append(d.getValue());
            sbKey.append(d.getKey());
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }

        return sbKey.toString() + separator + sb.toString();
    }
}
