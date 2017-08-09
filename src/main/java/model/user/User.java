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

    // TODO: Check if method is still relevant
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
    public String toString() {
        return "ID: " + this.id
                + ", " + "Name: " + this.name;
    }

    public enum UserType {
        Employee(1), Customer(2);

        public int id;

        UserType(int id) {
            this.id = id;
        }
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
        public String getLocation() { return location; }

        @Override
        public String toString() {
            return super.toString()
                    + ", " + "Rank: " + this.salary;
        }
    }

    public static class Customer extends User {

        int rank;

        Customer(User user) {
            this.setId(user.id);
            this.setName(user.name);
            this.type = Database.Table.CUSTOMERS;
        }

        public int getRank() {
            return rank;
        }

        @Override
        public String toString() {
            return super.toString()
                    + ", " + "Rank: " + this.rank;
        }
    }
}


