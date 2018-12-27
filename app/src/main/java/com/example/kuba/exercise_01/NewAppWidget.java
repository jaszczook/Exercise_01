package com.example.kuba.exercise_01;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static final String WIDGET_IMAGE_BUTTON = "com.example.kuba.intent.action.WIDGET_IMAGE_BUTTON";
    private static boolean isStandardLogo = true;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("http://www.pja.edu.pl/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Intent imageIntent = new Intent(WIDGET_IMAGE_BUTTON);
        PendingIntent imagePendingIntent = PendingIntent.getBroadcast(context, 0, imageIntent, 0);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setOnClickPendingIntent(R.id.websiteButton, pendingIntent);
        views.setOnClickPendingIntent(R.id.logoButton, imagePendingIntent);
        views.setImageViewResource(R.id.logoImageView, R.drawable.pjwstk_logo);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        if (intent.getAction().equals(WIDGET_IMAGE_BUTTON)) {
            if (isStandardLogo) {
                remoteViews.setImageViewResource(R.id.logoImageView, R.drawable.pjatk_logo);
                isStandardLogo = false;
            } else {
                remoteViews.setImageViewResource(R.id.logoImageView, R.drawable.pjwstk_logo);
                isStandardLogo = true;
            }

            appWidgetManager.updateAppWidget(new ComponentName(context, NewAppWidget.class), remoteViews);
        }
    }
}
