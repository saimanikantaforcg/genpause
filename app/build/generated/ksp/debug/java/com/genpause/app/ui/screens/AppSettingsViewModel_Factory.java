package com.genpause.app.ui.screens;

import androidx.lifecycle.SavedStateHandle;
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
public final class AppSettingsViewModel_Factory implements Factory<AppSettingsViewModel> {
  private final Provider<ZenPauseRepository> repositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public AppSettingsViewModel_Factory(Provider<ZenPauseRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public AppSettingsViewModel get() {
    return newInstance(repositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static AppSettingsViewModel_Factory create(Provider<ZenPauseRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new AppSettingsViewModel_Factory(repositoryProvider, savedStateHandleProvider);
  }

  public static AppSettingsViewModel newInstance(ZenPauseRepository repository,
      SavedStateHandle savedStateHandle) {
    return new AppSettingsViewModel(repository, savedStateHandle);
  }
}
