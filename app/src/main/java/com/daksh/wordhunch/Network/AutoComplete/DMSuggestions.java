package com.daksh.wordhunch.Network.AutoComplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DMSuggestions {

    @Expose
    @SerializedName("searchTerm")
    private String searchTerm;

    @Expose
    @SerializedName("dictionaryCode")
    private String dictionaryCode;

    @Expose
    @SerializedName("suggestions")
    private List<String> suggestions = new ArrayList<>();

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getDictionaryCode() {
        return dictionaryCode;
    }

    public void setDictionaryCode(String dictionaryCode) {
        this.dictionaryCode = dictionaryCode;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }
}
