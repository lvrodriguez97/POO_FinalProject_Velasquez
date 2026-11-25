package domain;

import java.io.Serializable;
import java.util.ArrayList;

// The facade of the domain: the user interface goes through the store
// for every operation. The store creates and owns the garments and the sales,
// and enforces the rules that involve more than one object.
// Serializable: saved to a file together with the whole store.
public class ClothingStore implements Serializable {

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
        for(Sale sale: getSales()){
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

    // --- Sales ---

    // Returns a copy: callers can read the sales but not add or remove them.
    public ArrayList<Sale> getSales(){
        return new ArrayList<>(sales);
    }

    // Returns the sale with that id as soon as it finds it.
    // Throws an exception instead of returning null when it does not exist.
    public Sale findSaleById(int id){
        for(Sale sale: getSales()){
            if(sale.getId() == id){
                return sale;
            }
        }
        throw new IllegalArgumentException("No existe una venta con ese ID");
    }

    // Registers a sale built by the user interface and returns it.
    // First checks every line, then changes the stock: if any line fails,
    // nothing is discounted and no sale is created (all or nothing).
    public Sale registerSale(ArrayList<SaleLine> lines){
        ArrayList<Item> seen = new ArrayList<>();

        // Checks only: rejects repeated items (bad input) and lines
        // without enough units (the current stock cannot serve them).
        for(SaleLine line:lines){
            if(seen.contains(line.getItem())){
                throw new IllegalArgumentException("Operación fallida: "+line.getItem().getGarment().getName()+
                "\nTalla: "+line.getItem().getSize()+
                "\nColor: "+line.getItem().getColor()+ "\nEstá repetido en la venta");
            }
            if(!line.getItem().hasEnoughUnits(line.getQuantity())){
                throw new IllegalStateException("Operación fallida: "+line.getItem().getGarment().getName()+
                "\nTalla: "+line.getItem().getSize()+
                "\nColor: "+line.getItem().getColor()+ "\nNo tiene suficientes unidades en stock ("+line.getItem().getUnits()+")");
            }
            seen.add(line.getItem());
        }

        // Every line passed: discount the units and create the sale.
        // The sale is created before increasing the counter, so a failed
        // constructor never skips an id.
        for(SaleLine line: lines){
            line.getItem().removeUnits(line.getQuantity());
        }
        Sale newSale = new Sale(nextSaleId, lines);
        nextSaleId++;
        sales.add(newSale);
        return newSale;
    }

    // Annuls a sale and returns its units to the stock. The sale stays in
    // the list as a historical record; annulled sales are found by filtering
    // with isAnnulled(), so no separate list is needed.
    public void annulSale(int id, String reason){
        Sale annulmentAttemptSale;
        annulmentAttemptSale = findSaleById(id);
        annulmentAttemptSale.annul(reason);

        for(SaleLine line: annulmentAttemptSale.getLines()){
            line.getItem().addUnits(line.getQuantity());
        }
    }

    // --- Queries ---

    // Total units of this item sold in sales that are not annulled.
    // Unlike isItemSold, it skips annulled sales (their units went back
    // to the stock) and it never stops early: it must count every sale.
    // Used by BestSellersReport and StockClassifier.
    public int getUnitsSold(Item item){
        int unitsSold = 0;
        for(Sale sale : getSales()){
            if(!sale.isAnnulled()){
                for(SaleLine line: sale.getLines()){
                    if(line.getItem() == item){
                        unitsSold += line.getQuantity();
                    }
                }
            }
        }
        return unitsSold;
    }
}
