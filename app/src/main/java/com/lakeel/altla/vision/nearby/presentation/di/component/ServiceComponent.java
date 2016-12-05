package com.lakeel.altla.vision.nearby.presentation.di.component;

import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;
import com.lakeel.altla.vision.nearby.presentation.di.module.ConfigModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.RepositoryModule;
import com.lakeel.altla.vision.nearby.presentation.service.LINEService;
import com.lakeel.altla.vision.nearby.presentation.service.LocationService;
import com.lakeel.altla.vision.nearby.presentation.service.NotificationService;
import com.lakeel.altla.vision.nearby.presentation.service.RecentlyService;

import dagger.Component;

@InjectScope
@Component(modules = {RepositoryModule.class, ConfigModule.class})
public interface ServiceComponent {

    void inject(LINEService service);

    void inject(LocationService service);

    void inject(RecentlyService service);

    void inject(NotificationService service);
}