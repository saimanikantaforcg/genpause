package com.genpause.app.di;

import com.genpause.app.data.dao.EventDao;
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
public final class DatabaseModule_ProvideEventDaoFactory implements Factory<EventDao> {
  private final Provider<ZenPauseDatabase> dbProvider;

  public DatabaseModule_ProvideEventDaoFactory(Provider<ZenPauseDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public EventDao get() {
    return provideEventDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideEventDaoFactory create(
      Provider<ZenPauseDatabase> dbProvider) {
    return new DatabaseModule_ProvideEventDaoFactory(dbProvider);
  }

  public static EventDao provideEventDao(ZenPauseDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideEventDao(db));
  }
}
