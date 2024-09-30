package com.future.getd.ui.home;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivityEqSettingBinding;
import com.future.getd.jl.ProductManager;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.adapter.EqAdapter;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.ui.bean.EqItem;
import com.future.getd.ui.view.SpacesItemDecoration;
import com.future.getd.ui.view.seekbar.OnRangeChangedListener;
import com.future.getd.ui.view.seekbar.RangeSeekBar;
import com.future.getd.ui.view.seekbar.VerticalRangeSeekBar;
import com.future.getd.utils.DrawUtil;
import com.future.getd.utils.SharePreferencesUtil;
import com.jieli.bluetooth.bean.base.BaseError;
import com.jieli.bluetooth.bean.device.eq.EqInfo;
import com.jieli.bluetooth.bean.device.eq.EqPresetInfo;
import com.jieli.bluetooth.interfaces.rcsp.callback.BTRcspEventCallback;
import com.jieli.bluetooth.interfaces.rcsp.callback.OnRcspActionCallback;

import java.util.ArrayList;
import java.util.List;

public class EqSettingActivity extends BaseActivity<ActivityEqSettingBinding> implements OnRangeChangedListener {
    private List<EqItem> eqItemList = new ArrayList<>();
    EqAdapter adapter;
    private List<VerticalRangeSeekBar> seekBars = new ArrayList<>();
    private List<TextView> textViews = new ArrayList<>();
    private BTRcspEventCallback btRcspEventCallback;
    private DeviceSettings deviceSettings;
    boolean isSupportCustom = false;//是否支持自定义音效 设备支持,但产品决定不用自定义 按需设置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.bg_main);
    }

    @Override
    protected ActivityEqSettingBinding getBinding() {
        return ActivityEqSettingBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        binding.title.ivBack.setOnClickListener(v->{finish();});
        binding.title.tvTitle.setText(getString(R.string.balance_setting));

        seekBars.add(binding.sb1);
        seekBars.add(binding.sb2);
        seekBars.add(binding.sb3);
        seekBars.add(binding.sb4);
        seekBars.add(binding.sb5);
        seekBars.add(binding.sb6);
        seekBars.add(binding.sb7);
        seekBars.add(binding.sb8);
        seekBars.add(binding.sb9);
        seekBars.add(binding.sb10);
        binding.sb1.setOnRangeChangedListener(this);
        binding.sb2.setOnRangeChangedListener(this);
        binding.sb3.setOnRangeChangedListener(this);
        binding.sb4.setOnRangeChangedListener(this);
        binding.sb5.setOnRangeChangedListener(this);
        binding.sb6.setOnRangeChangedListener(this);
        binding.sb7.setOnRangeChangedListener(this);
        binding.sb8.setOnRangeChangedListener(this);
        binding.sb9.setOnRangeChangedListener(this);
        binding.sb10.setOnRangeChangedListener(this);
        textViews.add(binding.tv1);
        textViews.add(binding.tv2);
        textViews.add(binding.tv3);
        textViews.add(binding.tv4);
        textViews.add(binding.tv5);
        textViews.add(binding.tv6);
        textViews.add(binding.tv7);
        textViews.add(binding.tv8);
        textViews.add(binding.tv9);
        textViews.add(binding.tv10);

        if(!isSupportCustom){
            binding.llBar.setEnabled(false);
            for (VerticalRangeSeekBar seekBar : seekBars) {
                seekBar.setEnabled(false);
            }
        }

        deviceSettings = SharePreferencesUtil.getInstance().getDevicesByClassMac(ProductManager.currentDevice.getClassicMac());
        int eqMode = deviceSettings.getEqMode();
        LogUtils.e("current EQ info : " + deviceSettings.getEqInfo() + " , mode : " + deviceSettings.getEqMode());
        //自然 摇滚 流行 经典 爵士 乡村 自定义
        //onEqPresetChange eqPresetInfo : EqPresetInfo{number=10, eqInfos=[
        // EqInfo{mode=0, isNew=false, value=[-2, 0, 1, 4, 2, -3, -1, 5, -5, 2], freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]},
        // EqInfo{mode=1, isNew=false, value=[-3, 0, 0, 2, 2, 2, 4, 1, -3, 4], freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]},
        // EqInfo{mode=2, isNew=false, value=[-6, -2, 0, 0, 0, 2, 2, 0, -3, 4], freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]},
        // EqInfo{mode=3, isNew=false, value=[-4, 2, 2, 3, 2, 2, 3, 3, -3, 5], freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]},
        // EqInfo{mode=4, isNew=false, value=[-4, 0, 0, 6, 2, 4, 3, 6, -3, 4], freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]},
        // EqInfo{mode=5, isNew=false, value=[-2, -3, 0, 5, 2, 2, 4, 3, -3, 5], freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]},
        // EqInfo{mode=6, isNew=false, value=[0, 0, 0, 0, 0, 0, 0, 0, 0, 0], freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]}],

        EqInfo eqNature = new EqInfo(EqItem.DEV_MODE_NATURAL,new byte[]{-2, 0, 1, 4, 2, -3, -1, 5, -5, 2},new int[]{31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000});
        EqInfo eqRock = new EqInfo(EqItem.DEV_MODE_ROCK,new byte[]{-3, 0, 0, 2, 2, 2, 4, 1, -3, 4},new int[]{31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000});
        EqInfo eqPop = new EqInfo(EqItem.DEV_MODE_POP,new byte[]{-6, -2, 0, 0, 0, 2, 2, 0, -3, 4},new int[]{31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000});
        EqInfo eqClass = new EqInfo(EqItem.DEV_MODE_CLASS,new byte[]{-4, 2, 2, 3, 2, 2, 3, 3, -3, 5},new int[]{31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000});
//        EqInfo eqDance = new EqInfo(EqItem.DEV_MODE_CUSTOM,new byte[]{-3, 3, 0, 6, 2, 2, 4, 5, -3, 4},new int[]{31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000});
//        EqInfo eqBlue = new EqInfo(EqItem.DEV_MODE_CUSTOM,new byte[]{3, 6, 8, 3, -2, 0, 4, 7, 9, 10},new int[]{31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000});
        EqInfo eqJazz = new EqInfo(EqItem.DEV_MODE_JAZZ,new byte[]{-4, 0, 0, 6, 2, 4, 3, 6, -3, 4},new int[]{31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000});
        EqInfo eqVillage = new EqInfo(EqItem.DEV_MODE_VILLAGE,new byte[]{-2, -3, 0, 5, 2, 2, 4, 3, -3, 5},new int[]{31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000});
//        EqInfo eqEle = new EqInfo(EqItem.DEV_MODE_CUSTOM,new byte[]{6, 5, 0, -5, -4, 0, 6, 8, 8, 7},new int[]{31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000});
        EqInfo eqCustom = new EqInfo(EqItem.DEV_MODE_CUSTOM,deviceSettings.getEqInfo().getValue(),new int[]{31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000});

        EqItem custom = new EqItem(EqItem.MODE_CUSTOM,getString(R.string.eq_custom),eqMode == EqItem.MODE_CUSTOM,eqCustom);
        EqItem nature = new EqItem(EqItem.MODE_NATURAL,getString(R.string.eq_nature),eqMode == EqItem.MODE_NATURAL,eqNature);
        EqItem rock = new EqItem(EqItem.MODE_ROCK,getString(R.string.eq_roll),eqMode == EqItem.MODE_ROCK,eqRock);
        EqItem pop = new EqItem(EqItem.MODE_POP,getString(R.string.eq_pop),eqMode == EqItem.MODE_POP,eqPop);
        EqItem classic = new EqItem(EqItem.MODE_CLASS,getString(R.string.eq_class),eqMode == EqItem.MODE_CLASS,eqClass);
        EqItem jazz = new EqItem(EqItem.MODE_JAZZ,getString(R.string.eq_jazz),eqMode == EqItem.MODE_JAZZ,eqJazz);
        EqItem village = new EqItem(EqItem.MODE_VILLAGE,getString(R.string.eq_village),eqMode == EqItem.MODE_VILLAGE,eqVillage);
//        EqItem eqType2 = new EqItem(EqItem.MODE_DANCE,getString(R.string.eq_dance),eqMode == EqItem.MODE_DANCE,eqDance);
//        EqItem eqType3 = new EqItem(EqItem.MODE_BLUE,getString(R.string.eq_blue),eqMode == EqItem.MODE_BLUE,eqBlue);
//        EqItem eqType7 = new EqItem(EqItem.MODE_ELECTRONIC,getString(R.string.eq_eletronic),eqMode == EqItem.MODE_ELECTRONIC,eqEle);

        if(isSupportCustom){
            eqItemList.add(custom);
        }
        eqItemList.add(nature);
        eqItemList.add(rock);
        eqItemList.add(pop);
        eqItemList.add(classic);
        eqItemList.add(jazz);
        eqItemList.add(village);
//        eqItemList.add(eqType7);
//        eqItemList.add(eqType8);
        adapter = new EqAdapter(eqItemList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(EqSettingActivity.this, 3);
        binding.rv.setLayoutManager(gridLayoutManager);
        binding.rv.setAdapter(adapter);
        int space = DrawUtil.dp2px(this,5);
        binding.rv.addItemDecoration(new SpacesItemDecoration(space));
        //控制列数
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return position == 6 ? 2 : 1;
//            }
//        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<EqItem>() {
            @Override
            public void onClick(@NonNull BaseQuickAdapter<EqItem, ?> baseQuickAdapter, @NonNull View view, int position) {
                LogUtils.d("onClick position : " + position);
                EqItem data = eqItemList.get(position);
                updateEqDetail(data.getEqInfo());
                for (int i = 0; i < eqItemList.size(); i++) {
                    if(i == position){
                        eqItemList.get(i).isSelect = true;
                    }else{
                        eqItemList.get(i).isSelect = false;
                    }
                }
                adapter.notifyDataSetChanged();
                deviceSettings.setEqInfo(data.getEqInfo());
                deviceSettings.setEqMode(data.getMode());
                saveToDb();
                setToDevice(data.getEqInfo());
            }
        });
        for (int i = 0; i < eqItemList.size(); i++) {
            EqItem item = eqItemList.get(i);
            int index = item.getMode();
            if(deviceSettings.getEqMode() == index){
                item.isSelect = true;
                updateEqDetail(item.getEqInfo());
            }else{
                item.isSelect = false;
            }
        }
    }

    @Override
    protected void initData() {
        //(EqSettingActivity.java:91).onEqChange: onEqPresetChange eqInfo : EqInfo{mode=6, isNew=false, value=[0, 8, -8, -4, 4, -1, 1, 0, 0, 0], freqs=[31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000]}
        //(EqSettingActivity.java:85).onEqPresetChange: onEqPresetChange eqPresetInfo : EqPresetInfo{number=10,
        //自然 摇滚 流行 经典 爵士 乡村 自定义
        // eqInfos=[EqInfo{mode=0, isNew=false, value=[-4, -3, 0, 3, 2, 2, 0, 3, -3, 2], freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]},
        // EqInfo{mode=1, isNew=false, value=[-3, 0, 0, 2, 2, 2, 4, 1, -3, 4], freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]},
        // EqInfo{mode=2, isNew=false, value=[-6, -2, 0, 0, 0, 2, 2, 0, -3, 4], freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]},
        // EqInfo{mode=3, isNew=false, value=[-4, 2, 2, 3, 2, 2, 3, 3, -3, 5], freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]},
        // EqInfo{mode=4, isNew=false, value=[-4, 0, 0, 6, 2, 4, 3, 6, -3, 4], freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]},
        // EqInfo{mode=5, isNew=false, value=[-2, -3, 0, 5, 2, 2, 4, 3, -3, 5], freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]},
        // EqInfo{mode=6, isNew=false, value=[0, 8, -8, -4, 4, -1, 1, 0, 0, 0], freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]}],
        // freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]}
        //(EqSettingActivity.java:99).onSuccess: onEqPresetChange getEqInfo : true

        //onEqChange: onEqPresetChange eqInfo : EqInfo{mode=2, isNew=false, value=[-6, -2, 0, 0, 0, 2, 2, 0, -3, 4], freqs=[31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000]}
        //添加蓝牙RCSP事件监听器
        btRcspEventCallback = new BTRcspEventCallback() {
            @Override
            public void onEqPresetChange(BluetoothDevice device, EqPresetInfo eqPresetInfo) {
                //此处将会回调均衡器预设值
                LogUtils.i("onEqPresetChange eqPresetInfo : " + eqPresetInfo);
                //onEqPresetChange eqPresetInfo : EqPresetInfo{number=10, eqInfos=[
                // EqInfo{mode=0, isNew=false, value=[-2, 0, 1, 4, 2, -3, -1, 5, -5, 2], freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]},
                // EqInfo{mode=1, isNew=false, value=[-3, 0, 0, 2, 2, 2, 4, 1, -3, 4], freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]},
                // EqInfo{mode=2, isNew=false, value=[-6, -2, 0, 0, 0, 2, 2, 0, -3, 4], freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]},
                // EqInfo{mode=3, isNew=false, value=[-4, 2, 2, 3, 2, 2, 3, 3, -3, 5], freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]},
                // EqInfo{mode=4, isNew=false, value=[-4, 0, 0, 6, 2, 4, 3, 6, -3, 4], freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]},
                // EqInfo{mode=5, isNew=false, value=[-2, -3, 0, 5, 2, 2, 4, 3, -3, 5], freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]},
                // EqInfo{mode=6, isNew=false, value=[0, 0, 0, 0, 0, 0, 0, 0, 0, 0], freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]}],
                // freqs=[180, 230, 100, 300, 2500, 3600, 8000, 850, 6000, 13000]}
            }
            @Override
            public void onEqChange(BluetoothDevice device, EqInfo eqInfo) {
                //此处将会回调均衡器效果信息
                //EqInfo{mode=0, isNew=false, value=[-2, 0, 1, 4, 2, -3, -1, 5, -5, 2], freqs=[31, 63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000]}
                LogUtils.i("onEqChange eqInfo : " + eqInfo);
                int devMode = eqInfo.getMode();
                int uiMode = DevMode2UiMode(devMode);
                deviceSettings.setEqInfo(eqInfo);
                deviceSettings.setEqMode(uiMode);
                for (int i = 0; i < eqItemList.size(); i++) {
                    EqItem item = eqItemList.get(i);
                    if (item.getMode() == uiMode) {
                        item.isSelect = true;
                    }else{
                        item.isSelect = false;
                    }
                }
                adapter.notifyDataSetChanged();

                updateEqDetail(eqInfo);
            }
        };
        rcspController.addBTRcspEventCallback(btRcspEventCallback);
        rcspController.getEqInfo(rcspController.getUsingDevice(), new OnRcspActionCallback<Boolean>() {
            @Override
            public void onSuccess(BluetoothDevice device, Boolean message) {
                //成功回调
                //结果将会在BTRcspEventCallback#onEqPresetChange、BTRcspEventCallback#onEqChange回调
                LogUtils.i("onEqPresetChange getEqInfo : " + message);
            }

            @Override
            public void onError(BluetoothDevice device, BaseError error) {
                //失败回调
                //error - 错误信息
                LogUtils.i("onEqPresetChange getEqInfo onError : " + error);
            }
        });
//        rcspController.configEqInfo(rcspController.getUsingDevice(),e);
    }

    private void updateEqDetail(/*EqData eqData*/EqInfo eqInfo){
//        byte[] details = eqData.getEqData();
        byte[] details = eqInfo.getValue();
        String curProgress = "";
        for (int i = 0; i < details.length; i++) {
            if(i < seekBars.size()){
                int progress = 0;
                if(details[i] < 0){
                    progress = details[i] + 8;
                }else{
                    progress = details[i] + 9;
                }
                seekBars.get(i).setProgress(progress);
//                seekBars.get(i).invalidate();
                curProgress += progress + ",";
                textViews.get(i).setText(details[i] + "db");
            }
        }
        LogUtils.e("curProgress :" + curProgress);
    }

    private int UiMode2DevMode(int uiMode){
        int devMode = 6;
        if (uiMode == 0) {
            //自然
        } else if (uiMode == 1) {
            //自然
        }
        return devMode;
    }

    private int DevMode2UiMode(int devMode){
        int uiMode = EqItem.MODE_CUSTOM;
        //自然 摇滚 流行 经典 爵士 乡村 自定义
        if (devMode == 0) {
            //自然
            uiMode = EqItem.MODE_NATURAL;
        } else if (devMode == 1) {
            //摇滚
            uiMode = EqItem.MODE_ROCK;
        } else if (devMode == 2) {
            //流行
            uiMode = EqItem.MODE_POP;
        } else if (devMode == 3) {
            //经典
            uiMode = EqItem.MODE_CLASS;
        } else if (devMode == 4) {
            //爵士
            uiMode = EqItem.MODE_JAZZ;
        } else if (devMode == 5) {
            //乡村
            uiMode = EqItem.MODE_VILLAGE;
        } else if (devMode == 6) {
            //自定义
            uiMode = EqItem.MODE_CUSTOM;
        }
        return uiMode;
    }

    @Override
    public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
        if(!isFromUser){
            return;
        }
//        LogUtils.i("onRangeChanged :" +view.getLeftSeekBar().getProgress() + " , leftValue : " + leftValue + " , rightValue" + rightValue);
        EqInfo eqData = eqItemList.get(eqItemList.size() - 1).getEqInfo();
        byte[] details = eqData.getValue();
        for (int i = 0; i < seekBars.size(); i++) {
            if(seekBars.get(i).getId() == view.getId()){
                int progress = (int) seekBars.get(i).getLeftSeekBar().getProgress();
                int value = 0;
                if(progress >= 9){
                    value = (int) (progress - 9);
                }else{
                    value = (int) (progress - 8);
                }
                textViews.get(i).setText(value + "db");
                break;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
        LogUtils.e("onStartTrackingTouch :" +view.getLeftSeekBar().getProgress() + " , isLeft : " + isLeft);
    }

    @Override
    public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
        LogUtils.e("onStopTrackingTouch :" +view.getLeftSeekBar().getProgress() + " , isLeft : " + isLeft);
        EqInfo eqData = eqItemList.get(0).getEqInfo();
        byte[] details = eqData.getValue();
        for (int i = 0; i < seekBars.size(); i++) {
            int progress = (int) seekBars.get(i).getLeftSeekBar().getProgress();
            int value = 0;
            if(progress >= 9){
                value = (int) (progress - 9);
            }else{
                value = (int) (progress - 8);
            }
//            HyLog.e("current eq index : " + i + " , value :" + value + " , progress : " + progress);
            if(i < details.length){
                details[i] = (byte)value;
            }
            textViews.get(i).setText(value + "db");
        }

        eqData.setValue(details);
        eqItemList.get(0).setEqInfo(eqData);
        //同步设备EQ
        setToDevice(eqData);

        if(deviceSettings != null){
            deviceSettings.setEqInfo(eqData);
            deviceSettings.setEqMode(EqItem.MODE_CUSTOM);
            saveToDb();
        }
        updateCustom(eqItemList.get(0));

    }

    private void saveToDb() {
        SharePreferencesUtil.getInstance().updateSettingsByClassMac(this,deviceSettings.getClassicMac(),deviceSettings);
    }

    private void setToDevice(EqInfo eqData) {
        if(rcspController.isDeviceConnected()){
            LogUtils.e("configEqInfo eqData : " + eqData);
            rcspController.configEqInfo(rcspController.getUsingDevice(), eqData, new OnRcspActionCallback<Boolean>() {
                @Override
                public void onSuccess(BluetoothDevice bluetoothDevice, Boolean aBoolean) {
                        LogUtils.i("configEqInfo onSuccess");
                }

                @Override
                public void onError(BluetoothDevice bluetoothDevice, BaseError baseError) {
                    LogUtils.i("configEqInfo onSuccess ： " + baseError);
                }
            });
        }
    }

    private void updateCustom(EqItem item){
        eqItemList.set(0,item);
        for (int i = 0; i < eqItemList.size(); i++) {
            if(i == 0){
                eqItemList.get(i).isSelect = true;
            }else{
                eqItemList.get(i).isSelect = false;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rcspController.removeBTRcspEventCallback(btRcspEventCallback);
        saveToDb();
    }
}