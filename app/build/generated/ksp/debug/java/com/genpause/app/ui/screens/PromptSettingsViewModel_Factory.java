package com.genpause.app.ui.screens;

import com.genpause.app.data.preferences.PreferencesManager;
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
public final class PromptSettingsViewModel_Factory implements Factory<PromptSettingsViewModel> {
  private final Provider<PreferencesManager> prefsProvider;

  public PromptSettingsViewModel_Factory(Provider<PreferencesManager> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  @Override
  public PromptSettingsViewModel get() {
    return newInstance(prefsProvider.get());
  }

  public static PromptSettingsViewModel_Factory create(Provider<PreferencesManager> prefsProvider) {
    return new PromptSettingsViewModel_Factory(prefsProvider);
  }

  public static PromptSettingsViewModel newInstance(PreferencesManager prefs) {
    return new PromptSettingsViewModel(prefs);
  }
}
