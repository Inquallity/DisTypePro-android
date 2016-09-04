package ru.ibakaidov.distypepro.model;

/**
 * @author Maksim Radko
 */
public class Word {

    private String wordString;

    private long categoryId;

    public String getWordString() {
        return wordString;
    }

    public void setWordString(String wordString) {
        this.wordString = wordString;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
