package ru.ibakaidov.distypepro.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import ru.ibakaidov.distypepro.R;
import ru.ibakaidov.distypepro.adapter.viewholder.WordViewHolder;
import ru.ibakaidov.distypepro.model.Word;

/**
 * @author Maksim Radko
 */
public class WordsListAdapter extends RecyclerView.Adapter<WordViewHolder> {

    private List<Word> words = Collections.emptyList();

    public void changeDataSet(@NonNull List<Word> entries) {
        words = entries;
        notifyDataSetChanged();
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.li_words, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        holder.bindView(words.get(position));
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    @NonNull
    public Word getItem(int position) {
        return words.get(position);
    }
}
