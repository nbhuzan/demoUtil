package cn.trustway.nb.trustwayutil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.trustway.nb.core.input_cardid.view.DialogIdCardIdInput;
import cn.trustway.nb.core.input_carnum.view.DialogCarNumInput;
import cn.trustway.nb.core.input_letter_number.view.DialogInputLetterNumber;
import cn.trustway.nb.core.menu.model.MenuModel;
import cn.trustway.nb.core.menu.util.MenuUtil;
import cn.trustway.nb.core.menu.view.DialogMenu;
import cn.trustway.nb.core.prompt.view.DialogPrompt;

/**
 * Created by huzan on 2017/11/7.
 * 描述：
 */

public class TestActivity extends Activity {
    private Context context;
    List<MenuModel> menuModels = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        context = this;
//        FastClickHlper.isFastClick();

        MenuModel menuModel = new MenuModel();
        menuModel.setKey("1");
        menuModel.setValue("nihao1");
        menuModels.add(menuModel);
        menuModel = new MenuModel();
        menuModel.setKey("2");
        menuModel.setValue("nihao2");
        menuModels.add(menuModel);
        menuModel = new MenuModel();
        menuModel.setKey("3");
        menuModel.setValue("nihao3");
        menuModels.add(menuModel);

    }

    public void prompt(View v) {
        new DialogPrompt.Build(context)
                .haveCancelButton(true)
                .setNeedCheck(true)
                .setStyle(DialogPrompt.STYLE_QUESTION)
                .setTitle("提示")
                .setMsg("你好吗？我是提示框")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "好的，知道了", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }

    public void cardid(View v) {
        new DialogIdCardIdInput.Build(this)
                .setLastIdcard("123")
                .setMustAll(true)
                .setOnSubmitListener(idcard -> Toast.makeText(context, idcard, Toast.LENGTH_SHORT).show())
                .setCanceledOnTouchOutside(true)
                .create();
    }

    public void carnum(View v) {
        new DialogCarNumInput.Build(this, "")
                .setFzjg("浙B")
                .setOnSubmitListener(carNum -> Toast.makeText(context, carNum, Toast.LENGTH_SHORT).show())
                .create();

    }

    public void menu(View v) {

        new DialogMenu.Build(this)
                .setList(menuModels)
                .setTitle("单选测试")
                .setOnClickSubmit(dictionary -> Toast.makeText(context, MenuUtil.formatKeyValue(dictionary,MenuUtil.separator_line), Toast.LENGTH_SHORT).show())
                .create();
    }

    public void menuDouble(View v) {
        new DialogMenu.Build(context,true)
                .setList(menuModels)
                .setSelectList(menuModels.subList(0,1))
                .setOnSelectSubmit(new DialogMenu.OnSelectSubmit() {
                    @Override
                    public void onSelect(List<MenuModel> list) {
                        Toast.makeText(context, list.size()+"", Toast.LENGTH_SHORT).show();
                    }
                })
                .setTitle("多选测试")
                .create();
    }

    public void search(View v){
        startActivity(new Intent(this,SearchActivity.class));
    }

    public void letterNumber(View v){
        new DialogInputLetterNumber.Build(context,"12")
                .setOnSubmitListener(carNum -> Toast.makeText(context, carNum, Toast.LENGTH_SHORT).show()).create();
    }

}
