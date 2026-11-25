package domain;

import java.util.ArrayList;

// Base class of every report. It keeps the store the data comes from and
// defines the contract each report must fulfill: a title, the column headers
// and the rows. Headers and rows are comma separated, so the same text can be
// shown on the console and written to a CSV file.
// It is abstract because a generic report would have no title or rows to give.
public abstract class Report {

    private final ClothingStore store;

    // Protected: only subclasses call it, through super(store).
    protected Report(ClothingStore store){
        this.store = store;
    }

    // Protected: subclasses use it to ask the store for data.
    protected ClothingStore getStore(){
        return store;
    }

    // --- Contract: each report implements these ---

    // The name of the report, shown above the table.
    public abstract String getTitle();

    // The column names, separated by commas.
    public abstract String getHeaders();

    // One text per row, with its values separated by commas.
    public abstract ArrayList<String> getRows();
}
