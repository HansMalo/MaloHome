package com.example.myfirstlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IngredAdapter.ListItemClickListener {
    private IngredAdapter mAdapter;
    private RecyclerView mIngredList;
    private EditText mInputForm;
    private Button mAddBtn;
    private Toast mToast;
    private List<IngModel> IngredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInputForm = findViewById(R.id.inputForm);
        mAddBtn=findViewById(R.id.addBtn);
        mIngredList= findViewById(R.id.rv_container);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mIngredList.setLayoutManager(layoutManager);

        mAdapter=new IngredAdapter(IngredList,this);
        mIngredList.setAdapter((mAdapter));
        //initialize IngredList
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
        Log.d("onListItemClick", "IngredList Length before removal: " + IngredList.size());
        IngredList.remove(clickedItemIndex);
        mAdapter.notifyItemRemoved(clickedItemIndex);
        Log.d("onListItemClick", "IngredList Length after removal: " + IngredList.size());

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
        String toastMessage = "Item #" + clickedItemIndex + " clicked and removed.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT);

        mToast.show();

    }

    public void btnClick(View v){

        Log.d("Button Click", "Button was clicked");
        String input=mInputForm.getText().toString();
        mInputForm.setText("");
        String[] iSpl=input.split("");
        Log.d("Button Click", "input String split: Name " + iSpl[0] +
                ", Amount" + iSpl[1] + "Unit" + iSpl[2] );
        IngModel Ingred = new IngModel(iSpl[0], Float.parseFloat(iSpl[1]), iSpl[2]);
        IngredList.add(Ingred);
        mAdapter.notifyItemInserted(IngredList.size()-1);
    }




    //dummy list for test purposes
    private void initIngredList() {
        IngModel Ingred = new IngModel("Krass", 50, "g");
        IngredList.add(Ingred);

        Ingred = new IngModel("Kas", 50, "g");
        IngredList.add(Ingred);

        Ingred = new IngModel("Kaes", 50, "g");
        IngredList.add(Ingred);

        Ingred = new IngModel("Kos", 50, "g");
        IngredList.add(Ingred);

        Ingred = new IngModel("Kus", 50, "g");
        IngredList.add(Ingred);
        Ingred = new IngModel("Kis", 50, "g");
        IngredList.add(Ingred);
        Ingred = new IngModel("Kms", 50, "g");
        IngredList.add(Ingred);
        Ingred = new IngModel("Kws", 50, "g");
        IngredList.add(Ingred);
        Ingred = new IngModel("Kqs", 50, "g");
        IngredList.add(Ingred);
        Ingred = new IngModel("Kios", 50, "g");
        IngredList.add(Ingred);
        Ingred = new IngModel("Kaeses", 2550, "g");
        IngredList.add(Ingred);
        Ingred = new IngModel("Klabss", 10, "g");
        IngredList.add(Ingred);
        Ingred = new IngModel("Kiaxs", 220, "g");
        IngredList.add(Ingred);
        mAdapter.notifyDataSetChanged();
    }

}
