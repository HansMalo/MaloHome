package com.example.myfirstlist;


import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class IngredAdapter extends RecyclerView.Adapter<IngredAdapter.IngredViewHolder>{
    private static final String TAG = IngredAdapter.class.getSimpleName();
    private List<IngModel> mIngredList;
    //onClickListener to make it easy for Activity to interface with RecyclerView
    final private ListItemClickListener mOnClickListener;

    private static int viewHolderCount;

    //private int mNumberItems;
//interface for onListItemClick handling
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);

    }
/* Constructor for IngredAdapter that accepts a number of items to display and the specification
     * for the ListItemClickListener. */
    public IngredAdapter(ListItemClickListener mOnClickListener,List<IngModel> IngredList) {
        mIngredList=IngredList;
        this.mOnClickListener = mOnClickListener;
        viewHolderCount=0;
    }

    /**
     *
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link androidx.recyclerview.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new NumberViewHolder that holds the View for each list item
     */


    @Override
    public IngredViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewType){

        Context context=viewGroup.getContext();
        int layoutIdForListItem=R.layout.ing_list_item;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup,shouldAttachToParentImmediately);
        IngredViewHolder viewHolder=new IngredViewHolder(view);

        viewHolder.getItemId().setText



        viewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + viewHolderCount);
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
    public void onBindViewHolder(IngredViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }



    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    // COMPLETED (5) Implement OnClickListener in the NumberViewHolder class
    /**
     * Cache of the children views for a list item.
     */

    class IngredViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
        // Will display the position in the list, ie 0 through getItemCount() - 1
        TextView ingView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param itemView The View that you inflated in
         *                 {@link IngredAdapter#onCreateViewHolder(ViewGroup, int)}
         */

        public IngredViewHolder (View itemView){
            super(itemView);

            ingView=(TextView) ingView.findViewById(R.id.ingView);
            ingView.setOnClickListener(this);
        }

        public void setIngredient(Context context, View MainActivity, IngModel mIngModel){
            /*float Amount=mIngModel.getAmount();
            String Unit=mIngModel.getUnit();
            String Name=mIngModel.getName();
            String Category=mIngModel.getCategory(); */
            StringBuilder builder=new StringBuilder(mIngModel.getName());
            builder.append(Float.toString(mIngModel.getAmount()));
            builder.append(mIngModel.getUnit());
            ingView.setText(builder);

            //not done yet
            /*itemView.setOnClickListener(view-> {

            });


             */

        }

        /*
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         * @param listIndex Position of the item in the list
         *

        void bind(int listIndex) {
            ingView.setText(String.valueOf(listIndex));
        }
        */

        // COMPLETED (6) Override onClick, passing the clicked item's position (getAdapterPosition()) to mOnClickListener via its onListItemClick method
        /**
         * Called whenever a user clicks on an item in the list.
         * @param v The View that was clicked
         */

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }



}

