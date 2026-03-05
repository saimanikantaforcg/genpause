package com.genpause.app.data.repository;

import com.genpause.app.data.dao.EventDao;
import com.genpause.app.data.dao.ScheduleDao;
import com.genpause.app.data.dao.TargetAppDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class ZenPauseRepository_Factory implements Factory<ZenPauseRepository> {
  private final Provider<TargetAppDao> targetAppDaoProvider;

  private final Provider<EventDao> eventDaoProvider;

  private final Provider<ScheduleDao> scheduleDaoProvider;

  public ZenPauseRepository_Factory(Provider<TargetAppDao> targetAppDaoProvider,
      Provider<EventDao> eventDaoProvider, Provider<ScheduleDao> scheduleDaoProvider) {
    this.targetAppDaoProvider = targetAppDaoProvider;
    this.eventDaoProvider = eventDaoProvider;
    this.scheduleDaoProvider = scheduleDaoProvider;
  }

  @Override
  public ZenPauseRepository get() {
    return newInstance(targetAppDaoProvider.get(), eventDaoProvider.get(), scheduleDaoProvider.get());
  }

  public static ZenPauseRepository_Factory create(Provider<TargetAppDao> targetAppDaoProvider,
      Provider<EventDao> eventDaoProvider, Provider<ScheduleDao> scheduleDaoProvider) {
    return new ZenPauseRepository_Factory(targetAppDaoProvider, eventDaoProvider, scheduleDaoProvider);
  }

  public static ZenPauseRepository newInstance(TargetAppDao targetAppDao, EventDao eventDao,
      ScheduleDao scheduleDao) {
    return new ZenPauseRepository(targetAppDao, eventDao, scheduleDao);
  }
}
