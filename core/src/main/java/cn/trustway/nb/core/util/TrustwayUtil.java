package cn.trustway.nb.core.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by huzan on 2017/11/7.
 * 描述：
 */

public class TrustwayUtil {
    /**
     *创建时间：2017/7/24
     *创建者：huzan
     *描述：关闭io
     */
    public static void closeIO(Closeable... cb){
        for (Closeable closeable : cb) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ignored) {

                }
            }
        }
    }
}
