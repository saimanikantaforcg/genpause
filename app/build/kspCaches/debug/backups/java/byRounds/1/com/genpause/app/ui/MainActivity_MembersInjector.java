package com.genpause.app.ui;

import com.genpause.app.data.preferences.PreferencesManager;
import com.genpause.app.data.repository.ZenPauseRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<ZenPauseRepository> repositoryProvider;

  public MainActivity_MembersInjector(Provider<PreferencesManager> preferencesManagerProvider,
      Provider<ZenPauseRepository> repositoryProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.repositoryProvider = repositoryProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<PreferencesManager> preferencesManagerProvider,
      Provider<ZenPauseRepository> repositoryProvider) {
    return new MainActivity_MembersInjector(preferencesManagerProvider, repositoryProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectPreferencesManager(instance, preferencesManagerProvider.get());
    injectRepository(instance, repositoryProvider.get());
  }

  @InjectedFieldSignature("com.genpause.app.ui.MainActivity.preferencesManager")
  public static void injectPreferencesManager(MainActivity instance,
      PreferencesManager preferencesManager) {
    instance.preferencesManager = preferencesManager;
  }

  @InjectedFieldSignature("com.genpause.app.ui.MainActivity.repository")
  public static void injectRepository(MainActivity instance, ZenPauseRepository repository) {
    instance.repository = repository;
  }
}
