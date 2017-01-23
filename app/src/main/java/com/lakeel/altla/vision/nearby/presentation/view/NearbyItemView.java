package com.lakeel.altla.vision.nearby.presentation.view;


import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyItemModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.nearby.NearbyUserListPresenter;

public interface NearbyItemView extends ItemView {

    void setItemPresenter(NearbyUserListPresenter.NearbyItemPresenter itemPresenter);

    void showItem(NearbyItemModel model);
}