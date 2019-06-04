package com.example.wordpronouncer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

import static android.os.Build.VERSION_CODES.M;

public class saved_audio_list extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<ModelClass> list = new ArrayList<>();
    private LinearLayout emptyMessageContainer;
    private ArrayList<SelectedItems_RecyclerView> selectedItems = new ArrayList<>();
    private Typeface ubuntu_regular;
    private Dialog alertDialog;
    private SearchView searchView;

    // int variable to store clicked view's position from recycler view
    private int selectedItem =-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_audio_list);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Saved files");

        emptyMessageContainer = findViewById(R.id.empty_message_container);
        emptyMessageContainer.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(list);
        recyclerView.setAdapter(adapter);

        ubuntu_regular = Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-Regular.ttf");

        prepareList();
    }

    // Adapter class for recycler view
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {

        ArrayList<ModelClass> list;
        ArrayList<ModelClass> list1;

        MyAdapter(ArrayList<ModelClass> list){
            this.list = list;
            this.list1=list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_saved_audio_list, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.fileName.setText(list.get(position).getFileName());
            holder.fileLocation.setText(list.get(position).getFileLocation());

            // if current item is selected for multiple sharing
            // make its background gray and set it as checked.
            if(list.get(position).isSelectedd()){
                holder.checkBox.setChecked(true);
                holder.container.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                holder.fileName.setTextColor(getResources().getColor(android.R.color.white));
                holder.fileLocation.setTextColor(getResources().getColor(android.R.color.white));
                holder.option.setImageDrawable(getResources().getDrawable(R.drawable.ic_option_white_24dp));
            }
            else{
                // let all the un-selected items normal as before
                holder.checkBox.setChecked(false);
                holder.container.setBackgroundColor(getResources().getColor(android.R.color.white));
                holder.fileName.setTextColor(getResources().getColor(android.R.color.black));
                holder.fileLocation.setTextColor(getResources().getColor(android.R.color.black));
                holder.option.setImageDrawable(getResources().getDrawable(R.drawable.ic_option_black_24dp));
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults = new FilterResults();

                    if(constraint.length()==0){
                        filterResults.values = list1;
                        filterResults.count = list1.size();
                    }
                    else{
                        String charString = constraint.toString().toLowerCase();
                        ArrayList<ModelClass> filterlist = new ArrayList<>();
                        for(int i=0;i<list1.size();i++){
                            if(list1.get(i).getFileName().toLowerCase().contains(charString)){
                                filterlist.add(list1.get(i));
                            }
                        }
                        filterResults.values = filterlist;
                        filterResults.count = filterlist.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    adapter.list = (ArrayList<ModelClass>) results.values;
                    adapter.notifyDataSetChanged();
                }
            };
        }

        // custom view holder class for card elements
        class MyViewHolder extends RecyclerView.ViewHolder{

            CheckBox checkBox;
            TextView fileName,fileLocation;
            ImageView option;
            ConstraintLayout container;

             MyViewHolder(@NonNull View v) {
                super(v);

                checkBox = v.findViewById(R.id.checkbox);
                fileName= v.findViewById(R.id.file_name);
                fileLocation = v.findViewById(R.id.file_location);
                option =  v.findViewById(R.id.btn_option);
                container = v.findViewById(R.id.cardview_container);

                fileName.setTypeface(ubuntu_regular);
                fileLocation.setTypeface(ubuntu_regular);

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selectedItem = getAdapterPosition();

                        if(checkBox.isChecked()){

                            // check box is checked now
                            //set selected item as true in recycler view
                            list.get(selectedItem).setSelectedd(true);
                            selectedItems.add(new SelectedItems_RecyclerView(selectedItem,MainActivity.filePath+"/"+fileName.getText().toString()));
                        }
                        else{
                            // check box is unchecked now
                            //set selected item as false in recycler view
                            list.get(selectedItem).setSelectedd(false);
                            // traversing list to check if current item exists in list
                            // then remove it as checkbox is unchecked now
                            for(int i=0;i<selectedItems.size();i++){
                                if(selectedItems.get(i).getSelectedItemInRecyclerView() == selectedItem ){
                                    selectedItems.remove(i);
                                }
                            }
                        }
                        if(!recyclerView.isComputingLayout()){
                            notifyItemChanged(selectedItem);
                        }

                        // will display selected app count and total app count in Toolbar
                        // when any checkbox is selected
                        if(!selectedItems.isEmpty()){
                            getSupportActionBar().setTitle("Selected: "+selectedItems.size()+"/"+list.size());
                            // this will recreate menu in toolbar
                            invalidateOptionsMenu();
                        }
                        else{
                            // restoring toolbar contents as App Name and Version
                            getSupportActionBar().setTitle("Saved files");
                            // this will recreate menu in toolbar
                            invalidateOptionsMenu();
                        }

                    }
                });

                option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MenuBuilder menuBuilder = new MenuBuilder(saved_audio_list.this);
                        MenuInflater menuInflater = new MenuInflater(saved_audio_list.this);
                        menuInflater.inflate(R.menu.option_menu_saved_audio_list,menuBuilder);
                        MenuPopupHelper menuPopupHelper = new MenuPopupHelper(saved_audio_list.this,menuBuilder,option);
                        menuPopupHelper.setForceShowIcon(true);
                        menuBuilder.setCallback(new MenuBuilder.Callback() {
                            @Override
                            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {

                                switch (item.getItemId()){

                                    case R.id.option_play:

                                        break;

                                    case R.id.option_share:

                                        break;

                                    case R.id.option_copy:

                                        break;

                                    case R.id.option_del:
                                        try{
                                            File file = new File(MainActivity.filePath+fileName.getText().toString());
                                            if(file.exists()){
                                                file.delete();
                                                list.remove(getAdapterPosition());
                                                if(!recyclerView.isComputingLayout()){
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }

                                            if(list.isEmpty()){
                                                // directory is empty now
                                                recyclerView.setVisibility(View.GONE);
                                                emptyMessageContainer.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        catch(Exception ignored){}
                                        break;
                                }
                                return true;
                            }

                            @Override
                            public void onMenuModeChange(MenuBuilder menu) {

                            }
                        });
                        menuPopupHelper.show();
                    }
                });
            }
        }
        // end of MyAdapter class
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem share = menu.findItem(R.id.action_share);
        MenuItem search = menu.findItem(R.id.action_search);

        searchView = (SearchView) search.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        if(selectedItems.isEmpty()){
            share.setVisible(false);
            search.setVisible(true);
        }
        else{
            share.setVisible(true);
            search.setVisible(false);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_share) {
            if (Build.VERSION.SDK_INT >= M) {
                // check runtime permissions regarding storage
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // permissions not granted yet
                    // requesting permissions
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 110);
                } else {
                    // permissions already granted, proceed with functionality
                    //noinspection unchecked
                    new ShareMultipleFiles().execute(selectedItems);
                }
            } else {
                // no need for checking runtime permissions, proceed with functionality
                //noinspection unchecked
                new ShareMultipleFiles().execute(selectedItems);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareList(){

        try{
            File directory = new File(MainActivity.filePath);
            if(directory.exists()){
                File[] files = directory.listFiles();

                if(!(files.length ==0)){
                    for(File f:files){
                        ModelClass modelClass = new ModelClass(f.getName(),"Internal Storage/Learn_Pronunciation/");
                        list.add(modelClass);
                    }
                }
                else{
                    // directory exists but empty
                    recyclerView.setVisibility(View.GONE);
                    emptyMessageContainer.setVisibility(View.VISIBLE);
                }
            }
            else{
                // no such directory exists
                directory.mkdirs();
                recyclerView.setVisibility(View.GONE);
                emptyMessageContainer.setVisibility(View.VISIBLE);
            }
        }
        catch(Exception ignored){}
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }
        else if(!selectedItems.isEmpty()) {
            list.clear();
            prepareList();
            selectedItems.clear();
            getSupportActionBar().setTitle("Saved files");
            invalidateOptionsMenu();
            if(!recyclerView.isComputingLayout()){
                adapter.notifyDataSetChanged();
            }
        }
        else{
            super.onBackPressed();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ShareMultipleFiles extends AsyncTask<ArrayList<SelectedItems_RecyclerView>,Void,ArrayList<File>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            View view = LayoutInflater.from(saved_audio_list.this).inflate(R.layout.progress_bar,null);
            AlertDialog.Builder ab=new AlertDialog.Builder(saved_audio_list.this);
            ab.setView(view);
            ab.setCancelable(false);
            alertDialog = ab.create();
            alertDialog.show();
        }

        @SafeVarargs
        @Override
        protected final ArrayList<File> doInBackground(ArrayList<SelectedItems_RecyclerView>... arrayLists) {

            ArrayList<File> tempList=new ArrayList<>();

            for(int i=0; i < arrayLists[0].size();i++){
                File file = new File(arrayLists[0].get(i).getFilePath());
                tempList.add(file);
            }
            return tempList;
        }

        @Override
        protected void onPostExecute(ArrayList<File> files) {
            super.onPostExecute(files);

            if(alertDialog!=null && alertDialog.isShowing()){
                alertDialog.dismiss();
            }

            ArrayList<Uri> uriArrayList=new ArrayList<>();
            try{
                for(int i=0; i<files.size(); i++){
                    Uri uriForFile = FileProvider.getUriForFile(getApplicationContext(),BuildConfig.APPLICATION_ID +".provider",files.get(i));
                    uriArrayList.add(uriForFile);
                }

                Intent intent=new Intent(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("audio/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM,uriArrayList);

                PackageManager packageManager = getPackageManager();
                if(packageManager.resolveActivity(intent,0) !=null ){
                    startActivity(Intent.createChooser(intent, "Share via"));
                }
            }
            catch(Exception ignored){
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==110){

            if(grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED ){
                //noinspection unchecked
                new ShareMultipleFiles().execute(selectedItems);
            }
        }
    }

    // end of main class
}