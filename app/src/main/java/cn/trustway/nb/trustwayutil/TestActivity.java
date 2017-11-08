package cn.trustway.nb.trustwayutil;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.trustway.nb.core.view.DialogIdCardIdInput;

/**
 * Created by huzan on 2017/11/7.
 * 描述：
 */

public class TestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FastClickHlper.isFastClick();
//        new DialogCarNumInput.Build(this,"")
//                .setFzjg("浙江")
//                .create();
        new DialogIdCardIdInput.Build(this)
                .setLastIdcard("123")
                .create();
    }
}
