package com.example.wordpronouncer;

import java.util.Locale;

public class Locale_Name_Language {

    private String LocaleName;
    private Locale localeLanguage;

    Locale_Name_Language(String name, Locale lang){
        this.localeLanguage =lang;
        this.LocaleName=name;
    }

    public String getLocaleName() {
        return LocaleName;
    }

    public Locale getLocaleLanguage() {
        return localeLanguage;
    }

}
