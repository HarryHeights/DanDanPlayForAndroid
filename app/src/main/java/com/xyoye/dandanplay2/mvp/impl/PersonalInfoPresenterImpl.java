package com.xyoye.dandanplay2.mvp.impl;

import android.os.Bundle;

import com.xyoye.dandanplay2.base.BaseMvpPresenterImpl;
import com.xyoye.dandanplay2.mvp.presenter.PersonalInfoPresenter;
import com.xyoye.dandanplay2.mvp.view.PersonalInfoView;
import com.xyoye.dandanplay2.utils.Lifeful;

/**
 * Created by YE on 2018/7/23.
 */


public class PersonalInfoPresenterImpl extends BaseMvpPresenterImpl<PersonalInfoView> implements PersonalInfoPresenter {

    public PersonalInfoPresenterImpl(PersonalInfoView view, Lifeful lifeful) {
        super(view, lifeful);
    }

    @Override
    public void init() {

    }

    @Override
    public void process(Bundle savedInstanceState) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
