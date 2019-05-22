package com.xyoye.dandanplay2.ui.weight.item;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.bean.SearchHistoryBean;
import com.xyoye.dandanplay2.bean.event.DeleteHistoryEvent;
import com.xyoye.dandanplay2.bean.event.SearchHistoryEvent;
import com.xyoye.dandanplay2.utils.interf.AdapterItem;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by xyy on 2019/1/8.
 */

public class SearchHistoryItem implements AdapterItem<SearchHistoryBean> {
    @BindView(R.id.history_text_tv)
    TextView historyTextTv;
    @BindView(R.id.delete_history_iv)
    ImageView deleteHistoryIv;
    @BindView(R.id.history_info_rl)
    RelativeLayout historyInfoRl;
    @BindView(R.id.delete_all_rl)
    RelativeLayout deleteAllRl;

    @Override
    public int getLayoutResId() {
        return R.layout.item_search_history;
    }

    @Override
    public void initItemViews(View itemView) {

    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(SearchHistoryBean model, int position) {
        if (model.get_id() == -1 && StringUtils.isEmpty(model.getText())) {
            historyInfoRl.setVisibility(View.GONE);
            deleteAllRl.setVisibility(View.VISIBLE);

            deleteAllRl.setOnClickListener(v ->
                    EventBus.getDefault().post(new DeleteHistoryEvent(-1, true)));
        } else {
            historyInfoRl.setVisibility(View.VISIBLE);
            deleteAllRl.setVisibility(View.GONE);

            historyTextTv.setText(model.getText());

            historyInfoRl.setOnClickListener(v ->
                    EventBus.getDefault().post(new SearchHistoryEvent(model.getText())));

            deleteHistoryIv.setOnClickListener(v ->
                    EventBus.getDefault().post(new DeleteHistoryEvent(position, false)));
        }
    }
}
