package com.future.getd.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.future.getd.R;
import com.future.getd.ui.bean.EqItem;

import java.util.List;

public class EqAdapter extends BaseQuickAdapter<EqItem, QuickViewHolder> {

    public EqAdapter(@NonNull List<? extends EqItem> items) {
        super(items);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable EqItem eqType) {
        // 设置item数据
        if (eqType != null) {
            quickViewHolder.setText(R.id.tv_type,eqType.getTypeDescribe());
            quickViewHolder.setImageResource(R.id.iv_type,getDrawByType(eqType.getMode()));
            quickViewHolder.setImageResource(R.id.iv_select,eqType.isSelect()?R.drawable.eq_select:R.drawable.eq_unselect);
        }
    }

    //  public static final int MODE_CUSTOM = 0;
    //    public static final int MODE_POP = 1;
    //    public static final int MODE_DANCE = 2;
    //    public static final int MODE_BLUE = 3;
    //    public static final int MODE_JAZZ = 4;
    //    public static final int MODE_VILLAGE = 5;
    //    public static final int MODE_ROLL = 6;
    //    public static final int MODE_ELECTRONIC = 7;
    //    public static final int MODE_CLASS = 8;
    private int getDrawByType(int type){
        int src = R.drawable.eq_custom;
        switch (type){
            case EqItem.MODE_CUSTOM:
                src = R.drawable.eq_custom;
                break;
            case EqItem.MODE_POP:
                src = R.drawable.eq_pop;
                break;
            case EqItem.MODE_DANCE:
                src = R.drawable.eq_dance;
                break;
            case EqItem.MODE_BLUE:
                src = R.drawable.eq_blue;
                break;
            case EqItem.MODE_JAZZ:
                src = R.drawable.eq_jazz;
                break;
            case EqItem.MODE_VILLAGE:
                src = R.drawable.eq_village;
                break;
            case EqItem.MODE_ROLL:
                src = R.drawable.eq_roll;
                break;
            case EqItem.MODE_ELECTRONIC:
                src = R.drawable.eq_electronic;
                break;
            case EqItem.MODE_CLASS:
                src = R.drawable.eq_class;
                break;
        }
        return src;
    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        // 返回一个 ViewHolder
        return new QuickViewHolder(R.layout.item_eq,viewGroup);
    }
}
