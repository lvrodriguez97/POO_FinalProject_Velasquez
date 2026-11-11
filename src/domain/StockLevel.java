package domain;

// Result of the stock classification made by StockClassifier.
// OUT_OF_STOCK: no units left.
// RESTOCK: fewer units left than units sold.
// ENOUGH: at least as many units left as units sold.
public enum StockLevel {
    OUT_OF_STOCK, RESTOCK, ENOUGH
}
