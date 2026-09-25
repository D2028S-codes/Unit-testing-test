import java.util.ArrayList;
import java.util.List;

// Customer profile
public class Customer {
    private String name;
    private String tier;
    private double rewardBalance;

    public Customer(String name, String tier, double rewardBalance) {
        this.name = name;
        this.tier = tier;
        this.rewardBalance = rewardBalance;
    }

    public String getTier() { return tier; }
    public double getRewardBalance() { return rewardBalance; }
    public void setRewardBalance(double balance) { this.rewardBalance = balance; }
}



