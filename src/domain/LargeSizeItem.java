package domain;

// An item in size L or larger. It behaves like any item,
// but its unit price includes a fixed surcharge for the extra fabric.
public class LargeSizeItem extends Item{

    private static final long LARGE_SIZE_SURCHARGE = 5000;

    // Package-private: only Garment creates items.
    // Everything is stored by the parent class.
    LargeSizeItem(Garment garment, Size size, Color color){
        super(garment, size, color);
    }

    // The price of a regular item plus the large size surcharge.
    @Override
    public long getUnitPrice(){
        return super.getUnitPrice() + LARGE_SIZE_SURCHARGE;
    }
}
