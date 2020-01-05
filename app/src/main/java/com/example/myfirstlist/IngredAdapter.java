package com.example.myfirstlist;


import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
     * for the ListItemClickListener.
     * @param List<IngModel> List with IngredientsModel objects as described in the IngModel class
     * @param listener Listener for list item clicks */
    public IngredAdapter(List<IngModel> IngredList, ListItemClickListener listener) {
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
    public IngredViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        Context context=parent.getContext();
        int layoutIdForListItem=R.layout.ing_list_item;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View itemView = inflater.inflate(R.layout.ing_list_item, parent,shouldAttachToParentImmediately);
        IngredViewHolder viewHolder=new IngredViewHolder(itemView);
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

    class IngredViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
        // Will display the position in the list, ie 0 through getItemCount() - 1
        public TextView ingView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param view The View that you inflated in
         *                 {@link IngredAdapter#onCreateViewHolder(ViewGroup, int)}
         */

        public IngredViewHolder (View view){
            super(view);
            ingView=(TextView) view.findViewById(R.id.ingView);
            ingView.setOnClickListener(this);
        }

        public void setIngredient(IngModel mIngModel){
            /*float Amount=mIngModel.getAmount();
            String Unit=mIngModel.getUnit();
            String Name=mIngModel.getName();
            String Category=mIngModel.getCategory(); */
            StringBuilder builder=new StringBuilder(mIngModel.getName());
            builder.append(mIngModel.getAmount());
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
         */

        void bind(int listIndex) {
            ingView.setText(String.valueOf(listIndex));
        }


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

