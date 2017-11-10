package cn.trustway.nb.frame.model;

import java.io.Serializable;

/**
 * Created by huzan on 2017/11/10.
 * 描述：
 */

public class TabModel implements Serializable {
    private String text;
    private int icon;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
