package com.genpause.app.data.analytics;

import android.content.Context;
import com.genpause.app.data.repository.ZenPauseRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class AnalyticsEngine_Factory implements Factory<AnalyticsEngine> {
  private final Provider<Context> contextProvider;

  private final Provider<ZenPauseRepository> repositoryProvider;

  public AnalyticsEngine_Factory(Provider<Context> contextProvider,
      Provider<ZenPauseRepository> repositoryProvider) {
    this.contextProvider = contextProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AnalyticsEngine get() {
    return newInstance(contextProvider.get(), repositoryProvider.get());
  }

  public static AnalyticsEngine_Factory create(Provider<Context> contextProvider,
      Provider<ZenPauseRepository> repositoryProvider) {
    return new AnalyticsEngine_Factory(contextProvider, repositoryProvider);
  }

  public static AnalyticsEngine newInstance(Context context, ZenPauseRepository repository) {
    return new AnalyticsEngine(context, repository);
  }
}
