package com.genpause.app.di;

import com.genpause.app.data.dao.ScheduleDao;
import com.genpause.app.data.db.ZenPauseDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DatabaseModule_ProvideScheduleDaoFactory implements Factory<ScheduleDao> {
  private final Provider<ZenPauseDatabase> dbProvider;

  public DatabaseModule_ProvideScheduleDaoFactory(Provider<ZenPauseDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ScheduleDao get() {
    return provideScheduleDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideScheduleDaoFactory create(
      Provider<ZenPauseDatabase> dbProvider) {
    return new DatabaseModule_ProvideScheduleDaoFactory(dbProvider);
  }

  public static ScheduleDao provideScheduleDao(ZenPauseDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideScheduleDao(db));
  }
}
