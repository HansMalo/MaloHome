package com.example.myfirstlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IngredAdapter.ListItemClickListener {
    private IngredAdapter mAdapter;
    private RecyclerView mIngredList;
    private Toast mToast;
    private List<IngModel> IngredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize IngredList

        mIngredList= findViewById(R.id.rv_container);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mIngredList.setLayoutManager(layoutManager);

        mAdapter=new IngredAdapter(IngredList,this);
        mIngredList.setAdapter((mAdapter));
        initIngredList();


    }

    // COMPLETED (10) Override ListItemClickListener's onListItemClick method
    /**
     * This is where we receive our callback from
     * {@link com.example.myfirstlist.IngredAdapter.ListItemClickListener}
     *
     * This callback is invoked when you click on an item in the list.
     *
     * @param clickedItemIndex Index in the list of the item that was clicked.
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        // COMPLETED (11) In the beginning of the method, cancel the Toast if it isn't null
        /*
         * Even if a Toast isn't showing, it's okay to cancel it. Doing so
         * ensures that our new Toast will show immediately, rather than
         * being delayed while other pending Toasts are shown.
         *
         * Comment out these three lines, run the app, and click on a bunch of
         * different items if you're not sure what I'm talking about.
         */
        if (mToast != null) {
            mToast.cancel();
        }

        // COMPLETED (12) Show a Toast when an item is clicked, displaying that item number that was clicked
        /*
         * Create a Toast and store it in our Toast field.
         * The Toast that shows up will have a message similar to the following:
         *
         *                     Item #42 clicked.
         */
        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);

        mToast.show();
    }

    private void initIngredList() {
        IngModel Ingred = new IngModel("Krass", "g", 50);
        IngredList.add(Ingred);

        Ingred = new IngModel("Kas", "g", 50);
        IngredList.add(Ingred);

        Ingred = new IngModel("Kaes", "g", 50);
        IngredList.add(Ingred);

        Ingred = new IngModel("Kos", "g", 50);
        IngredList.add(Ingred);

        Ingred = new IngModel("Kus", "g", 50);
        IngredList.add(Ingred);
        Ingred = new IngModel("Kis", "g", 50);
        IngredList.add(Ingred);
        Ingred = new IngModel("Kms", "g", 50);
        IngredList.add(Ingred);
        Ingred = new IngModel("Kws", "g", 50);
        IngredList.add(Ingred);
        Ingred = new IngModel("Kqs", "g", 50);
        IngredList.add(Ingred);
        Ingred = new IngModel("Kios", "g", 50);
        IngredList.add(Ingred);
        mAdapter.notifyDataSetChanged();
    }

}
