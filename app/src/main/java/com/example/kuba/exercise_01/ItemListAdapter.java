package com.example.kuba.exercise_01;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder> {

    private List<Item> itemList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemListAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new ItemListViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ItemListViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Item item = itemList.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(String.valueOf(item.getPrice()).concat(" PLN"));
        holder.quantity.setText(String.valueOf(Integer.toString(item.getQuantity())));
        holder.isBought.setChecked(item.isBought());

        holder.isBought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.isBought.isChecked()) {
                    holder.isBought.setChecked(true);
                    item.setBought(true);
                } else if (!holder.isBought.isChecked()) {
                    holder.isBought.setChecked(false);
                    item.setBought(false);
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ItemListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name, price, quantity;
        public CheckBox isBought;

        public ItemListViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            price = view.findViewById(R.id.price);
            quantity = view.findViewById(R.id.quantity);
            isBought = view.findViewById(R.id.isBought);
        }
    }
}
