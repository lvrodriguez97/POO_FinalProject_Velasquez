package domain;

import java.util.ArrayList;

// Report with one row per sale that is not annulled: its id, how many lines
// it has, how many units were sold and its total. The last row adds them up.
public class SalesSummaryReport extends Report {

    // Public: the reports menu creates it. The store is kept by Report.
    public SalesSummaryReport(ClothingStore store){
        super(store);
    }

    @Override
    public String getTitle(){
        return "Resumen de ventas";
    }

    @Override
    public String getHeaders(){
        return "ID,lineas,unidades,total";
    }

    // Annulled sales are skipped: their units went back to the stock,
    // so they are not real income. They have their own report.
    // The TOTAL row is always present, with zeros when there are no sales.
    @Override
    public ArrayList<String> getRows(){
        ArrayList<String> rows = new ArrayList<>();
        int totalLines = 0;
        int totalUnits = 0;
        long totalIncome = 0;

        for(Sale sale: getStore().getSales()){
            if(!sale.isAnnulled()){
                String newRow = null;
                // Values of this sale; they start at zero for every sale.
                int rowUnits = 0;
                int rowLines = 0;
                long rowTotalIncome = sale.getTotal();
                for(SaleLine line: sale.getLines()){
                    rowUnits += line.getQuantity();
                    rowLines ++;
                }
                newRow = ""+sale.getId()+","+rowLines+","+rowUnits+","+rowTotalIncome;
                totalLines += rowLines;
                totalUnits += rowUnits;
                totalIncome += rowTotalIncome;
                rows.add(newRow);
            }
        }
        String finalRow = "TOTAL"+","+totalLines+","+totalUnits+","+totalIncome;
        rows.add(finalRow);
        return rows;
    }
}
