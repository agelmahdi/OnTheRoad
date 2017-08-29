package com.graduation.a3ltreq.ontheroad.Widget;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.graduation.a3ltreq.ontheroad.R;
import com.graduation.a3ltreq.ontheroad.helper.TimelineContract;


/**
 * Created by Ahmed El-Mahdi on 8/28/2017.
 */

 class PicksWidgetAdapter implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;

    PicksWidgetAdapter(Context applicationContext) {


        mContext = applicationContext;
    }

    @Override
    public void onCreate() {


    }

    @Override
    public void onDataSetChanged() {

        Uri PICKS_URI = TimelineContract.PickEntry.CONTENT_URI_PICKS;
        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(
                PICKS_URI,
                null,
                null,
                null,
                TimelineContract.PickEntry.COLUMN_PICK_ID + " DESC"
        );

    }

    @Override
    public void onDestroy() {
        mCursor.close();

    }

    @Override
    public int getCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(i);
        int idIndex = mCursor.getColumnIndex(TimelineContract.PickEntry._ID);
        int nameIndex = mCursor.getColumnIndex(TimelineContract.PickEntry.COLUMN_USERS_NAME);
        int messageIndex = mCursor.getColumnIndex(TimelineContract.PickEntry.COLUMN_MESSAGES);
        int created_atIndex = mCursor.getColumnIndex(TimelineContract.PickEntry.COLUMN_CREATED_AT);
        int locationIndex = mCursor.getColumnIndex(TimelineContract.PickEntry.COLUMN_LOCATION);
        int pick_idIndex = mCursor.getColumnIndex(TimelineContract.PickEntry.COLUMN_PICK_ID);


        String name = mCursor.getString(nameIndex);
        String message = mCursor.getString(messageIndex);
        String created_at = mCursor.getString(created_atIndex);
        String location = mCursor.getString(locationIndex);
        String pick_id = mCursor.getString(pick_idIndex);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        views.setTextViewText(R.id.w_name, name);
        views.setTextViewText(R.id.w_description, message);
        views.setTextViewText(R.id.w_duration, created_at);
        views.setTextViewText(R.id.w_location, location);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
