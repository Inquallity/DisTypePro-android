package ru.ibakaidov.distypepro.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import ru.ibakaidov.distypepro.R;
import ru.ibakaidov.distypepro.adapter.viewholder.CategoriesViewHolder;
import ru.ibakaidov.distypepro.model.Category;

/**
 * @author Maksim Radko
 */
public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesViewHolder> {

    private List<Category> categories = Collections.emptyList();

    public void changeDataSet(@NonNull List<Category> entries) {
        categories = entries;
        notifyDataSetChanged();
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.li_categories, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoriesViewHolder holder, int position) {
        holder.bindView(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @NonNull
    public Category getItem(int position) {
        return categories.get(position);
    }
}
