package domain;

import java.util.ArrayList;

// The facade of the domain: the user interface goes through the store
// for every operation. The store creates and owns the garments and the sales,
// and enforces the rules that involve more than one object.
public class ClothingStore {

    private final ArrayList<Garment> garments;
    private final ArrayList<Sale> sales;
    private int nextSaleId;

    // Public: Main creates the store. It starts empty; the first sale is number 1.
    public ClothingStore(){
        garments = new ArrayList<>();
        sales = new ArrayList<>();
        nextSaleId = 1;
    }

    // --- Garments ---

    // Creates a garment and stores it. The Garment constructor validates
    // the name and the price. Returns the garment so the user interface
    // can keep working with it.
    public Garment registerGarment(String name, Category category, long basePrice){
        Garment garment = new Garment(name, category, basePrice);
        garments.add(garment);
        return garment;
    }

    // Returns a copy: callers can read the garments but not add or remove them.
    public ArrayList<Garment> getGarments(){
        return new ArrayList<>(garments);
    }

    // Returns a new list with the garments of the given category.
    // Empty when there are none, never null.
    public ArrayList<Garment> findGarmentsByCategory(Category category){
        ArrayList<Garment> result = new ArrayList<>();
        for(Garment garment: getGarments()){
            if(garment.getCategory() == category){
                result.add(garment);
            }
        }
        return result;
    }

    // The following methods delegate: each rule lives in the object that owns
    // the data, so the store does not repeat it. Exceptions reach the menus.

    // Garment.setBasePrice rejects prices that are not greater than zero.
    public void updateBasePrice(Garment garment, long newPrice){
        garment.setBasePrice(newPrice);
    }

    // --- Items ---

    // Garment.addItem rejects repeated combinations and decides whether
    // the new item is a regular Item or a LargeSizeItem.
    // Returns the item so the user interface can add units to it right away.
    public Item addItemToGarment(Garment garment, Size size, Color color){
        Item newItem = garment.addItem(size, color);
        return newItem;
    }

    // Item.addUnits rejects zero or negative quantities.
    // The item is enough: it already knows its garment and keeps its own units.
    public void addUnits(Item item, int quantity){
        item.addUnits(quantity);
    }

    // --- Removal ---
    // An item or a garment can be removed only if no sale references it,
    // annulled sales included, so no sale line is left pointing to nothing.

    // Helper for the removal methods: answers whether any sale has a line
    // with this exact item (== compares the same object in memory).
    // Returns as soon as it finds one.
    private boolean isItemSold(Item item){
        for(Sale sale: sales){
            for(SaleLine line: sale.getLines()){
                if(item == line.getItem()){
                    return true;
                }
            }
        }
        return false;
    }

    public void removeItem(Garment garment, Item item){
        if(isItemSold(item)){
            throw new IllegalStateException("El ítem no puede borrarse pues tiene una venta registrada");
        }
        garment.removeItem(item);
    }

    // Checks every item first: if any of them was sold, nothing is removed.
    // Uses the real list, not getGarments(), because a copy would not change the store.
    public void removeGarment(Garment garment){
        for(Item item: garment.getItems()){
            if(isItemSold(item)){
                throw new IllegalStateException("Operación fallida: "+garment.getName()+" tiene ventas registradas");
            }
        }
        garments.remove(garment);
    }
}
