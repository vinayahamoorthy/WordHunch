package com.daksh.wordhunch.Rink;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daksh.wordhunch.R;

import java.util.ArrayList;
import java.util.List;

public class SuggestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
        if(lsItems != null)
            lsItems.add(strItem);
        else {
            lsItems = new ArrayList<>();
            lsItems.add(strItem);
        }

        SuggestionsAdapter.this.notifyItemInserted(lsItems.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_suggestion_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.txTitle.setText(lsItems.get(position));
    }

    @Override
    public int getItemCount() {
        if(lsItems != null)
            if(!lsItems.isEmpty())
                return lsItems.size();
            else
                return 0;
        else
            return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            txTitle = (TextView) itemView.findViewById(R.id.suggestionInput);
        }
    }
}
