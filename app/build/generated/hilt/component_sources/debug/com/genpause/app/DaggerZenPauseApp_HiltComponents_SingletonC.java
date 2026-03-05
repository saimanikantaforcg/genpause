package com.genpause.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.genpause.app.data.analytics.AnalyticsEngine;
import com.genpause.app.data.dao.EventDao;
import com.genpause.app.data.dao.ScheduleDao;
import com.genpause.app.data.dao.TargetAppDao;
import com.genpause.app.data.db.ZenPauseDatabase;
import com.genpause.app.data.preferences.PreferencesManager;
import com.genpause.app.data.repository.ZenPauseRepository;
import com.genpause.app.di.AppModule_ProvideAnalyticsEngineFactory;
import com.genpause.app.di.AppModule_ProvidePreferencesManagerFactory;
import com.genpause.app.di.AppModule_ProvideRulesEngineFactory;
import com.genpause.app.di.DatabaseModule_ProvideDatabaseFactory;
import com.genpause.app.di.DatabaseModule_ProvideEventDaoFactory;
import com.genpause.app.di.DatabaseModule_ProvideScheduleDaoFactory;
import com.genpause.app.di.DatabaseModule_ProvideTargetAppDaoFactory;
import com.genpause.app.domain.RulesEngine;
import com.genpause.app.ui.MainActivity;
import com.genpause.app.ui.MainActivity_MembersInjector;
import com.genpause.app.ui.screens.AppSettingsViewModel;
import com.genpause.app.ui.screens.AppSettingsViewModel_HiltModules;
import com.genpause.app.ui.screens.AppSettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.genpause.app.ui.screens.AppSettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.genpause.app.ui.screens.DashboardViewModel;
import com.genpause.app.ui.screens.DashboardViewModel_HiltModules;
import com.genpause.app.ui.screens.DashboardViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.genpause.app.ui.screens.DashboardViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.genpause.app.ui.screens.FocusBlocksViewModel;
import com.genpause.app.ui.screens.FocusBlocksViewModel_HiltModules;
import com.genpause.app.ui.screens.FocusBlocksViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.genpause.app.ui.screens.FocusBlocksViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.genpause.app.ui.screens.ManagedAppsViewModel;
import com.genpause.app.ui.screens.ManagedAppsViewModel_HiltModules;
import com.genpause.app.ui.screens.ManagedAppsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.genpause.app.ui.screens.ManagedAppsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.genpause.app.ui.screens.PromptSettingsViewModel;
import com.genpause.app.ui.screens.PromptSettingsViewModel_HiltModules;
import com.genpause.app.ui.screens.PromptSettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.genpause.app.ui.screens.PromptSettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.genpause.app.ui.screens.SettingsViewModel;
import com.genpause.app.ui.screens.SettingsViewModel_HiltModules;
import com.genpause.app.ui.screens.SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.genpause.app.ui.screens.SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class DaggerZenPauseApp_HiltComponents_SingletonC {
  private DaggerZenPauseApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public ZenPauseApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements ZenPauseApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public ZenPauseApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements ZenPauseApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public ZenPauseApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements ZenPauseApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public ZenPauseApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements ZenPauseApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ZenPauseApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements ZenPauseApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ZenPauseApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements ZenPauseApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public ZenPauseApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements ZenPauseApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public ZenPauseApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends ZenPauseApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends ZenPauseApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends ZenPauseApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends ZenPauseApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(6).put(AppSettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, AppSettingsViewModel_HiltModules.KeyModule.provide()).put(DashboardViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, DashboardViewModel_HiltModules.KeyModule.provide()).put(FocusBlocksViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, FocusBlocksViewModel_HiltModules.KeyModule.provide()).put(ManagedAppsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, ManagedAppsViewModel_HiltModules.KeyModule.provide()).put(PromptSettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, PromptSettingsViewModel_HiltModules.KeyModule.provide()).put(SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SettingsViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectPreferencesManager(instance, singletonCImpl.providePreferencesManagerProvider.get());
      MainActivity_MembersInjector.injectRepository(instance, singletonCImpl.zenPauseRepositoryProvider.get());
      return instance;
    }
  }

  private static final class ViewModelCImpl extends ZenPauseApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AppSettingsViewModel> appSettingsViewModelProvider;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<FocusBlocksViewModel> focusBlocksViewModelProvider;

    private Provider<ManagedAppsViewModel> managedAppsViewModelProvider;

    private Provider<PromptSettingsViewModel> promptSettingsViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.appSettingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.focusBlocksViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.managedAppsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.promptSettingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(6).put(AppSettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) appSettingsViewModelProvider)).put(DashboardViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) dashboardViewModelProvider)).put(FocusBlocksViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) focusBlocksViewModelProvider)).put(ManagedAppsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) managedAppsViewModelProvider)).put(PromptSettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) promptSettingsViewModelProvider)).put(SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) settingsViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.genpause.app.ui.screens.AppSettingsViewModel 
          return (T) new AppSettingsViewModel(singletonCImpl.zenPauseRepositoryProvider.get(), viewModelCImpl.savedStateHandle);

          case 1: // com.genpause.app.ui.screens.DashboardViewModel 
          return (T) new DashboardViewModel(singletonCImpl.provideAnalyticsEngineProvider.get());

          case 2: // com.genpause.app.ui.screens.FocusBlocksViewModel 
          return (T) new FocusBlocksViewModel(singletonCImpl.zenPauseRepositoryProvider.get());

          case 3: // com.genpause.app.ui.screens.ManagedAppsViewModel 
          return (T) new ManagedAppsViewModel(singletonCImpl.zenPauseRepositoryProvider.get());

          case 4: // com.genpause.app.ui.screens.PromptSettingsViewModel 
          return (T) new PromptSettingsViewModel(singletonCImpl.providePreferencesManagerProvider.get());

          case 5: // com.genpause.app.ui.screens.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.providePreferencesManagerProvider.get(), singletonCImpl.zenPauseRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends ZenPauseApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends ZenPauseApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends ZenPauseApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<ZenPauseDatabase> provideDatabaseProvider;

    private Provider<ZenPauseRepository> zenPauseRepositoryProvider;

    private Provider<RulesEngine> provideRulesEngineProvider;

    private Provider<PreferencesManager> providePreferencesManagerProvider;

    private Provider<AnalyticsEngine> provideAnalyticsEngineProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private TargetAppDao targetAppDao() {
      return DatabaseModule_ProvideTargetAppDaoFactory.provideTargetAppDao(provideDatabaseProvider.get());
    }

    private EventDao eventDao() {
      return DatabaseModule_ProvideEventDaoFactory.provideEventDao(provideDatabaseProvider.get());
    }

    private ScheduleDao scheduleDao() {
      return DatabaseModule_ProvideScheduleDaoFactory.provideScheduleDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<ZenPauseDatabase>(singletonCImpl, 2));
      this.zenPauseRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ZenPauseRepository>(singletonCImpl, 1));
      this.provideRulesEngineProvider = DoubleCheck.provider(new SwitchingProvider<RulesEngine>(singletonCImpl, 0));
      this.providePreferencesManagerProvider = DoubleCheck.provider(new SwitchingProvider<PreferencesManager>(singletonCImpl, 3));
      this.provideAnalyticsEngineProvider = DoubleCheck.provider(new SwitchingProvider<AnalyticsEngine>(singletonCImpl, 4));
    }

    @Override
    public void injectZenPauseApp(ZenPauseApp zenPauseApp) {
      injectZenPauseApp2(zenPauseApp);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private ZenPauseApp injectZenPauseApp2(ZenPauseApp instance) {
      ZenPauseApp_MembersInjector.injectRulesEngine(instance, provideRulesEngineProvider.get());
      ZenPauseApp_MembersInjector.injectPreferencesManager(instance, providePreferencesManagerProvider.get());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.genpause.app.domain.RulesEngine 
          return (T) AppModule_ProvideRulesEngineFactory.provideRulesEngine(singletonCImpl.zenPauseRepositoryProvider.get());

          case 1: // com.genpause.app.data.repository.ZenPauseRepository 
          return (T) new ZenPauseRepository(singletonCImpl.targetAppDao(), singletonCImpl.eventDao(), singletonCImpl.scheduleDao());

          case 2: // com.genpause.app.data.db.ZenPauseDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.genpause.app.data.preferences.PreferencesManager 
          return (T) AppModule_ProvidePreferencesManagerFactory.providePreferencesManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.genpause.app.data.analytics.AnalyticsEngine 
          return (T) AppModule_ProvideAnalyticsEngineFactory.provideAnalyticsEngine(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.zenPauseRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
