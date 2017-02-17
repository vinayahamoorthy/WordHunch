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

    @Expose
    @SerializedName("textEntryPreview")
    @Property
    private String definition;

    @Id
    @Unique
    @Expose
    @SerializedName("entryLabel")
    private String entry;

    @Generated(hash = 2061534706)
    public DMSuggestions(String definition, String entry) {
        this.definition = definition;
        this.entry = entry;
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
}
