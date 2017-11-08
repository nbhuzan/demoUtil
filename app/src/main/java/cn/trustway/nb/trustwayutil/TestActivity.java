package cn.trustway.nb.trustwayutil;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.trustway.nb.core.menu.view.DialogMenu;
import cn.trustway.nb.core.menu.model.MenuModel;

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
//        new DialogIdCardIdInput.Build(this)
//                .setLastIdcard("123")
//                .create();
        List<MenuModel> menuModels = new ArrayList<>();
        MenuModel menuModel = new MenuModel();
        menuModel.setKey("1");
        menuModel.setValue("nihao");
        menuModels.add(menuModel);
        menuModel = new MenuModel();
        menuModel.setKey("1");
        menuModel.setValue("nihao");
        menuModels.add(menuModel);
        menuModel = new MenuModel();
        menuModel.setKey("1");
        menuModel.setValue("nihao");
        menuModels.add(menuModel);
        new DialogMenu.Build(this)
                .setList(menuModels)
                .create();


    }
}
