package com.genpause.app.ui.screens;

import com.genpause.app.data.repository.ZenPauseRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ManagedAppsViewModel_Factory implements Factory<ManagedAppsViewModel> {
  private final Provider<ZenPauseRepository> repositoryProvider;

  public ManagedAppsViewModel_Factory(Provider<ZenPauseRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ManagedAppsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ManagedAppsViewModel_Factory create(
      Provider<ZenPauseRepository> repositoryProvider) {
    return new ManagedAppsViewModel_Factory(repositoryProvider);
  }

  public static ManagedAppsViewModel newInstance(ZenPauseRepository repository) {
    return new ManagedAppsViewModel(repository);
  }
}
