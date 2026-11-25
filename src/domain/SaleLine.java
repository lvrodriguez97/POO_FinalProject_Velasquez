package domain;

import java.io.Serializable;

// One line of a sale: an item, how many units were sold and the unit price
// at the moment of the sale. Quantity and price belong to the combination
// of an item with a sale, not to either of them alone.
// Serializable: saved to a file together with the whole store.
public class SaleLine implements Serializable {

    // Fixed version of the class for saved files: changing the class later
    // does not make previously saved files unreadable.
    private static final long serialVersionUID = 1L;

    private final Item item;
    private final int quantity;
    private final long unitPrice;

    // Public: the user interface builds the lines while the employee picks items.
    // It only validates its own quantity; ClothingStore checks the stock later.
    // The unit price is copied now, so later price changes never alter the sale.
    public SaleLine(Item item, int quantity){
        if(quantity <= 0){
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = item.getUnitPrice();
    }

    // --- Accessors ---

    public Item getItem(){
        return item;
    }

    public int getQuantity(){
        return quantity;
    }

    // The copied price, not the item's current price.
    public long getUnitPrice(){
        return unitPrice;
    }

    // Derived value: calculated on request, never stored.
    public long getSubtotal(){
        return quantity * getUnitPrice();
    }
}
