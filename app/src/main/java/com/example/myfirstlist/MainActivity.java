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

import java.text.DecimalFormat;
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
        //legacy from debugging: initialize IngredList
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

        //legacy from tutorial
        /*
        String toastMessage = "Item #" + clickedItemIndex + " clicked and removed.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT);

        mToast.show();
        */

    }

    public void addBtnClick(View v) {

        Log.d("Button Click", "Button was clicked");
        String input;

        input = mInputForm.getText().toString();
        if (editing) {
            if (input.isEmpty()) {
                mAddBtn.setText("ADD");
                mGetCBBtn.setText("CB");
                mAdapter.notifyDataSetChanged();
                return;
            }
            editing=false;
            editItemFromInput(IngredList,input, mInputForm, mAdapter);
        } else{
            if (input.isEmpty()) {
                return;
            }
            Log.d("Button Click", "input String " + input);
            createItemFromInput(IngredList, input, mInputForm, mAdapter);
            mInputForm.setText("");
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
            }

            //do nothing
        }  else if (
                (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))|
                        (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML))){
            Document.OutputSettings settings = new Document.OutputSettings();
            //setting prettyprint false to keep \n and other formating chars
            settings.prettyPrint(false);
            //read data from clipboard
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            CharSequence cb = item.getText();
            //using Jsoup module to strip html from string
            String newcb = Jsoup.clean((String) cb, "", Whitelist.none(), settings);
            String[] allcb = newcb.split("\n");
            //Complete resolve issue for proper handling to search for dublicate items
            boolean isInList = false;
            for (int k = 0; k < allcb.length; k++) {
                isInList=false;
                //most recipe lists from websites are seperated by \t between ingredient and amount unit
                //Complete: better input handling here
                Pattern inputPattern=Pattern.compile("(\\d+\\.*\\d*)\\s*([a-zA-Z]*)");
                String samount=" ";
                String unit="";
                String name="";
                try {
                    String[] input = allcb[k].split("\t");
                    name = input[1];
                    Matcher inputMatcher=inputPattern.matcher(input[0]);

                if (inputMatcher.find(0)){
                    samount=inputMatcher.group(1);
                    unit=inputMatcher.group(2);}
                DecimalFormat decimalFormat=new DecimalFormat("#.###");
                float famount=0;
                float fOldAmount=0;

                    try {
                        famount = Float.parseFloat(samount);
                        for (int i = 0; i < IngredList.size(); i++) {
                            if (name.equalsIgnoreCase(IngredList.get(i).getName())) {
                                //Log.d("Button Click", "Entered dublicate Name Checker");
                                String oldAmount = IngredList.get(i).getAmount();
                                fOldAmount = Float.parseFloat(oldAmount);
                                String newAmount = decimalFormat.format(fOldAmount + famount);
                                IngredList.get(i).setAmount(newAmount);
                                mAdapter.notifyDataSetChanged();
                                isInList = true;
                                i = IngredList.size();
                            }
                        }
                    } catch (NumberFormatException e) {
                        samount=inputMatcher.group(1);
                        if (mToast != null) {
                            mToast.cancel();
                        }
                        String toastMessage = "Item" + name + "has non number format of amount, cannot calculate";
                        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT);
                        mToast.show();
                    }
                }
                catch (Exception e){
                    //TODO: proper exception handling for non float characters in Clipboard copy past routine
                    name="";
                    samount="";
                    unit="";

                }
                if (!isInList) {
                    IngModel Ingred = new IngModel(name, samount, unit);
                    IngredList.add(Ingred);
                    mAdapter.notifyItemInserted(IngredList.size() - 1);
                }

            }
            //mAdapter.notifyDataSetChanged();

            //Log.d("DataFromClipboard","receiveDatafrom CB performed \n" + cb);
            //Log.d("DataFromClipboard","receiveDatafrom newCB performed \n" + allcb[1]+allcb[2]);

            }
    }
    public void editItemFromInput(ArrayList <IngModel> List, String input, EditText mInputForm, IngredAdapter mAdapter ){

        //TODO: add case handling for different input styles i.e. input does not correspond to expected format

            mAddBtn.setText("ADD");
            mGetCBBtn.setText("CB");
            String[] iSpl=input.split(":");
            String samount = " ";
            String unit = "";
            String name = iSpl[0];
            if (iSpl.length>1) {
                Pattern inputPattern = Pattern.compile("(\\d+\\.*\\d*)\\s*([a-zA-Z]*)");
                Matcher inputMatcher = inputPattern.matcher(iSpl[1]);
                //check if any amount or unit were given
                if (inputMatcher.find(0)) {
                    samount = inputMatcher.group(1);
                    unit = inputMatcher.group(2);
                    try{
                        float famount=Float.parseFloat(samount);
                    } catch (Exception e) {
                        if (mToast != null) {
                            mToast.cancel();
                        }
                        mToast = Toast.makeText(this, "amount is not a number, cannot calculate", Toast.LENGTH_SHORT);
                        mToast.setGravity(Gravity.CENTER,0,0);

                        mToast.show();
                        e.printStackTrace();
                    }


                }
                else{unit=iSpl[1];}

                mInputForm.setText("");
                IngredList.get(clickedItemIndexint).setName(name);
                IngredList.get(clickedItemIndexint).setAmount(samount);
                IngredList.get(clickedItemIndexint).setUnit(unit);
                mAdapter.notifyItemChanged(clickedItemIndexint);
            }
            else {
                IngredList.get(clickedItemIndexint).setName(name);
                IngredList.get(clickedItemIndexint).setAmount("");
                IngredList.get(clickedItemIndexint).setUnit(unit);
            }









    }

    public void createItemFromInput(ArrayList <IngModel> List, String input, EditText mInputForm, IngredAdapter mAdapter ){
        Pattern inputPattern=Pattern.compile("(\\d+\\.*\\d*)\\s*([a-zA-Z]*)");
        String[] iSpl=input.split(":");
        String name=iSpl[0];
        String samount = " ";
        String unit = "";
        //maybe one "if" case can be omitted if regex inputPattern fits better to input
        if(iSpl.length>1) {
            Matcher inputMatcher = inputPattern.matcher(iSpl[1]);

            if (inputMatcher.find(0)) {
                samount = inputMatcher.group(1);
                unit = inputMatcher.group(2);
                mInputForm.setText("");


                    //COMPLETE: check if added Ingredient name is already in IngredList
                    for (int i = 0; i < List.size(); i++) {
                        if (iSpl[0].equalsIgnoreCase(List.get(i).getName())) {
                            try {
                                float famount = Float.parseFloat(samount);
                                float oldAmount = Float.parseFloat(List.get(i).getAmount());
                                DecimalFormat decimalFormat = new DecimalFormat("#.###");
                                //Log.d("Button Click", "Entered dublicate Name Checker");
                                List.get(i).setAmount(decimalFormat.format(oldAmount + famount));
                                mAdapter.notifyDataSetChanged();
                                return;
                            } catch (NumberFormatException e) {
                                if (mToast != null) {
                                    mToast.cancel();
                                }
                                String toastMessage = "Item" + name + "has non number format of amount, cannot calculate";
                                mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT);
                                mToast.show();
                                return;
                            }
                        }
                    }
                    IngModel Ingred = new IngModel(name, samount, unit);
                    IngredList.add(Ingred);
                    mAdapter.notifyItemInserted(IngredList.size() - 1);
            }
            //use unit as text output if no reasonable number was given
            else{unit=iSpl[1];
                IngModel Ingred = new IngModel(name, "", unit);
                IngredList.add(Ingred);
                mAdapter.notifyItemInserted(IngredList.size() - 1);
                            }

        }
        else {
            IngModel Ingred = new IngModel(name, "", unit);
            IngredList.add(Ingred);
            mAdapter.notifyItemInserted(IngredList.size() - 1);
        }
    }


    //initalize List
    private void initIngredListshrt(){
        IngModel Ingred = new IngModel("Krass", "50", "g");
        IngredList.add(Ingred);
        mAdapter.notifyDataSetChanged();
    }





}