package com.daksh.wordhunch.Network.Collins.AutoComplete;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "WordList"
)
public class DMWordList {

    @Id
    @Unique
    private String word;

    @Generated(hash = 434674902)
    public DMWordList(String word) {
        this.word = word;
    }

    @Generated(hash = 1753494896)
    public DMWordList() {
    }

    public String getWord() {
        return this.word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
