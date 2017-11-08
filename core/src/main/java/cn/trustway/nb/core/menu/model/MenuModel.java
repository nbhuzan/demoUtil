package cn.trustway.nb.core.menu.model;

import java.io.Serializable;

/**
 * Created by huzan on 2017/11/8.
 * 描述：dialogmenu使用的实体类
 */

public class MenuModel implements Serializable {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
