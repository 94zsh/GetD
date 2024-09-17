package com.future.getd.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivityGestureBinding;
import com.future.getd.jl.ProductManager;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.bean.ButtonFunction;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.ui.view.pop.SelectListPop;
import com.future.getd.utils.SharePreferencesUtil;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GestureActivity extends BaseActivity<ActivityGestureBinding> {
    private DeviceSettings deviceSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ActivityGestureBinding getBinding() {
        return ActivityGestureBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        deviceSettings = SharePreferencesUtil.getInstance().getDevicesByMac(ProductManager.currentDevice.getBleMac());

        binding.ctlLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectListPop selectListPopup = new SelectListPop(GestureActivity.this);
                selectListPopup.setData(getResources().getString(R.string.single_click), getListByMap(ButtonFunction.KEYID_MUSIC_LEFT_DOUBLE));
                selectListPopup.setOnSelectListener(new SelectListPop.OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text,int value) {
//                        tv_left.setText(text);
//                        setListByMap(ButtonFunction.KEYID_MUSIC_LEFT_DOUBLE,value);
//                        if(Producter.isQcyProtocol(deviceSettings.getName())){
//                            QcyManagerHelper.getInstance().getQcyHeadsetClient(address).setButtonFunction(QCYHeadsetClient.KEYID_MUSIC_LEFT_DOUBLE,value);
//                        }else{
//                            if(PodsManager.getInstance().getConnector(address) != null){
//                                PodsManager.getInstance().getConnector(address).setEarFunc(HyPodsConnector.EAR_LEFT,HyPodsConnector.CLICK_DOUBLE,value);
//                            }
//                        }
                    }
                });
                new XPopup.Builder(GestureActivity.this).asCustom(selectListPopup).show();
            }
        });
    }

    private List<SelectListPop.Item> getListByMap(int keyIndex) {
        int index = 0;
        List<SelectListPop.Item> list = new ArrayList<>();
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_none), false, ButtonFunction.FUNID_NONE));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_last), false, ButtonFunction.FUNID_PERVIOUS));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_next), false, ButtonFunction.FUNID_NEXT));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_play_pause), false, ButtonFunction.FUNID_PLAYPAUSE));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_answer),false,ButtonFunction.FUNID_RECEIVE_CALL));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_hand_up),false,ButtonFunction.FUNID_REFUSE_CALL));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_callback),false,ButtonFunction.FUNID_CALLBACK_CALL));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_volume_up), false, ButtonFunction.FUNID_ADD));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_volume_down), false, ButtonFunction.FUNID_SUB));
//        try {
//            ButtonFunction buttonFunction = deviceSettings.getButtonFunction();
//            HashMap<String, Integer> keyFuncs = (HashMap<String, Integer>) buttonFunction.getHashMap();
//            for (String ks : keyFuncs.keySet()) {
//                if (Integer.parseInt(ks) == keyIndex) {
//                    int funID = keyFuncs.get(ks);
//                    for (int i = 0; i < list.size(); i++) {
//                        if (funID == HyProtocolUtils.getLocalFuncIdByHyProtocol(list.get(i).value)) {
//                            index = i;
//                        }
//                    }
////                    index = getIndexByFunId(funID);
//                }
//
//            }
//        } catch (Exception ignore) {
//            LogUtils.e("headset callback onButtonFunctionChange error : " + ignore);
//        }

        if (index < list.size()) {
            list.get(index).isSelect = true;
        }
        return list;
    }
}