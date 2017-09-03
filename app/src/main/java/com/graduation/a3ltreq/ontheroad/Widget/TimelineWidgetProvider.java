package com.graduation.a3ltreq.ontheroad.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.graduation.a3ltreq.ontheroad.R;
import com.graduation.a3ltreq.ontheroad.TimelineDetails;

/**
 * Implementation of App Widget functionality.
 */
public class TimelineWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, PicksListFactory.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.timeline_widget);
            rv.setRemoteAdapter(R.id.picks_listview, intent);
            Intent startActivityIntent = new Intent(context, TimelineDetails.class);
            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.picks_listview, startActivityPendingIntent);
            rv.setEmptyView(R.id.picks_listview, R.id.empty_view);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);


    }
    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            int appWidgetIds[] = mgr.getAppWidgetIds(new ComponentName(context, TimelineWidgetProvider.class));
            mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.picks_listview);
            Log.d("received", intent.getAction());

        }
        super.onReceive(context, intent);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

