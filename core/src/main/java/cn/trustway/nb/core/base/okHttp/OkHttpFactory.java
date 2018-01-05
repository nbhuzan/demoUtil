package cn.trustway.nb.core.base.okHttp;


import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by huzan on 2017/7/19.
 * 描述：okhttp工厂类
 */

public class OkHttpFactory {

    public static void create(String url, Request request) {

        new OkHttpService(url, request);
    }


    public static void create(String url, Request request, NormalConsumer<Response> onSuccess) {

        new OkHttpService(url, request, onSuccess);
    }

    public static void create(String url, Request request, NormalConsumer<Response> onSuccess, NormalConsumer<ResponseError> onFail) {
        new OkHttpService(url, request, onSuccess, onFail);
    }


}
