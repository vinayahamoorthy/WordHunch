package com.daksh.wordhunch.Rink;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daksh.wordhunch.R;
import com.daksh.wordhunch.WordHunch;

import java.util.ArrayList;
import java.util.List;

public class SuggestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEWTYPE_EMPTY = 0;
    private final static int VIEWTYPE_WORDS = 1;

    /**
     * The list that holds the words to be displayed to the user on the screen
     */
    private List<String> lsItems;

    /**
     * A method to accept the list of words to be displayed to the user by default
     * @param lsItems
     */
    void setItems(@NonNull List<String> lsItems) {
        this.lsItems = lsItems;
    }

    /**
     * A method to add one word at a time. Used when the user taps enter after forming a word
     * @param strItem
     */
    void addItem(@NonNull String strItem) {
        //Check if the list is not null | If it is, instantiate, create a new list & add
        if(lsItems != null) {
            //Add the new item to the list only if it does not exist on the list.
            //If it does, just ignore || This is a secondary check as a contingency
            //The first level of check ensures any redundant entries are handled properly
            //with UI feedback
            if (!lsItems.contains(strItem))
                lsItems.add(strItem);
        } else {
            //Instantiate and add
            lsItems = new ArrayList<>();
            lsItems.add(strItem);
        }

        //Notify adapter a new item was added and the list needs to refresh
//        SuggestionsAdapter.this.notifyItemInserted(lsItems.size() - 1);
        SuggestionsAdapter.this.notifyDataSetChanged();
    }

    /**
     * Clears all items from the list
     */
    public void clearItems() {
        if(lsItems != null)
            lsItems.clear();

        SuggestionsAdapter.this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEWTYPE_EMPTY:
                View emptyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_suggestion_emptylist, parent, false);
                return new EmptyViewHolder(emptyView);

            case VIEWTYPE_WORDS:
                View wordView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_suggestion_listitem, parent, false);
                return new WordViewHolder(wordView);

            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(lsItems != null)
            if(!lsItems.isEmpty())
                return VIEWTYPE_WORDS;
            else
                return VIEWTYPE_EMPTY;
        else
            return VIEWTYPE_EMPTY;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder != null)
            if(holder instanceof WordViewHolder) {
                WordViewHolder viewHolder = (WordViewHolder) holder;
                viewHolder.txTitle.setText(lsItems.get(position));
            } else if(holder instanceof EmptyViewHolder) {
                EmptyViewHolder viewHolder = (EmptyViewHolder) holder;
                viewHolder.txTitle.setText(WordHunch.getContext().getString(R.string.WordList_Empty));
            }
    }

    @Override
    public int getItemCount() {
        if(lsItems != null)
            if(!lsItems.isEmpty())
                return lsItems.size();
            else
                return 1;
        else
            return 1;
    }

    private class WordViewHolder extends RecyclerView.ViewHolder {

        private TextView txTitle;

        WordViewHolder(View itemView) {
            super(itemView);
            txTitle = (TextView) itemView.findViewById(R.id.suggestionInput);
        }
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {

        private TextView txTitle;

        EmptyViewHolder(View itemView) {
            super(itemView);
            txTitle = (TextView) itemView.findViewById(R.id.emptylist_title);
        }
    }
}
