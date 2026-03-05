package com.genpause.app;

import com.genpause.app.data.preferences.PreferencesManager;
import com.genpause.app.domain.RulesEngine;
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
public final class ZenPauseApp_MembersInjector implements MembersInjector<ZenPauseApp> {
  private final Provider<RulesEngine> rulesEngineProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public ZenPauseApp_MembersInjector(Provider<RulesEngine> rulesEngineProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.rulesEngineProvider = rulesEngineProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  public static MembersInjector<ZenPauseApp> create(Provider<RulesEngine> rulesEngineProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new ZenPauseApp_MembersInjector(rulesEngineProvider, preferencesManagerProvider);
  }

  @Override
  public void injectMembers(ZenPauseApp instance) {
    injectRulesEngine(instance, rulesEngineProvider.get());
    injectPreferencesManager(instance, preferencesManagerProvider.get());
  }

  @InjectedFieldSignature("com.genpause.app.ZenPauseApp.rulesEngine")
  public static void injectRulesEngine(ZenPauseApp instance, RulesEngine rulesEngine) {
    instance.rulesEngine = rulesEngine;
  }

  @InjectedFieldSignature("com.genpause.app.ZenPauseApp.preferencesManager")
  public static void injectPreferencesManager(ZenPauseApp instance,
      PreferencesManager preferencesManager) {
    instance.preferencesManager = preferencesManager;
  }
}
