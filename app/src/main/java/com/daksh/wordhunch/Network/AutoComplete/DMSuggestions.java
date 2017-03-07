package com.daksh.wordhunch.Network.AutoComplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

@Entity(
        nameInDb = "Suggestions"
)
public class DMSuggestions {

    @Id
    @Unique
    @Expose
    @SerializedName("entryLabel")
    private String entry;

    @Expose
    @SerializedName("textEntryPreview")
    @Property
    private String definition;

    @Property
    private String date;

    @Generated(hash = 1099616769)
    public DMSuggestions(String entry, String definition, String date) {
        this.entry = entry;
        this.definition = definition;
        this.date = date;
    }

    @Generated(hash = 894785932)
    public DMSuggestions() {
    }

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

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
