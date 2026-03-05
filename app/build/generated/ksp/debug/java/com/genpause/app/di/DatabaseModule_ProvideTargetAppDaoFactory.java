package com.genpause.app.di;

import com.genpause.app.data.dao.TargetAppDao;
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
public final class DatabaseModule_ProvideTargetAppDaoFactory implements Factory<TargetAppDao> {
  private final Provider<ZenPauseDatabase> dbProvider;

  public DatabaseModule_ProvideTargetAppDaoFactory(Provider<ZenPauseDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public TargetAppDao get() {
    return provideTargetAppDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideTargetAppDaoFactory create(
      Provider<ZenPauseDatabase> dbProvider) {
    return new DatabaseModule_ProvideTargetAppDaoFactory(dbProvider);
  }

  public static TargetAppDao provideTargetAppDao(ZenPauseDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTargetAppDao(db));
  }
}
