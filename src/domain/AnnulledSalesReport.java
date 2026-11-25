package domain;

import java.util.ArrayList;

// Report with one row per annulled sale: its id, its total and the reason.
// Annulled sales are not stored apart: they are found by filtering all the
// sales with isAnnulled(). The last row adds up the annulled money.
public class AnnulledSalesReport extends Report{

    // Public: the reports menu creates it. The store is kept by Report.
    public AnnulledSalesReport(ClothingStore store){
        super(store);
    }

    @Override
    public String getTitle(){
        return "Ventas anuladas";
    }

    @Override
    public String getHeaders(){
        return "ID,Total,Motivo";
    }

    // The TOTAL row leaves the reason empty but keeps its column,
    // so every row has the same 3 columns in the CSV file.
    @Override
    public ArrayList<String> getRows(){
        ArrayList<String> rows = new ArrayList<>();
        long totalAnnulledIncome = 0;
        for(Sale sale: getStore().getSales()){
            if(!sale.isAnnulled()){
                continue;   // skips sales that are not annulled
            }
            long rowAnnulledIncome = sale.getTotal();
            String newRow = sale.getId()+","+ rowAnnulledIncome+","+sale.getAnnulmentReason();
            totalAnnulledIncome += rowAnnulledIncome;
            rows.add(newRow);
        }
        String finalRow = "TOTAL"+","+totalAnnulledIncome+",";
        rows.add(finalRow);
        return rows;
    }
}
