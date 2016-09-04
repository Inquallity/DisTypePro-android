package ru.ibakaidov.distypepro.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.ibakaidov.distypepro.model.Category;

/**
 * @author Maksim Radko
 */
public class CategoriesViewHolder extends RecyclerView.ViewHolder {

    public CategoriesViewHolder(View itemView) {
        super(itemView);
    }

    public void bindView(Category category) {
        final TextView tv = (TextView) itemView;
        tv.setText(category.getCategoryName());
    }
}
