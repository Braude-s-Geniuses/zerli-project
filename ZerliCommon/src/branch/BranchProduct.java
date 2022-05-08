package branch;

import order.Product;

import java.io.Serializable;

public class BranchProduct implements Serializable {

    private Branch branch;
    private Product product;
    private int quantity;

    public BranchProduct(Branch branch, Product product, int quantity) {
        this.branch = branch;
        this.product = product;
        this.quantity = quantity;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "BranchProduct{" +
                "branch=" + branch +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
