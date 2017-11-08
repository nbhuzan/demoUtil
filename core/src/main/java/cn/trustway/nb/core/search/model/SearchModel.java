package cn.trustway.nb.core.search.model;

import java.io.Serializable;

/**
 * Created by huzan on 2017/11/8.
 * 描述：搜索类型
 */

public class SearchModel implements Serializable {
    private String menu;
    private int keyboardType;

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public int getKeyboardType() {
        return keyboardType;
    }

    public void setKeyboardType(int keyboardType) {
        this.keyboardType = keyboardType;
    }

}
