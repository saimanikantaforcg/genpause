package com.genpause.app.domain;

import com.genpause.app.data.repository.ZenPauseRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class RulesEngine_Factory implements Factory<RulesEngine> {
  private final Provider<ZenPauseRepository> repositoryProvider;

  public RulesEngine_Factory(Provider<ZenPauseRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public RulesEngine get() {
    return newInstance(repositoryProvider.get());
  }

  public static RulesEngine_Factory create(Provider<ZenPauseRepository> repositoryProvider) {
    return new RulesEngine_Factory(repositoryProvider);
  }

  public static RulesEngine newInstance(ZenPauseRepository repository) {
    return new RulesEngine(repository);
  }
}
