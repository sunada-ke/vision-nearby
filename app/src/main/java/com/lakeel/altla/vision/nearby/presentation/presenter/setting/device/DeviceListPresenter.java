package com.lakeel.altla.vision.nearby.presentation.presenter.setting.device;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.CollectionUtils;
import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserBeaconsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.RemoveBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.RemoveUserBeaconUseCase;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.BeaconModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.DeviceModel;
import com.lakeel.altla.vision.nearby.presentation.view.DeviceItemView;
import com.lakeel.altla.vision.nearby.presentation.view.DeviceListView;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.DeviceAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DeviceListPresenter extends BasePresenter<DeviceListView> {

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindUserBeaconsUseCase findUserBeaconsUseCase;

    @Inject
    FindBeaconUseCase findBeaconUseCase;

    @Inject
    RemoveBeaconUseCase removeBeaconUseCase;

    @Inject
    RemoveUserBeaconUseCase removeUserBeaconUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceListPresenter.class);

    private final BeaconModelMapper modelMapper = new BeaconModelMapper();

    private List<DeviceModel> deviceModels = new ArrayList<>();

    @Inject
    DeviceListPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findUserBeaconsUseCase
                .execute(MyUser.getUid())
                .flatMap(this::findBeacon)
                .filter(entity -> entity != null)
                .map(modelMapper::map)
                .toSortedList((model1, model2) -> (int) (model2.lastUsedTime - model1.lastUsedTime))
                .subscribeOn(Schedulers.io())
                .subscribe(models -> {
                    deviceModels.clear();
                    deviceModels.addAll(models);
                    getView().updateItems();
                }, e -> {
                    LOGGER.error("Failed to find user beacons.", e);
                });
        subscriptions.add(subscription);
    }

    public int getItemCount() {
        return deviceModels.size();
    }

    public void onCreateItemView(DeviceAdapter.DeviceViewHolder viewHolder) {
        DeviceListPresenter.DeviceItemPresenter itemPresenter = new DeviceListPresenter.DeviceItemPresenter();
        itemPresenter.onCreateItemView(viewHolder);
        viewHolder.setItemPresenter(itemPresenter);
    }

    public class DeviceItemPresenter extends BaseItemPresenter<DeviceItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(deviceModels.get(position));
        }

        public void onClick(DeviceModel model) {
            getView().showTrackingFragment(model.beaconId, model.name);
        }

        public void onRemove(DeviceModel model) {
            Subscription subscription = removeBeaconUseCase
                    .execute(model.beaconId)
                    .flatMap(beaconId -> removeUserBeacon(MyUser.getUid(), model.beaconId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                                int size = deviceModels.size();
                                deviceModels.remove(model);
                                if (CollectionUtils.isEmpty(deviceModels)) {
                                    getView().removeAll(size);
                                } else {
                                    getView().updateItems();
                                }
                                getView().showSnackBar(R.string.message_removed);
                            },
                            e -> LOGGER.error("Failed to remove a device.", e));
            subscriptions.add(subscription);
        }
    }

    Observable<BeaconEntity> findBeacon(String beaconId) {
        return findBeaconUseCase.execute(beaconId).subscribeOn(Schedulers.io()).toObservable();
    }

    Single<String> removeUserBeacon(String userId, String beaconId) {
        return removeUserBeaconUseCase.execute(userId, beaconId).subscribeOn(Schedulers.io());
    }
}
