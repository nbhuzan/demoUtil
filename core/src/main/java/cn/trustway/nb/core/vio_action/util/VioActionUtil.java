package cn.trustway.nb.core.vio_action.util;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.trustway.nb.core.vio_action.model.VioActionModel;

/**
 * Created by huzan on 2017/11/9.
 * 描述：
 */

public class VioActionUtil {
    public static final int KEY_TYPE_KEY = 0; //wfxw
    public static final int KEY_TYPE_NAME = 1; //wfnr

    /**
     * 创建时间：2017/11/9
     * 创建者：huzan
     * 描述： 根据关键字过滤排序违法行为列表
     *
     * @param vioActionModelList 违法行为原始数据列表
     * @param key                关键字
     * @param keyType            关键字类别
     */
    public static List<VioActionModel> filterVioActionList(@NonNull List<VioActionModel> vioActionModelList, String key, int keyType) {
        List<VioActionModel> tempList = new ArrayList<>();
        for (VioActionModel vioActionModel : vioActionModelList) {
            if (keyType == KEY_TYPE_KEY && vioActionModel.getWfxw().contains(key)) {
                tempList.add(vioActionModel);
            } else if (keyType == KEY_TYPE_NAME && vioActionModel.getWfnr().contains(key)) {
                tempList.add(vioActionModel);
            }
        }

        Collections.sort(tempList, (vioActionModel, vioActionModel1) -> {
            Integer wfxw = Integer.parseInt(vioActionModel.getWfxw());
            Integer wfxw1 = Integer.parseInt(vioActionModel1.getWfxw());
            Integer index = 0;
            Integer index1 = 0;
            if (keyType == KEY_TYPE_NAME) {
                index = vioActionModel.getWfnr().indexOf(key);
                index1 = vioActionModel.getWfnr().indexOf(key);
            } else if (keyType == KEY_TYPE_KEY) {
                index = vioActionModel.getWfxw().indexOf(key);
                index1 = vioActionModel.getWfxw().indexOf(key);
            }
            if (index.equals(index1)) {
                return wfxw.compareTo(wfxw1);
            } else {
                return index.compareTo(index1);
            }
        });
        return tempList;
    }
}
