package com.genpause.app.ui.screens;

import com.genpause.app.data.preferences.PreferencesManager;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<PreferencesManager> prefsProvider;

  private final Provider<ZenPauseRepository> repositoryProvider;

  public SettingsViewModel_Factory(Provider<PreferencesManager> prefsProvider,
      Provider<ZenPauseRepository> repositoryProvider) {
    this.prefsProvider = prefsProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(prefsProvider.get(), repositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<PreferencesManager> prefsProvider,
      Provider<ZenPauseRepository> repositoryProvider) {
    return new SettingsViewModel_Factory(prefsProvider, repositoryProvider);
  }

  public static SettingsViewModel newInstance(PreferencesManager prefs,
      ZenPauseRepository repository) {
    return new SettingsViewModel(prefs, repository);
  }
}
