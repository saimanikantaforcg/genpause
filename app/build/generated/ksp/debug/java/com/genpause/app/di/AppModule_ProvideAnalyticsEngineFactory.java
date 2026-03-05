package com.genpause.app.di;

import android.content.Context;
import com.genpause.app.data.analytics.AnalyticsEngine;
import com.genpause.app.data.repository.ZenPauseRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppModule_ProvideAnalyticsEngineFactory implements Factory<AnalyticsEngine> {
  private final Provider<Context> contextProvider;

  private final Provider<ZenPauseRepository> repositoryProvider;

  public AppModule_ProvideAnalyticsEngineFactory(Provider<Context> contextProvider,
      Provider<ZenPauseRepository> repositoryProvider) {
    this.contextProvider = contextProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AnalyticsEngine get() {
    return provideAnalyticsEngine(contextProvider.get(), repositoryProvider.get());
  }

  public static AppModule_ProvideAnalyticsEngineFactory create(Provider<Context> contextProvider,
      Provider<ZenPauseRepository> repositoryProvider) {
    return new AppModule_ProvideAnalyticsEngineFactory(contextProvider, repositoryProvider);
  }

  public static AnalyticsEngine provideAnalyticsEngine(Context context,
      ZenPauseRepository repository) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAnalyticsEngine(context, repository));
  }
}
