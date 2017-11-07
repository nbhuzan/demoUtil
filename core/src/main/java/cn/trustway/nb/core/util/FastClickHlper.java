package cn.trustway.nb.core.util;

/**
 * Created by huzan on 2017/11/7.
 * 描述：防止快速点击
 */

public class FastClickHlper {
    private static long lastClickTime = 0;

    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        long CRITICAL = 500;
        if ( time - lastClickTime < CRITICAL) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
