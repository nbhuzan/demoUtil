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
import cn.trustway.nb.core.vio_action.model.VioActionModel;
import cn.trustway.nb.core.vio_action.view.DialogAction;
import cn.trustway.nb.core.vio_address.model.RoadModel;
import cn.trustway.nb.core.vio_address.view.DialogAddress;

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
                .setOnClickListener(view -> Toast.makeText(context, "好的，知道了", Toast.LENGTH_SHORT).show())
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
                .setOnClickSubmit(dictionary -> Toast.makeText(context, MenuUtil.formatKeyValue(dictionary, MenuUtil.separator_line), Toast.LENGTH_SHORT).show())
                .create();
    }

    public void menuDouble(View v) {
        new DialogMenu.Build(context, true)
                .setList(menuModels)
                .setSelectList(menuModels.subList(0, 1))
                .setOnSelectSubmit(new DialogMenu.OnSelectSubmit() {
                    @Override
                    public void onSelect(List<MenuModel> list) {
                        Toast.makeText(context, list.size() + "", Toast.LENGTH_SHORT).show();
                    }
                })
                .setTitle("多选测试")
                .create();
    }

    public void search(View v) {
        startActivity(new Intent(this, SearchActivity.class));
    }

    public void letterNumber(View v) {
        new DialogInputLetterNumber.Build(context, "1234")
                .setOnSubmitListener(carNum -> Toast.makeText(context, carNum, Toast.LENGTH_SHORT).show()).create();
    }

    public void road(View v) {
        List<RoadModel> roadList = new ArrayList<>();
        List<RoadModel> roadSectionList = new ArrayList<>();
        List<RoadModel> collectList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            RoadModel roadModel = new RoadModel();
            roadModel.setDldm(i + "0000");
            roadModel.setDlmc("道路" + i);
            for (int i1 = 0; i1 < 10; i1++) {
                RoadModel roadModel1 = new RoadModel();
                roadModel1.setDldm(i + "0000");
                roadModel1.setLddm(i1 + "000");
                roadModel1.setLdmc("道路"+i+"路段" + i1);
                roadSectionList.add(roadModel1);
            }
            roadList.add(roadModel);
            if (i < 5) {
                collectList.add(roadModel);
            }
        }

        new DialogAddress.Build(context)
                .setCollectList(collectList)
                .setRoadList(roadList)
                .setRoadSectionList(roadSectionList)
                .setOnOperationListener(new DialogAddress.OnOperationListener() {
                    @Override
                    public void onSubmit(String dldm, String lddm, String dlms, String location) {
                        Toast.makeText(context, location, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAddCollect(RoadModel roadModel) {
                        Toast.makeText(context, roadModel.getDldm(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDeleteCollect(RoadModel roadModel) {
                        Toast.makeText(context, roadModel.getDldm(), Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }

    public void vioAction(View view){
        List<VioActionModel> list = new ArrayList<>();
        List<VioActionModel> collectList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            VioActionModel vioActionModel = new VioActionModel();
            vioActionModel.setWfxw("违法行为"+i);
            vioActionModel.setWfnr("违法内容"+i);
            list.add(vioActionModel);
            if(i<5){
                collectList.add(vioActionModel);
            }
        }

        new DialogAction.Build(context)
                .isSelectMore(false)
                .setCollectVioActionList(collectList)
                .setVioActionList(list)
                .setOnSelectOne(vioWfdm -> {
                    Toast.makeText(context, vioWfdm.getWfnr(), Toast.LENGTH_SHORT).show();
                })
                .setOnOperationListener(new DialogAction.OnOperationListener() {
                    @Override
                    public void onAddCollect(VioActionModel vioActionModel) {
                        Toast.makeText(context, vioActionModel.getWfnr(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDeleteCollect(VioActionModel vioActionModel) {
                        Toast.makeText(context, vioActionModel.getWfnr(), Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }

     public void vioActionMore(View view){
        List<VioActionModel> list = new ArrayList<>();
        List<VioActionModel> collectList = new ArrayList<>();
        List<VioActionModel> selectList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            VioActionModel vioActionModel = new VioActionModel();
            vioActionModel.setWfxw("违法行为"+i);
            vioActionModel.setWfnr("违法内容"+i);
            list.add(vioActionModel);
            if(i<5){
                collectList.add(vioActionModel);
            }

        }
        selectList.add(list.get(0));

        new DialogAction.Build(context)
                .isSelectMore(true)
                .setCollectVioActionList(collectList)
                .setVioActionList(list)
                .setSelectMax(2)
                .setSelectList(selectList)
                .setOnSelectMore(vioWfdms -> Toast.makeText(context, vioWfdms.size()+"选中", Toast.LENGTH_SHORT).show())
                .setOnOperationListener(new DialogAction.OnOperationListener() {
                    @Override
                    public void onAddCollect(VioActionModel vioActionModel) {
                        Toast.makeText(context, vioActionModel.getWfnr(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDeleteCollect(VioActionModel vioActionModel) {
                        Toast.makeText(context, vioActionModel.getWfnr(), Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }

    public void frame(View v){
         startActivity(new Intent(context,FrameActivity.class));
    }

    public void testRxAndroid(View v){
        startActivity(new Intent(context,TextRxActivity.class));
    }


    public void testQrcode(View view) {
        startActivity(new Intent(context,QrcodeActivity.class));
    }
}
