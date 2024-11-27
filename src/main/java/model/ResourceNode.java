package model;

public class ResourceNode {
    private final String type;
    private final double productionCost;
    private final int output;

    public ResourceNode(String type, double productionCost, int output) {
        this.type = type;
        this.productionCost = productionCost;
        this.output = output;
    }

    public String getType() {
        return type;
    }

    public double getProductionCost() {
        return productionCost;
    }

    public int getOutput() {
        return output;
    }

    public void produce(double budget) {
        if (budget >= productionCost) {
            // Logic to produce resources based on the allocated budget
            // For example, increase the output based on the budget
        }
    }
}