package com.graduation.a3ltreq.ontheroad.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.graduation.a3ltreq.ontheroad.R;
import com.graduation.a3ltreq.ontheroad.model.Provider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed El-Mahdi on 7/2/2017.
 */

public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.MyViewHolder> {
    private final ArrayList<Provider> mPrvider;

    private final Context mContext;

    public ProviderAdapter(ArrayList<Provider> mPrvider, Context context) {
        this.mPrvider = mPrvider;
        this.mContext = context;
    }


    @Override
    public ProviderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.provider_list_item, parent, false);
        return new ProviderAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        final Provider provider = mPrvider.get(position);
        holder.name.setText(provider.getmName());
        holder.description.setText(provider.getS_name());
        holder.location.setText(provider.getLocation());
        holder.duration.setText(provider.getPhone());


    }

    @Override
    public int getItemCount() {
        return mPrvider.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView description;
        public final TextView duration;
        public final TextView location;
        public final View mView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name =  itemView.findViewById(R.id.p_name);
            this.description = itemView.findViewById(R.id.p_s_name);
            this.duration =  itemView.findViewById(R.id.p_phone);
            this.location = itemView.findViewById(R.id.p_location);
            mView = itemView;

        }

    }

    public void add(List<Provider> providers) {
        mPrvider.clear();
        mPrvider.addAll(providers);
        notifyDataSetChanged();
    }

    public ArrayList<Provider> getProviders() {
        return mPrvider;
    }

    public void clear() {

        mPrvider.clear();

        notifyDataSetChanged();
    }

}
