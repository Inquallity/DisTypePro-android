package ru.ibakaidov.distypepro.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.ibakaidov.distypepro.model.Word;

/**
 * @author Maksim Radko
 */
public class WordViewHolder extends RecyclerView.ViewHolder {

    public WordViewHolder(View itemView) {
        super(itemView);
    }

    public void bindView(Word word) {
        final TextView tv = (TextView) itemView;
        tv.setText(word.getWordString());
    }
}
