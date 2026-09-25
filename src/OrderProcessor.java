import java.util.ArrayList;
import java.util.List;

// Process orders and handle payments/inventory
public class OrderProcessor {
    public final String STANDARD = "STANDARD", SILVER = "SILVER", GOLD = "GOLD", VIP = "VIP";
    private List<CartItem> cart = new ArrayList<>();
    private List<String> appliedPromos = new ArrayList<>();

    //adds the quantity of a product to the cart if its valid
    public void addToCart(Product product, int qty) {
        if (qty > 0) {
            cart.add(new CartItem(product, qty));
        }
    }

    public List<CartItem> getCart() { return cart; }

    //adds a promo code to the system as long as the promo code hasn't already been used
    public void applyPromoCode(String code) {
        int countNotEqual = 0;
        for(int i=0; i<appliedPromos.size(); i++){
            if(!(appliedPromos.get(i).equals(code))){
                countNotEqual++;
            }
        }
            if(countNotEqual==appliedPromos.size()){
                appliedPromos.add(code);
            }
    }

    // Subtotal calculation
    public double calculateSubtotal() {
        double total = 0.0;
        for (CartItem item : cart) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    // Apply percentage discount based on customer tier
    public double calculateTierDiscount(double subtotal, Customer customer) {
        double discountRate = 0.0;
        if (customer.getTier() == SILVER) discountRate = 0.05;
        if (customer.getTier() == GOLD) discountRate = 0.10;
        if (customer.getTier() == VIP) discountRate = 0.20;

        return subtotal * discountRate;
    }

    // Calculate shipping fee based on total order weight/price. Orders over 100 get free standard shipping
    public double calculateShippingFee(double total, boolean isExpress) {
        if (total > 100.0) {
            return 0.0;
        }
        if(isExpress) {
            return 15.0;
        } else {
            return 5.0;
        }
    }







    // Calculate final order total including promo codes, taxes, and shipping. PromoCodes money off should result in the same exact amount of discount per tier regardless of other bonuses. (PromoCodes after discounts)
    public double calculateFinalTotal(Customer customer, boolean isExpress, double taxRate) {
        double total = calculateSubtotal();
        // Apply Promo Codes (Flat $10 off each)
        for (String promo : appliedPromos) {
            if (promo.equals("SAVE10")) {
                total -= 10.0;
            }
        }
        // Apply Tier Discount
        double tierDiscount = calculateTierDiscount(total, customer);
        total -= tierDiscount;
        // Apply Rewards
        if (customer.getRewardBalance() > 0) {
            if (customer.getRewardBalance() >= total) {
                customer.setRewardBalance(customer.getRewardBalance() - total);
                total = 0.0;
            } else {
                total -= customer.getRewardBalance();
                customer.setRewardBalance(0.0);
            }
        }
        // Add Shipping & Tax
        double shipping = calculateShippingFee(total, isExpress);
        double tax = total * taxRate;
        return total + shipping + tax;
    }





    // Decrement product stock after checkout only if there is enough items to complete the order
    public boolean reserveInventory() {
        for (CartItem item : cart) {
            int currentStock = item.getProduct().getStock();
            if (currentStock >= item.getQuantity()) {
                item.getProduct().setStock(currentStock - item.getQuantity());
            } else {
                return false; // Out of stock
            }
        }
        return true;
    }

    // Consolidated matching items into increased quantity instead of separate items
    public void consolidateCart() {
        for (int i = 0; i < cart.size(); i++) {
            for (int j = i + 1; j < cart.size(); j++) {
                if (cart.get(i).getProduct().getId().equals(cart.get(j).getProduct().getId())) {
                    cart.get(i).setQuantity(cart.get(i).getQuantity() + cart.get(j).getQuantity());
                    cart.remove(j);
                }
            }
        }
    }
}
