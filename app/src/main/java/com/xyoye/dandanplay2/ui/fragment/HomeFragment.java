package com.xyoye.dandanplay2.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.xyoye.dandanplay2.R;
import com.xyoye.dandanplay2.base.BaseFragment;
import com.xyoye.dandanplay2.bean.BangumiBean;
import com.xyoye.dandanplay2.mvp.impl.HomeFragmentPresenterImpl;
import com.xyoye.dandanplay2.mvp.presenter.HomeFragmentPresenter;
import com.xyoye.dandanplay2.mvp.view.HomeFragmentView;
import com.xyoye.dandanplay2.ui.activities.AnimeSeasonActivity;
import com.xyoye.dandanplay2.ui.activities.LoginActivity;
import com.xyoye.dandanplay2.ui.activities.PersonalFavoriteActivity;
import com.xyoye.dandanplay2.ui.activities.PersonalHistoryActivity;
import com.xyoye.dandanplay2.ui.activities.SearchActivity;
import com.xyoye.dandanplay2.ui.activities.WebViewActivity;
import com.xyoye.dandanplay2.ui.weight.ScrollableLayout;
import com.xyoye.dandanplay2.ui.weight.indicator.LinePagerIndicator;
import com.xyoye.dandanplay2.ui.weight.indicator.MagicIndicator;
import com.xyoye.dandanplay2.ui.weight.indicator.abs.CommonNavigatorAdapter;
import com.xyoye.dandanplay2.ui.weight.indicator.abs.IPagerIndicator;
import com.xyoye.dandanplay2.ui.weight.indicator.abs.IPagerTitleView;
import com.xyoye.dandanplay2.ui.weight.indicator.navigator.CommonNavigator;
import com.xyoye.dandanplay2.ui.weight.indicator.title.ColorTransitionPagerTitleView;
import com.xyoye.dandanplay2.ui.weight.indicator.title.SimplePagerTitleView;
import com.xyoye.dandanplay2.utils.AppConfig;
import com.xyoye.dandanplay2.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YE on 2018/6/29 0029.
 */


public class HomeFragment extends BaseFragment<HomeFragmentPresenter> implements HomeFragmentView {
    @BindView(R.id.scroll_layout)
    ScrollableLayout scrollableLayout;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh;

    AnimaFragmentAdapter fragmentAdapter;
    List<AnimeFragment> fragmentList;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @NonNull
    @Override
    protected HomeFragmentPresenter initPresenter() {
        return new HomeFragmentPresenterImpl(this, this);
    }

    @Override
    protected int initPageLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
        refresh.setColorSchemeResources(R.color.theme_color);

        List<String> dateList = new ArrayList<>();
        dateList.add("周日");
        dateList.add("周一");
        dateList.add("周二");
        dateList.add("周三");
        dateList.add("周四");
        dateList.add("周五");
        dateList.add("周六");
        initIndicator(dateList);
    }

    @Override
    public void initListener() {
        refresh.setOnRefreshListener(() ->
                presenter.getHomeFragmentData());

        refresh.setRefreshing(true);
        presenter.getHomeFragmentData();
    }

    private void initIndicator(List<String> dateList) {
        CommonNavigator commonNavigator = new CommonNavigator(getBaseActivity());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return dateList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(dateList.get(index));
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.text_black));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.theme_color));
                simplePagerTitleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
                return simplePagerTitleView;
            }

            @SuppressLint("ResourceType")
            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(ContextCompat.getColor(context, R.color.theme_color));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        magicIndicator.onPageSelected(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
    }

    @Override
    public void initViewPager(List<BangumiBean> beans) {
        fragmentList = new ArrayList<>();
        for (BangumiBean bean : beans) {
            fragmentList.add(AnimeFragment.newInstance(bean));
        }
        if (fragmentList.size() > 0) {
            scrollableLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(0));
        }

        fragmentAdapter = new AnimaFragmentAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                scrollableLayout.getHelper().setCurrentScrollableContainer(fragmentList.get(position));
                magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
            }
        });
    }

    @OnClick({R.id.search_ll, R.id.list_ll, R.id.follow_ll, R.id.history_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_ll:
                launchActivity(SearchActivity.class);
                break;
            case R.id.list_ll:
                launchActivity(AnimeSeasonActivity.class);
                break;
            case R.id.follow_ll:
                if (AppConfig.getInstance().isLogin()){
                    launchActivity(PersonalFavoriteActivity.class);
                }else {
                    launchActivity(LoginActivity.class);
                }
                break;
            case R.id.history_ll:
                if (AppConfig.getInstance().isLogin()){
                    launchActivity(PersonalHistoryActivity.class);
                }else {
                    launchActivity(LoginActivity.class);
                }
                break;
        }
    }

    @Override
    public void setBanners(List<String> images, List<String> titles, List<String> urls) {
        banner.releaseBanner();
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(images);
        banner.setBannerAnimation(Transformer.Default);
        banner.setBannerTitles(titles);
        banner.isAutoPlay(true);
        banner.setDelayTime(5000);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnBannerListener(position -> {
            String url = urls.get(position);
            String title = titles.get(position);
            Intent intent = new Intent(getContext(), WebViewActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("link", url);
            startActivity(intent);
        });
        banner.start();
    }

    @Override
    public void refreshUI(List<String> images, List<String> titles, List<String> urls, List<BangumiBean> beans) {
        setBanners(images, titles, urls);
        initViewPager(beans);

        if (refresh != null)
            refresh.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class AnimaFragmentAdapter extends FragmentPagerAdapter {
        private List<AnimeFragment> list;

        private AnimaFragmentAdapter(FragmentManager supportFragmentManager, List<AnimeFragment> list) {
            super(supportFragmentManager);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
