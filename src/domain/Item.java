package domain;

// A size and color combination of a garment, with its own stock of units.
// Example: "Oversize Shirt, M, BLACK, 5 units".
public class Item {

    private final Garment garment;
    private final Size size;
    private final Color color;
    private int units;

    // Package-private: only Garment creates its items.
    // Every item starts with 0 units; units enter only through addUnits.
    Item(Garment garment, Size size, Color color){
        this.garment = garment;
        this.size = size;
        this.color = color;
        units = 0;
    }

    // --- Accessors ---

    public Garment getGarment(){
        return garment;
    }

    public Size getSize(){
        return size;
    }

    public Color getColor(){
        return color;
    }

    public int getUnits(){
        return units;
    }

    // --- Stock operations ---

    // The only way units enter the stock. Rejects zero or negative quantities.
    protected void addUnits(int quantity){
        if(quantity <= 0){
            throw new IllegalArgumentException("No es un valor válido para agregar, ingrese un número mayor que cero");
        }
        this.units = getUnits() + quantity;
    }

    // Step 1 of a sale: answers whether there are at least the requested units.
    protected boolean hasEnoughUnits(int quantity){
        return getUnits() >= quantity;
    }

    // Step 2 of a sale: the only way units leave the stock.
    // Rejects the operation if there are not enough units.
    protected void removeUnits(int quantity){
        if(!hasEnoughUnits(quantity)){
            throw new IllegalStateException("No hay unidades suficientes");
        }
        this.units = getUnits() - quantity;
    }

    // --- Price ---

    // A regular item costs its garment's base price.
    // LargeSizeItem overrides this method to add the surcharge.
    public long getUnitPrice(){
        return getGarment().getBasePrice();
    }
}
