package com.graduation.a3ltreq.ontheroad.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.graduation.a3ltreq.ontheroad.R;
import com.graduation.a3ltreq.ontheroad.Fragment.TimeLineFragment;

/**
 * Created by Ahmed El-Mahdi on 2/25/2017.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.MyViewHolder> {

    private Cursor mCursor;
    private final Context mContext;

    private final TimelineAdapterOnClickHandler mClickHandler;

    public interface TimelineAdapterOnClickHandler {
        void onClickMessage(int id);
    }

    public TimeLineAdapter(Context mContext, TimelineAdapterOnClickHandler mClickHandler) {

        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        String name = mCursor.getString(TimeLineFragment.COL_NUM_USERS_NAME);
        String message = mCursor.getString(TimeLineFragment.COL_NUM_MESSAGES);
        String created_at = mCursor.getString(TimeLineFragment.COL_NUM_CREATED_AT);
        String location = mCursor.getString(TimeLineFragment.COL_NUM_LOCATION);
        String pick_id = mCursor.getString(TimeLineFragment.COL_NUM_PICK_ID);



        holder.itemView.setTag(pick_id);


        holder.name.setText(name);
        holder.description.setText(message);
        holder.duration.setText(created_at);
        holder.location.setText(location);


    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

   public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView name;
        public final TextView description;
        public final TextView duration;
        public final TextView location;
        public TextView priorityView;

        public MyViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.name);
            description =  itemView.findViewById(R.id.description);
            duration =  itemView.findViewById(R.id.duration);
            location =  itemView.findViewById(R.id.location);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int id = mCursor.getInt(TimeLineFragment.COL_NUM_PICK_ID);
            mClickHandler.onClickMessage(id);


        }
    }
}
