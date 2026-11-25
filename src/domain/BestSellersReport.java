package domain;

import java.util.ArrayList;

// Report with one row per item of every garment, from the most sold to the
// least sold. Items never sold also appear, with 0, so the employee can see
// what does not move. The last row adds up all the units sold.
public class BestSellersReport extends Report{

    // Public: the reports menu creates it. The store is kept by Report.
    public BestSellersReport(ClothingStore store){
        super(store);
    }

    @Override
    public String getTitle(){
        return "Artículos más vendidos";
    }

    @Override
    public String getHeaders(){
        return "Prenda,Talla,Color,Vendidos";
    }

    // Units sold come from ClothingStore.getUnitsSold, so annulled sales
    // are not counted. The items are sorted while they are still objects;
    // once turned into text their units could no longer be compared.
    @Override
    public ArrayList<String> getRows(){
        ArrayList<String> rows = new ArrayList<>();

        // 1. All the items of the store in a single list.
        ArrayList<Item> storeItems = new ArrayList<>();
        for(Garment garment: getStore().getGarments()){
            for(Item item: garment.getItems()){
                storeItems.add(item);
            }
        }

        // 2. Most sold first (b before a). sort is stable: ties keep
        // the order in which the items were registered.
        storeItems.sort((a,b) -> Integer.compare(getStore().getUnitsSold(b), getStore().getUnitsSold(a)));

        // 3. One row per item, then the TOTAL row with the same 4 columns.
        int totalSales = 0;
        for(Item item: storeItems){
            int rowTotalSales = getStore().getUnitsSold(item);
            String newRow = item.getGarment().getName()+","+item.getSize()+","+item.getColor()+","+ rowTotalSales;
            rows.add(newRow);
            totalSales += rowTotalSales;
        }
        String finalRow = "TOTAL"+","+","+","+totalSales;
        rows.add(finalRow);
        return rows;
    }
}
