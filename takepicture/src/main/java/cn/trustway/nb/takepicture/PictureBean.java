package cn.trustway.nb.takepicture;

import java.io.Serializable;

/**
 * Created by huzan on 2018/6/16.
 * 描述：
 */

public class PictureBean implements Serializable {
    private String path;
    private boolean isLocal;

    public PictureBean() {}

    public PictureBean(String path) {
        this.path = path;
    }

    public PictureBean(String path, boolean isLocal) {
        this.path = path;
        this.isLocal = isLocal;
    }



    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }
}
