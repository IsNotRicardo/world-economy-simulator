package model.core;

public class ResourceInfo {
    private int quantity;
    private double value;
    private final int[] supplyArchive;
    private int currentSize = 0;

    public ResourceInfo(int quantity, double value, int archiveTime) {
        this.quantity = quantity;
        this.value = value;
        this.supplyArchive = new int[archiveTime];
    }

    // Start of Getters
    public int getQuantity() {
        return quantity;
    }

    public double getValue() {
        return value;
    }

    public int[] getSupplyArchive() {
        return supplyArchive;
    }

    public int getCurrentSize() {
        return currentSize;
    }
    // End of Getters

    public double getValuePerUnit() {
        return value / quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void addValue(double value) {
        this.value += value;
    }

    public void subtractQuantityAndValue(int quantity) {
        int initialQuantity = this.quantity;
        double valueDifference = this.value / initialQuantity * quantity;

        this.quantity -= quantity;
        this.value -= valueDifference;
    }

    public void archiveSupply() {
        if (currentSize < supplyArchive.length) {
            currentSize++;
        }
        for (int i = currentSize - 1; i > 0; i--) {
            supplyArchive[i] = supplyArchive[i - 1];
        }
        supplyArchive[0] = this.getQuantity();
    }
}