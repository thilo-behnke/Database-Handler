package model;

import model.user.User;
import model.user.UserBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static model.Database.Types.*;

public class Database {

    public interface ITable {
        public User getTableEntity(List<String> attributes);
    }

    public enum Types {
        SERIAL, TEXT, INTEGER;
    }

    public enum Table implements ITable {
        USERS {
            @Override
            public User getTableEntity(List<String> attributes) {
               // TODO: throw exception?
                return null;
            }
        }, CUSTOMERS {
            @Override
            public User getTableEntity(List<String> attributes) {
                return new UserBuilder()
                        .setName(attributes.get(1))
                        .createCustomer()
                        .setRank(Integer.parseInt(attributes.get(2)))
                        .getCustomer();
            }
        }, EMPLOYEES {
            @Override
            public User getTableEntity(List<String> attributes) {
                return new UserBuilder()
                        .setName(attributes.get(1))
                        .createEmployee()
                        .setSalary(Integer.parseInt(attributes.get(2)))
                        .setLocation(attributes.get(3))
                        .getEmployee();
            }
        };

        public List<Columns> getColumns(Table table) {
            return Arrays.stream(Columns.values())
                    .filter(x -> x.table.equals(table))
                    .collect(Collectors.toList());
        }

        public enum Columns {
            U_ID(USERS, SERIAL), U_NAME(USERS, TEXT), U_TYPE(USERS, INTEGER),
            C_ID(CUSTOMERS, SERIAL), C_RANK(CUSTOMERS, INTEGER),
            E_ID(EMPLOYEES, SERIAL), E_SALARY(EMPLOYEES, INTEGER), E_LOCATION(EMPLOYEES, TEXT);

            private Table table;
            private Types type;

            Columns(Table table, Types type) {
                this.table = table;
                this.type = type;
            }

            public Types getType() {
                return type;
            }
        }

    }

}
