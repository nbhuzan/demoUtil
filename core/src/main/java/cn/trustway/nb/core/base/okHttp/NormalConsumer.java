package cn.trustway.nb.core.base.okHttp;

/**
 * Created by huzan on 2017/11/10.
 * 描述：
 */

public interface NormalConsumer<T> {
    void accept(T t);
}
