package domain;

import java.util.ArrayList;

// Decision support component: classifies each item by comparing the units
// left with the units sold, so the employee knows what to restock.
// Rule based, no libraries. The level is never stored in the item:
// it is calculated on request, so it always reflects the current stock.
public class StockClassifier {

    private final ClothingStore store;

    // Public: the reports menu creates it to show stock recommendations.
    public StockClassifier(ClothingStore store){
        this.store = store;
    }

    // Rules, in this order:
    // - no units left                         -> OUT_OF_STOCK
    // - units left not above the units sold   -> RESTOCK
    // - otherwise                             -> ENOUGH
    // Units sold exclude annulled sales (ClothingStore.getUnitsSold).
    public StockLevel classify(Item item){
        if(item.getUnits() == 0){
            return StockLevel.OUT_OF_STOCK;
        }
        if(item.getUnits() <= store.getUnitsSold(item)){
            return StockLevel.RESTOCK;
        }
        return StockLevel.ENOUGH;
    }

    // Every item of the store with the given level, in registration order.
    // Empty when there are none, never null.
    public ArrayList<Item> getItemsByLevel(StockLevel level){
        ArrayList<Item> items = new ArrayList<>();
        for(Garment garment: store.getGarments()){
            for(Item item: garment.getItems()){
                if(level == classify(item)){
                    items.add(item);
                }
            }
        }
        return items;
    }
}
