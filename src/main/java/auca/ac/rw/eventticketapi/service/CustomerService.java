package auca.ac.rw.eventticketapi.service;

import auca.ac.rw.eventticketapi.exception.DuplicateResourceException;
import auca.ac.rw.eventticketapi.exception.ResourceNotFoundException;
import auca.ac.rw.eventticketapi.model.Customer;
import auca.ac.rw.eventticketapi.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    public Customer createCustomer(Customer customer) {
        // Business rule: no duplicate email addresses
        if (customerRepository.existsByEmailIgnoreCase(customer.getEmail())) {
            throw new DuplicateResourceException("A customer with this email already exists: " + customer.getEmail());
        }
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existing = getCustomerById(id);
        if (customerRepository.existsByEmailIgnoreCaseAndIdNot(updatedCustomer.getEmail(), id)) {
            throw new DuplicateResourceException("A customer with this email already exists: " + updatedCustomer.getEmail());
        }
        existing.setFullName(updatedCustomer.getFullName());
        existing.setEmail(updatedCustomer.getEmail());
        existing.setPhone(updatedCustomer.getPhone());
        return customerRepository.save(existing);
    }

    public void deleteCustomer(Long id) {
        Customer existing = getCustomerById(id);
        customerRepository.delete(existing);
    }
}
