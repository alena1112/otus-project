package homework;


import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {
    private final TreeMap<Customer, String> internalMap;

    public CustomerService() {
        this.internalMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));
    }

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = internalMap.firstEntry();
        return entry != null ? new CopyEntry(entry) : null;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = internalMap.higherEntry(customer);
        return entry != null ? new CopyEntry(entry) : null;
    }

    public void add(Customer customer, String data) {
        internalMap.put(customer, data);
    }

    private static class CopyEntry implements Map.Entry<Customer, String> {
        private final Map.Entry<Customer, String> sourceEntry;

        public CopyEntry(Map.Entry<Customer, String> sourceEntry) {
            this.sourceEntry = sourceEntry;
        }

        @Override
        public Customer getKey() {
            return sourceEntry.getKey().clone();
        }

        @Override
        public String getValue() {
            return sourceEntry.getValue();
        }

        @Override
        public String setValue(String value) {
            throw new UnsupportedOperationException();
        }
    }
}
