package com.mapbox.services.android.navigation.ui.v5;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.v5.milestone.Milestone;
import com.mapbox.services.android.navigation.v5.milestone.MilestoneEventListener;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigationOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationConstants;
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;

import java.util.List;

import WeatherService.Interface.NextMilestoneSetter;

import static WeatherService.Methods.myUtils.getCorrection;
import static WeatherService.Methods.myUtils.mycustomMilestone;

/**
 * Serves as a launching point for the custom drop-in UI, {@link NavigationView}.
 * <p>
 * Demonstrates the proper setup and usage of the view, including all lifecycle methods.
 */
public class MapboxNavigationActivity extends AppCompatActivity
        implements OnNavigationReadyCallback,
        MilestoneEventListener,
  NavigationListener,
        ProgressChangeListener, NextMilestoneSetter {

  private NavigationView navigationView;

  int nextMilestone;



  public MapboxNavigationActivity() {
    nextMilestone = 0;
  }


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    setTheme(R.style.Theme_AppCompat_NoActionBar);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_navigation);
    navigationView = findViewById(R.id.navigationView);
    navigationView.setListener(this);
    navigationView.onCreate(savedInstanceState);
    navigationView.setActivity(this);
    initialize();
  }

  @Override
  public void onStart() {
    super.onStart();
    navigationView.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    navigationView.onResume();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    navigationView.onLowMemory();
  }

  @Override
  public void onBackPressed() {
    // If the navigation view didn't need to do anything, call super
    if (!navigationView.onBackPressed()) {
      super.onBackPressed();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    navigationView.onSaveInstanceState(outState);
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    navigationView.onRestoreInstanceState(savedInstanceState);
  }

  @Override
  public void onPause() {
    super.onPause();
    navigationView.onPause();
  }

  @Override
  public void onStop() {
    super.onStop();
    navigationView.onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    navigationView.onDestroy();
  }

  @Override
  public void onNavigationReady(boolean isRunning) {
    NavigationViewOptions.Builder options = NavigationViewOptions.builder();
    options.navigationListener(this);
    extractRoute(options);
    options.progressChangeListener(this);
    extractConfiguration(options);
    options.milestones(mycustomMilestone());
    options.milestoneEventListener(this);
    options.navigationOptions(MapboxNavigationOptions.builder().build());
    navigationView.startNavigation(options.build());
  }

  @Override
  public void onCancelNavigation() {
    finishNavigation();
  }

  @Override
  public void onNavigationFinished() {
    finishNavigation();
  }

  @Override
  public void onNavigationRunning() {
    // Intentionally empty
  }

  private void initialize() {
    Parcelable position = getIntent().getParcelableExtra(NavigationConstants.NAVIGATION_VIEW_INITIAL_MAP_POSITION);
    if (position != null) {
      navigationView.initialize(this, (CameraPosition) position);
    } else {
      navigationView.initialize(this);
    }
  }

  List<LegStep> steps;
  private void extractRoute(NavigationViewOptions.Builder options) {
    DirectionsRoute route = NavigationLauncher.extractRoute(this);
    steps = route.legs().get(0).steps();
    options.directionsRoute(route);

  }

  private void extractConfiguration(NavigationViewOptions.Builder options) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    options.shouldSimulateRoute(preferences.getBoolean(NavigationConstants.NAVIGATION_VIEW_SIMULATE_ROUTE, false));
    String offlinePath = preferences.getString(NavigationConstants.OFFLINE_PATH_KEY, "");
    if (!offlinePath.isEmpty()) {
      options.offlineRoutingTilesPath(offlinePath);
    }
    String offlineVersion = preferences.getString(NavigationConstants.OFFLINE_VERSION_KEY, "");
    if (!offlineVersion.isEmpty()) {
      options.offlineRoutingTilesVersion(offlineVersion);
    }
  }

  private void finishNavigation() {
    NavigationLauncher.cleanUpPreferences(this);
    finish();
  }


  @Override
  public void onMilestoneEvent(RouteProgress routeProgress, String instruction, Milestone milestone) {

// &&
    if(milestone.getIdentifier()==1000){
      Log.d("new step :",""+routeProgress.currentLegProgress().stepIndex());
     setnextMilestone(200);
//     if(routeProgress.currentLegProgress().currentStep().distance()>1000)
//     navigationView.updateWeather(routeProgress.directionsRoute(),
//             routeProgress.currentLegProgress().stepIndex(),
//             null);
    }

  }



  void setnextMilestone(int val){
    synchronized(this){
      this.nextMilestone = val;
    }
  }
   int getMilestone(){
    return nextMilestone;
  }

  @Override
  public void onProgressChange(Location location, RouteProgress routeProgress) {

    Log.d("dist travelled :",String.valueOf(routeProgress.currentLegProgress().currentStepProgress().distanceTraveled()));

    if(routeProgress.currentLegProgress().currentStepProgress().distanceTraveled()>=getMilestone()){
      setnextMilestone(getMilestone()+1000);

      navigationView.updateWeather(routeProgress.directionsRoute()
              ,routeProgress.currentLegProgress().stepIndex(),
              getCorrection(location,routeProgress));
     }
    }


  @Override
  public void updateNextMilestone(int val) {
    setnextMilestone(val);
  }
}
