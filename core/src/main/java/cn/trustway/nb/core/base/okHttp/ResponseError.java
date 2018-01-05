package cn.trustway.nb.core.base.okHttp;

import java.io.Serializable;

/**
 * Created by huzan on 2017/11/10.
 * 描述：
 */

public class ResponseError implements Serializable {
    private int code ;
    private String msg;
    private String url;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
