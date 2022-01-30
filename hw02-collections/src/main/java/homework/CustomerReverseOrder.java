package homework;


import java.util.*;

public class CustomerReverseOrder {
    private final Deque<Customer> internalQueue;

    public CustomerReverseOrder() {
        this.internalQueue = new ArrayDeque<>();
    }

    public void add(Customer customer) {
        internalQueue.push(customer);
    }

    public Customer take() {
        return internalQueue.pop();
    }
}
