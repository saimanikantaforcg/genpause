package com.genpause.app.ui.screens;

import com.genpause.app.data.analytics.AnalyticsEngine;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<AnalyticsEngine> analyticsEngineProvider;

  public DashboardViewModel_Factory(Provider<AnalyticsEngine> analyticsEngineProvider) {
    this.analyticsEngineProvider = analyticsEngineProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(analyticsEngineProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<AnalyticsEngine> analyticsEngineProvider) {
    return new DashboardViewModel_Factory(analyticsEngineProvider);
  }

  public static DashboardViewModel newInstance(AnalyticsEngine analyticsEngine) {
    return new DashboardViewModel(analyticsEngine);
  }
}
