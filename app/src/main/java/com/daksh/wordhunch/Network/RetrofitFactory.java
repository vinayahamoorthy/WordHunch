package com.daksh.wordhunch.Network;

import com.daksh.wordhunch.Network.Collins.CollinsRetroFit;
import com.daksh.wordhunch.Network.WordList.WordListRetroFit;

public class RetrofitFactory {

    private static Retrofit retrofit;

    public enum Source {
        WordList,

        Collins
    }

    private RetrofitFactory() {
        //private constructor to hinder creation of objects
    }

    public static Retrofit build(Source source) {
        switch (source) {
            case WordList:
                retrofit = new WordListRetroFit();
                break;

            case Collins:
                retrofit = new CollinsRetroFit();
                break;

            default:
                retrofit = new CollinsRetroFit();
                break;
        }

        retrofit.onConfigure();
        return retrofit;
    }

    public static Retrofit getType(Source source) {
        switch (source) {
            case WordList:
                if(retrofit.instanceOf() == CollinsRetroFit.class)
                    return build(source);
                else
                    return retrofit;

            case Collins: default:
                if(retrofit.instanceOf() == WordListRetroFit.class)
                    return build(source);
                else
                    return retrofit;
        }
    }
}
