package com.example.myfirstlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements IngredAdapter.ListItemClickListener,IngredAdapter.ListItemLongClickListener{
    private IngredAdapter mAdapter;
    private RecyclerView mIngredList;
    private EditText mInputForm;
    private Button mAddBtn;
    private Button mGetCBBtn;
    private Toast mToast;
    private ArrayList<IngModel> IngredList = new ArrayList<>();
    int clickedItemIndexint=0;
    boolean editing=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInputForm = findViewById(R.id.inputForm);
        mAddBtn=findViewById(R.id.addBtn);
        mGetCBBtn =findViewById(R.id.cBBtn);
        mIngredList= findViewById(R.id.rv_container);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mIngredList.setLayoutManager(layoutManager);

        mAdapter=new IngredAdapter(IngredList, this, this);
        mIngredList.setAdapter((mAdapter));
        //initialize IngredList
        //initIngredListshrt();
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
    public void onListItemLongClick(int clickedItemIndex){
        Log.d("onListItemLongClick", "ClickedItemIndex "
                + Integer.toString(clickedItemIndex));

        //copy Text to show in editTextField
        StringBuilder builder=new StringBuilder(IngredList.get(clickedItemIndex).getName());
        builder.append(": ");
        builder.append(IngredList.get(clickedItemIndex).getAmount());
        builder.append(" ");
        builder.append(IngredList.get(clickedItemIndex).getUnit());
        mInputForm.setText(builder);

        //set flag editTextField is now used for editing an item
        editing=true;
        mAddBtn.setText("EDIT");
        mGetCBBtn.setText("Cancel");
        clickedItemIndexint=clickedItemIndex;

    }

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

    public void addBtnClick(View v) {

        Log.d("Button Click", "Button was clicked");
        String input;

        input = mInputForm.getText().toString();
        if (input.isEmpty()) {
            return;
        }
        if (editing) {
            editItemFromInput(IngredList,input, mInputForm, mAdapter);
        } else{
            Log.d("Button Click", "input String " + input);
        createItemFromInput(IngredList, input, mInputForm, mAdapter);
    }
    }

    public void cbBtnClicked (View v) {
        if (editing){
            //cancel editing and clear text
            mAddBtn.setText("ADD");
            mGetCBBtn.setText("CB");
            editing=false;
            mInputForm.setText("");
            return;
        }

        else {addItemFromClipboard();}

    }

    public void addItemFromClipboard (){

        //Completed: implement correctly, so items are propoerly displayed at the beginning
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (!(clipboard.hasPrimaryClip())) {
            if (mToast != null) {
                mToast.cancel();
                String toastMessage = "Clipboard is empty";
                mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT);

                mToast.show();
                return;
            }

            //do nothing
        } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML))) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            String cb = item.getText().toString();
            String newcb = Jsoup.clean(cb, Whitelist.none());

            //Log.d("DataFromClipboard","receiveDatafrom no plaintext "+ cb);
            //Log.d("DataFromClipboard","receiveDatafrom afterparsing "+ newcb);
            // This disables the paste menu item, since the clipboard has data but it is not plain text
            return;
        } else {
            Document.OutputSettings settings = new Document.OutputSettings();
            settings.prettyPrint(false);
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            CharSequence cb = item.getText();
            String newcb = Jsoup.clean((String) cb, "", Whitelist.none(), settings);
            String[] allcb = newcb.split("\n");
            //Complete resolve issue for proper handling to search for dublicate items
            boolean isInList = false;
            for (int k = 0; k < allcb.length; k++) {
                String[] input = allcb[k].split("\t");
                String name = input[1];
                //TODO: better input handling here
                String[] input2 = input[0].split(" ");
                float amount=0;
                try {
                    amount = Float.parseFloat(input2[0]);
                    for (int i = 0; i < IngredList.size(); i++) {
                        if (name.equalsIgnoreCase(IngredList.get(i).getName())) {
                            //Log.d("Button Click", "Entered dublicate Name Checker");
                            float oldAmount = IngredList.get(i).getAmount();
                            IngredList.get(i).setAmount(oldAmount + amount);
                            mAdapter.notifyDataSetChanged();
                            isInList = true;
                            i = IngredList.size();


                        }


                    }
                }
                catch (Exception e){
                    //TODO: exception handling for non float characters in Clipboard copy past routine
                    amount=0;
                }
                if (!isInList) {
                    IngModel Ingred = new IngModel(name, amount, input2[1]);
                    IngredList.add(Ingred);
                    mAdapter.notifyItemInserted(IngredList.size() - 1);
                    ;
                    //mAdapter.notifyItemChanged(IngredList.size());
                }

            }
            //mAdapter.notifyDataSetChanged();

            //Log.d("DataFromClipboard","receiveDatafrom CB performed \n" + cb);
            //Log.d("DataFromClipboard","receiveDatafrom newCB performed \n" + allcb[1]+allcb[2]);

            return;
        }
    }
    public void editItemFromInput(ArrayList <IngModel> List, String input, EditText mInputForm, IngredAdapter mAdapter ){

        //TODO: add case handling for different input styles i.e. input does not correspond to expected format

        try{
            mAddBtn.setText("ADD");
            mGetCBBtn.setText("CB");
            String[] iSpl=input.split(":");
            Pattern inputPattern=Pattern.compile("(\\d+\\.\\d)\\s*([a-zA-Z]+)");
            String name=iSpl[0];
            String samount=" ";
            String unit="";
            Matcher inputMatcher=inputPattern.matcher(iSpl[1]);
            if (inputMatcher.find(0)){
                samount=inputMatcher.group(1);
                unit=inputMatcher.group(2);}

            float famount=Float.parseFloat(samount);
            mInputForm.setText("");
            IngredList.get(clickedItemIndexint).setName(name);
            IngredList.get(clickedItemIndexint).setAmount(famount);
            IngredList.get(clickedItemIndexint).setUnit(unit);
            mAdapter.notifyItemChanged(clickedItemIndexint);
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

    public void createItemFromInput(ArrayList <IngModel> List, String input, EditText mInputForm, IngredAdapter mAdapter ){
        Pattern inputPattern=Pattern.compile("(\\d+)\\s*([a-zA-Z]+)");
        String[] iSpl=input.split(":");
        String name=iSpl[0];
        String samount=" ";
        String unit="";
        Matcher inputMatcher=inputPattern.matcher(iSpl[1]);

        if (inputMatcher.find(0)){
            samount=inputMatcher.group(1);
            unit=inputMatcher.group(2);}

        //TODO: add case handling for different input styles i.e. input does not correspond to expected format

        try{
            float famount=Float.parseFloat(samount);
            mInputForm.setText("");
                /*should work
                 Log.d("Button Click", "input String split: Name " + iSpl[0] +
                       ", Amount " + iSpl[1] + ", Unit " + iSpl[2] );
                */
            //COMPLETE: check if added Ingredient name is already in IngredList
            for (int i=0;i<List.size();i++) {
                if (iSpl[0].equalsIgnoreCase(List.get(i).getName())) {
                    //Log.d("Button Click", "Entered dublicate Name Checker");
                    float oldAmount = List.get(i).getAmount();
                    List.get(i).setAmount(oldAmount + famount);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
            }
            IngModel Ingred = new IngModel(name, famount, unit);
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


    //initalize List
    private void initIngredListshrt(){
        IngModel Ingred = new IngModel("Krass", 50, "g");
        IngredList.add(Ingred);
        mAdapter.notifyDataSetChanged();
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