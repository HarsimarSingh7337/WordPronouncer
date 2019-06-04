package com.example.wordpronouncer;

public class ModelClass {

    private String FileName;
    private String FileLocation;
    private boolean isSelectedd;

    ModelClass(){

    }

    ModelClass(String fileName, String fileLocation){
        this.FileLocation = fileLocation;
        this.FileName = fileName;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileLocation() {
        return FileLocation;
    }

    public void setFileLocation(String fileLocation) {
        FileLocation = fileLocation;
    }

    public boolean isSelectedd() {
        return isSelectedd;
    }

    public void setSelectedd(boolean selectedd) {
        isSelectedd = selectedd;
    }
}
