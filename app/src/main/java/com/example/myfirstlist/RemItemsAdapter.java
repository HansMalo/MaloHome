package com.example.myfirstlist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RemItemsAdapter extends RecyclerView.Adapter<RemItemsAdapter.RemItemsViewHolder>{

    private ArrayList<IngModel> mIngredList;
    //onClickListener to make it easy for Activity to interface with RecyclerView
    //remove item on click
    final private remListItemClickListener mOnClickListener;
    //onLongClickListener same as clicklistener, for Item editing

    private static int viewHolderCount;
    DecimalFormat decimalFormat=new DecimalFormat("#.###");

    //private int mNumberItems;
//interface for onListItemClick handling

    public interface remListItemClickListener {
        void onRemListItemClick(int clickedItemIndex);
    }


    /* Constructor for IngredAdapter that accepts a number of items to display and the specification
     * for the ListItemClickListener.
     * @param List<IngModel> List with IngredientsModel objects as described in the IngModel class
     * @param listener Listener for list item clicks */
    public RemItemsAdapter(ArrayList<IngModel> IngredList, RemItemsAdapter.remListItemClickListener listener) {
        this.mIngredList=IngredList;
        mOnClickListener=listener;
        viewHolderCount=0;
    }

    /**
     *
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link androidx.recyclerview.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new NumberViewHolder that holds the View for each list item
     */


    @Override
    public RemItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context=parent.getContext();
        int layoutIdForListItem=R.layout.ing_list_item;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View itemView = inflater.inflate(R.layout.ing_list_item, parent,shouldAttachToParentImmediately);
        RemItemsViewHolder viewHolder=new RemItemsViewHolder(itemView);
        viewHolderCount++;
        return viewHolder;

    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder(RemItemsAdapter.RemItemsViewHolder holder, int position) {
        String spc=" ";
        StringBuilder builder=new StringBuilder(mIngredList.get(position).getName());
        builder.append(spc);
        builder.append(mIngredList.get(position).getAmount());
        builder.append(mIngredList.get(position).getUnit());
        holder.ingView.setText(builder);
    }



    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        return mIngredList.size();
    }

    // COMPLETED (5) Implement OnClickListener in the NumberViewHolder class
    /**
     * Cache of the children views for a list item.
     */

    class RemItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Will display the position in the list, ie 0 through getItemCount() - 1
        public TextView ingView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param view The View that you inflated in
         *                 {@link IngredAdapter#onCreateViewHolder(ViewGroup, int)}
         */

        public RemItemsViewHolder (View view){
            super(view);
            ingView=view.findViewById(R.id.ingView);
            ingView.setOnClickListener(this);
        }

        /*
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         * @param listIndex Position of the item in the list
         *
         */

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onRemListItemClick(clickedPosition);
        }


    }



}
