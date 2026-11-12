package domain;

import java.util.ArrayList;

// A garment model, for example "Oversize Shirt". It has a name, a category
// and a base price, and owns its items (one per size and color combination).
public class Garment {

    private final String name;
    private final Category category;
    private long basePrice; 
    private final ArrayList<Item> items; 

    // Package-private: only ClothingStore creates garments.
    // Every garment starts with no items.
    Garment(String name, Category category, long basePrice){
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("La prenda debe crearse con un nombre");
        }
        this.name = name;
        setBasePrice(basePrice);
        this.category = category;
        this.items = new ArrayList<>();
    }

    // --- Accessors ---

    public String getName(){
        return name;
    }

    public Category getCategory(){
        return category;
    }

    public long getBasePrice(){
        return basePrice;
    }

    // Returns a copy: callers can read the items and pick one,
    // but they cannot add or remove items without going through addItem.
    public ArrayList<Item> getItems(){
        return new ArrayList<>(items);
    }

    // --- Mutator ---

    // The only modifiable attribute. Also used by the constructor,
    // so the "price greater than zero" rule lives in one place.
    protected void setBasePrice(long newPrice){
        if(newPrice <= 0){
            throw new IllegalArgumentException("El precio debe ser mayor que cero");
        }
        this.basePrice = newPrice;
    }

    // --- Items ---

    // Creates the item for a size and color combination and stores it.
    // Rejects repeated combinations. Sizes from L upward create a LargeSizeItem.
    protected Item addItem(Size size, Color color){
        for(Item item : items){
            if(item.getSize() == size && item.getColor() == color){
                throw new IllegalArgumentException("La combinación " + size + " · " + color
                        + " ya existe en " + name + ". Use la opción 'Agregar unidades'.");
            }
        }
        Item newItem;
        if(size.compareTo(Size.L) >= 0){
            newItem = new LargeSizeItem(this, size, color);
        }else{
            newItem = new Item(this, size, color);
        }
        items.add(newItem);
        return newItem;
    }

    // Removes an item from this garment.
    // ClothingStore checks beforehand that no sale references it.
    protected void removeItem(Item item){
        items.remove(item);
    }
}
