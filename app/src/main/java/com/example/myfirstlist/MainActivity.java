package com.example.myfirstlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IngredAdapter.ListItemClickListener {
    private IngredAdapter mAdapter;
    private RecyclerView mIngredList;
    private EditText mInputForm;
    private Button mAddBtn;
    private Button mGetCP;
    private Toast mToast;
    private ArrayList<IngModel> IngredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInputForm = findViewById(R.id.inputForm);
        mAddBtn=findViewById(R.id.addBtn);
        mGetCP=findViewById(R.id.cBBtn);
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
        String input;

        input=mInputForm.getText().toString();
        if (input.isEmpty()){ return;}
        Log.d("Button Click", "input String " + input );
        String[] iSpl=input.split(" ");
        //TODO: add case handling for different input styles i.e. input does not correspond to expected format
        try{
            float amount=Float.parseFloat(iSpl[1]);
            mInputForm.setText("");
            /*should work
             Log.d("Button Click", "input String split: Name " + iSpl[0] +
                   ", Amount " + iSpl[1] + ", Unit " + iSpl[2] );
            */
            //COMPLETE: check if added Ingredient is already in IngredList
            for (int i=0;i<IngredList.size();i++) {
                if (iSpl[0].equalsIgnoreCase(IngredList.get(i).getName())) {
                    //Log.d("Button Click", "Entered dublicate Name Checker");
                    float oldAmount = IngredList.get(i).getAmount();
                    IngredList.get(i).setAmount(oldAmount + amount);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }




            IngModel Ingred = new IngModel(iSpl[0], amount, iSpl[2]);
            IngredList.add(Ingred);


            mAdapter.notifyItemInserted(IngredList.size()-1);
        }
        catch (Exception e){
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(this, "please use the input pattern :)", Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER,0,0);

            mToast.show();
            return;

        }

    }

    public void receiveDataFromClipboard(View v) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (!(clipboard.hasPrimaryClip())) {
            return;
            //do nothing or throw error?
        } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML))) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            String cb=item.getText().toString();
            String newcb= Jsoup.parse(cb).text();

            Log.d("DataFromClipboard","receiveDatafrom no plaintext "+ cb);
            Log.d("DataFromClipboard","receiveDatafrom afterparsing "+ newcb);
            // This disables the paste menu item, since the clipboard has data but it is not plain text
            return;
        } else {

            // This enables the paste menu item, since the clipboard contains plain text.
            ClipData.Item item1 = clipboard.getPrimaryClip().getItemAt(0);
            String mitem2=item1.getText().toString();
            String newcb= Jsoup.parse(mitem2).text();
            Log.d("DataFromClipboard","receiveDatafrom CB performed " + mitem2);
            Log.d("DataFromClipboard","receiveDatafrom CB performed " + newcb);
            return;
        }

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
        /*Ingred = new IngModel("Kios", 50, "g");
        IngredList.add(Ingred);
        Ingred = new IngModel("Kaeses", 2550, "g");
        IngredList.add(Ingred);
        Ingred = new IngModel("Klabss", 10, "g");
        IngredList.add(Ingred);
        Ingred = new IngModel("Kiaxs", 220, "g");
        IngredList.add(Ingred); */
        mAdapter.notifyDataSetChanged();
    }

}
