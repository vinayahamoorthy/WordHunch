package com.daksh.wordhunch.Network.WordList;

import okhttp3.Response;

public class WordListEvent {

    private Response response;

    public WordListEvent(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
