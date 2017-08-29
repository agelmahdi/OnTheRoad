package com.graduation.a3ltreq.ontheroad.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Ahmed El-Mahdi on 8/28/2017.
 */

public class PicksListFactory extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new PicksWidgetAdapter(this.getApplicationContext());
    }
}
