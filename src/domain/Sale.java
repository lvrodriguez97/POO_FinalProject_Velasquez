package domain;

import java.io.Serializable;
import java.util.ArrayList;

// A completed sale: an id and one or more sale lines.
// Sales are never deleted; they can only be annulled, keeping the record.
// Serializable: saved to a file together with the whole store.
public class Sale implements Serializable {

    private final int id;
    private final ArrayList<SaleLine> lines;
    private boolean annulled;
    private String annulmentReason;

    // Package-private: only ClothingStore creates sales, after checking the stock.
    // A sale is born complete, with at least one line. It keeps its own copy
    // of the list, so changes to the caller's list never alter a registered sale.
    Sale(int id, ArrayList<SaleLine> lines){
        if(lines.isEmpty()){
            throw new IllegalArgumentException("No hay productos registrados para la venta");
        }
        this.lines = new ArrayList<>(lines);
        this.id = id;
        annulled = false;
        annulmentReason = null;
    }

    // --- Accessors ---

    public int getId(){
        return id;
    }

    // Returns a copy: callers can read the lines but cannot change the sale.
    public ArrayList<SaleLine> getLines(){
        return new ArrayList<>(lines);
    }

    public boolean isAnnulled(){
        return annulled;
    }

    // null while the sale is not annulled.
    public String getAnnulmentReason(){
        return annulmentReason;
    }

    // Derived value: the sum of the line subtotals, calculated on request.
    public long getTotal(){
        long total = 0;
        for(SaleLine line : getLines()){
            total += line.getSubtotal();
        }
        return total;
    }

    // --- Annulment ---

    // Protected: only ClothingStore annuls sales, because it must also
    // return the units to the stock. A sale can be annulled only once.
    // The reason cannot contain commas because it is exported to CSV.
    protected void annul(String reason){
        if(isAnnulled()){
            throw new IllegalStateException("La venta ya ha sido anulada");
        }
        if(reason == null || reason.isBlank() || reason.contains(",")){
            throw new IllegalArgumentException("La razón no puede estar vacía ni incluir comas. Por favor, edítela.");
        }
        annulled = true;
        annulmentReason = reason;
    }
}
