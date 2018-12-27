package com.example.kuba.exercise_01;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ShopListViewHolder> {

    private List<Shop> shopList;

    public ShopListAdapter(List<Shop> shopList) {
        this.shopList = shopList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ShopListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_list_row, parent, false);

        return new ShopListViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ShopListAdapter.ShopListViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Shop shop = shopList.get(position);
        holder.name.setText(shop.getName());
        holder.description.setText(String.valueOf(shop.getDescription()));
        if (shop.getRadius() == 1) {
            holder.radius.setText(String.valueOf(shop.getRadius()).concat(" meter"));
        } else {
            holder.radius.setText(String.valueOf(shop.getRadius()).concat(" meters"));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return shopList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ShopListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name, description, radius;

        public ShopListViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            radius = view.findViewById(R.id.radius);
        }
    }
}
