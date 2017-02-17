package com.daksh.wordhunch.Network.AutoComplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DMSuggestions {

    @Expose
    @SerializedName("textEntryPreview")
    private String definition;

    @Expose
    @SerializedName("entryLabel")
    private String entry;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
}
