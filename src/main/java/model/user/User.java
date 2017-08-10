package model.user;

import model.Database;

// TODO: Add JDoc
public class User {

    int id;
    String name;
    Database.Table type;


    User() {
    }

    void setId(int id) {
        this.id = id;
    }

    void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Database.Table getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        return type == user.type;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ID: " + this.id
                + ", " + "Name: " + this.name;
    }

    public static class Employee extends User {

        int salary;
        String location;

        Employee(User user) {
            this.setId(user.id);
            this.setName(user.name);
            this.type = Database.Table.EMPLOYEES;
        }

        public int getSalary() {
            return salary;
        }

        public String getLocation() {
            return location;
        }

        @Override
        public boolean equals(Object o) {
            if (!super.equals(o)) return false;
            if (this == o) return true;
            if (getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            Employee employee = (Employee) o;

            if (salary != employee.salary) return false;
            return location != null ? location.equals(employee.location) : employee.location == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + salary;
            result = 31 * result + (location != null ? location.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return super.toString()
                    + ", " + "Rank: " + this.salary;
        }
    }

    public static class Customer extends User {

        String rank;

        Customer(User user) {
            this.setId(user.id);
            this.setName(user.name);
            this.type = Database.Table.CUSTOMERS;
        }

        public String getRank() {
            return rank;
        }

        @Override
        public boolean equals(Object o) {
            if (!super.equals(o)) return false;
            if (this == o) return true;
            if (getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            Customer customer = (Customer) o;

            return rank.equals(customer.rank);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (rank != null ? rank.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return super.toString()
                    + ", " + "Rank: " + this.rank;
        }
    }
}


