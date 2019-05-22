package com.xyoye.dandanplay2.ui.weight.item;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.bean.AnimeBean;
import com.xyoye.dandanplay2.ui.activities.AnimeDetailActivity;
import com.xyoye.dandanplay2.ui.weight.CornersCenterCrop;
import com.xyoye.dandanplay2.utils.AppConfig;
import com.xyoye.dandanplay2.utils.interf.AdapterItem;

import java.text.DecimalFormat;

import butterknife.BindView;

/**
 * Created by YE on 2018/7/15.
 */


public class AnimeItem implements AdapterItem<AnimeBean> {
    @BindView(R.id.image_iv)
    ImageView imageView;
    @BindView(R.id.anima_title)
    TextView animaTitle;
    @BindView(R.id.status_tv)
    TextView statusTv;
    @BindView(R.id.favorite_tv)
    TextView favoriteTv;
    @BindView(R.id.rating_tv)
    TextView ratingTv;

    private View mView;

    @Override
    public int getLayoutResId() {
        return R.layout.item_anime;
    }

    @Override
    public void initItemViews(View itemView) {
        mView = itemView;
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(AnimeBean model, int position) {

        if (AppConfig.getInstance().isLogin()){
            favoriteTv.setVisibility(View.VISIBLE);
            if (model.isIsFavorited())
                favoriteTv.setText("已关注");
        }

        statusTv.setText(model.isIsOnAir()
                         ? "连载中"
                         : "已完结");

        double rating = model.getRating();
        int ratingInt = (int) rating;
        if (rating == ratingInt){
            ratingTv.setText(ratingInt+"");
        }else {
            DecimalFormat df =new java.text.DecimalFormat("#.0");
            String ratingText = df.format(rating);
            ratingTv.setText(ratingText);
        }

        animaTitle.setText(model.getAnimeTitle());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .transform(new CornersCenterCrop(ConvertUtils.dp2px(5)));

        Glide.with(imageView.getContext())
                .load(model.getImageUrl())
                .apply(options)
                .into(imageView);

        mView.setOnClickListener(v ->
                AnimeDetailActivity.launchAnimeDetail(
                        (Activity)mView.getContext(),
                        model.getAnimeId()+"")
        );
    }
}
