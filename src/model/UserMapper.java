package model;

import database.DatabaseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserMapper {

    private static DatabaseEntity userMapping;
    private static DatabaseEntity employeeMapping;
    private static DatabaseEntity customerMapping;

    public static DatabaseEntity getUserMapping(User user) {
        if (userMapping == null) {
            userMapping = new DatabaseEntity();
            userMapping.setTable("users");
            userMapping.setColumns(
                    new ArrayList<>(
                            Arrays.asList(
                                    new DatabaseEntity.Column("id", DatabaseEntity.Column.Types.SERIAL),
                                    new DatabaseEntity.Column("type", DatabaseEntity.Column.Types.INTEGER),
                                    new DatabaseEntity.Column("name", DatabaseEntity.Column.Types.TEXT)
                            )
                    )
            );
        }
        return userMapping;
    }

    public static DatabaseEntity getUserMapping(User.Employee employee) {
        if (employeeMapping == null) {
            employeeMapping = new DatabaseEntity();
            employeeMapping.setTable("employees");
            employeeMapping.setColumns(
                    new ArrayList<>(
                            Arrays.asList(
                                    new DatabaseEntity.Column("id", DatabaseEntity.Column.Types.SERIAL),
                                    new DatabaseEntity.Column("salary", DatabaseEntity.Column.Types.INTEGER),
                                    new DatabaseEntity.Column("location", DatabaseEntity.Column.Types.TEXT)
                            )
                    )
            );
            employeeMapping.setAttributes(
                    new ArrayList<>(
                            Arrays.asList(
                                    "id",
                                    "name",
                                    "salary",
                                    "location"
                            )
                    )
            );
        }
        return employeeMapping;
    }

    public static DatabaseEntity getUserMapping(User.Customer customer) {
        if (customerMapping == null) {
            customerMapping = new DatabaseEntity();
            customerMapping.setTable("customers");
            customerMapping.setColumns(
                    new ArrayList<>(
                            Arrays.asList(
                                    new DatabaseEntity.Column("id", DatabaseEntity.Column.Types.SERIAL),
                                    new DatabaseEntity.Column("rank", DatabaseEntity.Column.Types.INTEGER)
                            )
                    )
            );
            customerMapping.setAttributes(
                    new ArrayList<>(
                            Arrays.asList(
                                    "id",
                                    "name",
                                    "rank"
                            )
                    )
            );
        }
        return customerMapping;
    }

    public static DatabaseEntity getUserTableStructure() {
        return getUserMapping(new User());
    }

    public static DatabaseEntity getCustomerTableStructure() {
        return getUserMapping(new User.Customer(new User()));
    }

    public static DatabaseEntity getEmployeeTableStructure() {
        return getUserMapping(new User.Employee(new User()));
    }

    private static User createUserByAttributes(User.Employee employee, List<String> attributes) {
        DatabaseEntity entity = UserMapper.getUserMapping(employee);
        if (attributes.size() == entity.getAttributes().size()) {
            UserBuilder userBuilder = new UserBuilder();
            return userBuilder
                    .setName(attributes.get(1))
                    .createEmployee()
                    .setSalary(Integer.parseInt(attributes.get(2)))
                    .setLocation(attributes.get(3))
                    .getEmployee();
        } else {
            throw new IllegalArgumentException("List has wrong format: " + attributes.size() + " Columns");
        }
    }

    private static User createUserByAttributes(User.Customer customer, List<String> attributes) {
        DatabaseEntity entity = UserMapper.getUserMapping(customer);
        if (attributes.size() == entity.getAttributes().size()) {
            UserBuilder userBuilder = new UserBuilder();
            return userBuilder
                    .setName(attributes.get(1))
                    .createCustomer()
                    .setRank(Integer.parseInt(attributes.get(2)))
                    .getCustomer();
        } else {
            throw new IllegalArgumentException("List has wrong format: " + attributes.size() + " Columns");
        }
    }

    public static User.Employee createEmployeeByAttributes(List<String> attributes) {
        return (User.Employee) createUserByAttributes(new User.Employee(new User()), attributes);
    }

    public static User.Customer createCustomerByAttributes(List<String> attributes) {
        return (User.Customer) createUserByAttributes(new User.Customer(new User()), attributes);
    }
}
