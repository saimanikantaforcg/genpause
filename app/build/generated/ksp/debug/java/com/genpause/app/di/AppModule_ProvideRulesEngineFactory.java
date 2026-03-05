package com.genpause.app.di;

import com.genpause.app.data.repository.ZenPauseRepository;
import com.genpause.app.domain.RulesEngine;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideRulesEngineFactory implements Factory<RulesEngine> {
  private final Provider<ZenPauseRepository> repositoryProvider;

  public AppModule_ProvideRulesEngineFactory(Provider<ZenPauseRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public RulesEngine get() {
    return provideRulesEngine(repositoryProvider.get());
  }

  public static AppModule_ProvideRulesEngineFactory create(
      Provider<ZenPauseRepository> repositoryProvider) {
    return new AppModule_ProvideRulesEngineFactory(repositoryProvider);
  }

  public static RulesEngine provideRulesEngine(ZenPauseRepository repository) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideRulesEngine(repository));
  }
}
