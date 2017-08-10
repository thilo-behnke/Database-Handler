package user;

// TODO: Add JDoc
public class UserBuilder {

    private User user;

    public UserBuilder() {
        user = new User();
    }

    // TODO: Check if method is still relevant
    public UserBuilder setId(int id) {
        user.id = id;
        return this;
    }

    // TODO: Check privacy levels
    public UserBuilder setName(String name) {
        user.name = name;
        return this;
    }

    public CustomerBuilder createCustomer() {
        return new CustomerBuilder(new User.Customer(user));
    }

    public EmployeeBuilder createEmployee() {
        return new EmployeeBuilder(new User.Employee(user));
    }

    public class EmployeeBuilder extends UserBuilder {

        private User.Employee employee;

        EmployeeBuilder(User.Employee employee) {
            this.employee = employee;
        }

        public EmployeeBuilder setSalary(int salary) {
            employee.salary = salary;
            return this;
        }

        public EmployeeBuilder setLocation(String location) {
            employee.location = location;
            return this;
        }

        public User.Employee getEmployee() {
            return this.employee;
        }

    }

    public class CustomerBuilder extends UserBuilder {

        private User.Customer customer;

        CustomerBuilder(User.Customer customer) {
            this.customer = customer;
        }

        public CustomerBuilder setRank(String rank) {
            customer.rank = rank;
            return this;
        }

        public User.Customer getCustomer() {
            return this.customer;
        }

    }



}
