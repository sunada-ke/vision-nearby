package com.lakeel.altla.vision.nearby.presentation.presenter.device;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.CollectionUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindAllDeviceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FoundDeviceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.LostDeviceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.RemoveDeviceUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.DeviceModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.DeviceModel;
import com.lakeel.altla.vision.nearby.presentation.view.DeviceItemView;
import com.lakeel.altla.vision.nearby.presentation.view.DeviceListView;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.DeviceAdapter;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class DeviceListPresenter extends BasePresenter<DeviceListView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindAllDeviceUseCase findAllDeviceUseCase;

    @Inject
    RemoveDeviceUseCase removeDeviceUseCase;

    @Inject
    LostDeviceUseCase lostDeviceUseCase;

    @Inject
    FoundDeviceUseCase foundDeviceUseCase;

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private final DeviceModelMapper modelMapper = new DeviceModelMapper();

    private final List<DeviceModel> deviceModels = new ArrayList<>();

    @Inject
    DeviceListPresenter() {
    }

    public void onActivityCreated() {
        findUserDevices();
    }

    public void onStop() {
        subscriptions.unSubscribe();
    }

    public int getItemCount() {
        return deviceModels.size();
    }

    public void onCreateItemView(DeviceAdapter.DeviceItemViewHolder viewHolder) {
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

        public void onFound(DeviceModel model) {
            analyticsReporter.foundDevice(model.beaconId, model.name);

            Subscription subscription = foundDeviceUseCase.execute(model.beaconId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ErrorAction<>(), DeviceListPresenter.this::findUserDevices);
            subscriptions.add(subscription);
        }

        public void onLost(DeviceModel model) {
            analyticsReporter.lostDevice(model.beaconId, model.name);

            Subscription subscription = lostDeviceUseCase.execute(model.beaconId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ErrorAction<>(), DeviceListPresenter.this::findUserDevices);
            subscriptions.add(subscription);
        }

        public void onRemove(DeviceModel model) {
            analyticsReporter.removeDevice(model.beaconId, model.name);

            Subscription subscription = removeDeviceUseCase.execute(model.beaconId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        int size = deviceModels.size();
                        deviceModels.remove(model);
                        if (CollectionUtils.isEmpty(deviceModels)) {
                            getView().removeAll(size);
                        } else {
                            getView().updateItems();
                        }
                        getView().showSnackBar(R.string.snackBar_message_removed);
                    }, new ErrorAction<>());
            subscriptions.add(subscription);
        }
    }

    private void findUserDevices() {
        Subscription subscription = findAllDeviceUseCase.execute()
                .map(modelMapper::map)
                .toSortedList((t1, t2) -> Long.compare(t2.lastUsedTime, t1.lastUsedTime))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                    deviceModels.clear();
                    deviceModels.addAll(models);
                    getView().updateItems();
                }, new ErrorAction<>());
        subscriptions.add(subscription);
    }
}
