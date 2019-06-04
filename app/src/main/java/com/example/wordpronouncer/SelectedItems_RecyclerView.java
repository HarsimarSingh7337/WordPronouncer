package com.example.wordpronouncer;

public class SelectedItems_RecyclerView {

    private int selectedItemInRecyclerView;
    private String filePath;

    SelectedItems_RecyclerView(int selectedItemInRecyclerView, String filePath){
        this.filePath = filePath;
        this.selectedItemInRecyclerView = selectedItemInRecyclerView;
    }

    public int getSelectedItemInRecyclerView() {
        return selectedItemInRecyclerView;
    }

    public String getFilePath() {
        return filePath;
    }
}
