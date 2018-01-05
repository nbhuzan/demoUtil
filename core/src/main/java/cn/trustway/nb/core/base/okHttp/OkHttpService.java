package cn.trustway.nb.core.base.okHttp;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by huzan on 2017/7/19.
 * 描述：okhttp操作类
 */
public class OkHttpService {

    private static final int TIME_OUT = 30;
    private static OkHttpClient mOkHttpClient;
    private final String EMPTY_MSG = "服务器连接失败";
    private final int NETWORK_ERROR = -100;
    private static final int CODE_JSON_ERROR = -101;
    private static final String MSG_JSON_ERROR = "数据解析失败";
    public static final int CODE_OTHER_ERROR = -102;
    private static final String MSG_OTHER_ERROR = "数据返回失败";

    private String url;  //请求的url


    static {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.hostnameVerifier((hostname, session) -> true);
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.followRedirects(true);
        mOkHttpClient = okHttpClientBuilder.build();
    }

    OkHttpService(String url, Request request) {
        this(url, request,null);
    }

    OkHttpService(String url, Request request,NormalConsumer<Response> onSuccess) {
        this(url, request,onSuccess, null);
    }

    OkHttpService(String url, Request request, NormalConsumer<Response> onSuccess, NormalConsumer<ResponseError> onFail) {
        this.url = url;
        make(url, request,  onSuccess, onFail);
    }


    private void make(String url, Request request, NormalConsumer<Response> onSuccess, NormalConsumer<ResponseError> onFail) {
        Call call = mOkHttpClient.newCall(request);

        Handler deliveryHandler = new Handler(Looper.getMainLooper());

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (onFail != null) {
                    deliveryHandler.post(() -> onFail.accept(error(NETWORK_ERROR, EMPTY_MSG)));
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    if (response.code() != 200) {
                        deliveryHandler.post(() -> onFail.accept(error(CODE_OTHER_ERROR, "服务器请求错误：" + response.code())));
                    } else if (onSuccess != null) {
                        deliveryHandler.post(() -> onSuccess.accept(response));
                    }
                } catch (Exception e) {
                    deliveryHandler.post(() -> onFail.accept(error(CODE_OTHER_ERROR, MSG_OTHER_ERROR)));
                }
            }
        });
    }

    /**
     * 创建时间：2017/7/19
     * 创建者：huzan
     * 描述：取消网络请求
     */
    public static void cancleTag(String... urls) {
        for (String url : urls) {
            for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
                if (call.request().tag().equals(url)) {
                    call.cancel();
                }
            }
        }
    }

    /**
     * 创建时间：2017/7/19
     * 创建者：huzan
     * 描述：处理错误返回
     */
    private ResponseError error(int code, String msg) {
        ResponseError model = new ResponseError();
        model.setCode(code);
        model.setMsg(msg);
        model.setUrl(url);
        return model;
    }
}
