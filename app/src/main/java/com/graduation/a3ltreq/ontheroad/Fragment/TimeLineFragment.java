package com.graduation.a3ltreq.ontheroad.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graduation.a3ltreq.ontheroad.Adapter.TimeLineAdapter;
import com.graduation.a3ltreq.ontheroad.MainActivity;
import com.graduation.a3ltreq.ontheroad.R;
import com.graduation.a3ltreq.ontheroad.TimelineDetails;
import com.graduation.a3ltreq.ontheroad.activity.AddPickActivity;
import com.graduation.a3ltreq.ontheroad.helper.TimelineContract;


public class TimeLineFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, TimeLineAdapter.TimelineAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();


    private static final String[] MESSAGES_PROJECTION = {
            TimelineContract.PickEntry.COLUMN_USERS_NAME,
            TimelineContract.PickEntry.COLUMN_MESSAGES,
            TimelineContract.PickEntry.COLUMN_CREATED_AT,
            TimelineContract.PickEntry.COLUMN_LOCATION,
            TimelineContract.PickEntry.COLUMN_PICK_ID
    };
   public static final int COL_NUM_USERS_NAME = 0;
   public static final int COL_NUM_MESSAGES = 1;
   public static final int COL_NUM_CREATED_AT = 2;
  public  static final int COL_NUM_LOCATION = 3;
   public static final int COL_NUM_PICK_ID = 4;


    private RecyclerView recyclerView;
    private TimeLineAdapter mAdapter;

    private Context mContext;

    private LoaderManager mLoaderManager;

    private int mPosition = RecyclerView.NO_POSITION;

    private static final int TASK_LOADER_ID = 0;



    public TimeLineFragment() {
        // Required empty public constructor
    }


    @Override
    public void onClickMessage(int id) {

        Intent intent = new Intent(getContext(), TimelineDetails.class);
        Uri uriIdClicked = TimelineContract.buildUriWithID(id);
        intent.setData(uriIdClicked);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getContext();

        mLoaderManager = getLoaderManager();


        View rootView = inflater.inflate(R.layout.fragment_time_line, container, false);
        // Inflate the layout for this fragment

        recyclerView =  rootView.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new TimeLineAdapter(mContext, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));

        FloatingActionButton fabButton =  rootView.findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                Intent addTaskIntent = new Intent(mContext, AddPickActivity.class);
                startActivity(addTaskIntent);
            }
        });

        mLoaderManager.initLoader(TASK_LOADER_ID, null, this);

        return rootView;


    }

    @Override
    public void onStart() {
        super.onStart();
        mLoaderManager.initLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        // re-queries for all tasks
        mLoaderManager.restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case TASK_LOADER_ID:
                String selection = TimelineContract.PickEntry.COLUMN_PICK_ID;
                return new CursorLoader(getContext(), TimelineContract.PickEntry.CONTENT_URI_PICKS,
                        MESSAGES_PROJECTION, selection, null, TimelineContract.PickEntry.COLUMN_PICK_ID + " DESC");
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        recyclerView.smoothScrollToPosition(mPosition);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
