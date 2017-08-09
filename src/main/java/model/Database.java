package model;

import model.user.User;
import model.user.UserBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static model.Database.Types.*;

public class Database {

    public interface ITable {
        User getTableEntity(List<String> attributes);
        String getTableKey();
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

            @Override
            public String getTableKey() {
                return Column.U_ID.name();
            }
        }, CUSTOMERS {
            @Override
            public User getTableEntity(List<String> attributes) {
                return new UserBuilder()
                        .setId(Integer.parseInt(attributes.get(0)))
                        .setName(attributes.get(1))
                        .createCustomer()
                        .setRank(Integer.parseInt(attributes.get(2)))
                        .getCustomer();
            }

            @Override
            public String getTableKey() {
                return Column.C_ID.name();
            }
        }, EMPLOYEES {
            @Override
            public User getTableEntity(List<String> attributes) {
                return new UserBuilder()
                        .setId(Integer.parseInt(attributes.get(0)))
                        .setName(attributes.get(1))
                        .createEmployee()
                        .setSalary(Integer.parseInt(attributes.get(2)))
                        .setLocation(attributes.get(3))
                        .getEmployee();
            }

            @Override
            public String getTableKey() {
                return Column.E_ID.name();
            }
        };

        public static List<Column> getColumns(Table table) {
            return Arrays.stream(Column.values())
                    .filter(x -> x.table.equals(table))
                    .collect(Collectors.toList());
        }

        public interface IColumn {
            Object getAttribute(User user);
        }

        public enum Column implements IColumn {
            U_ID(USERS, SERIAL) {
                @Override
                public Object getAttribute(User user) {
                    return user.getId();
                }
            }, U_NAME(USERS, TEXT) {
                @Override
                public Object getAttribute(User user) {
                    return user.getName();
                }
            }, U_TYPE(USERS, INTEGER) {
                @Override
                public Object getAttribute(User user) {
                    return user.getType().ordinal();
                }
            },
            C_ID(CUSTOMERS, SERIAL) {
                @Override
                public Object getAttribute(User user) {
                    return user.getId();
                }
            }, C_RANK(CUSTOMERS, INTEGER) {
                @Override
                public Object getAttribute(User user) {
                    return ((User.Customer)user).getRank();
                }
            },
            E_ID(EMPLOYEES, SERIAL) {
                @Override
                public Object getAttribute(User user) {
                    return user.getId();
                }
            }, E_SALARY(EMPLOYEES, INTEGER) {
                @Override
                public Object getAttribute(User user) {
                    return ((User.Employee)user).getSalary();
                }
            }, E_LOCATION(EMPLOYEES, TEXT) {
                @Override
                public Object getAttribute(User user) {
                    return ((User.Employee)user).getLocation();
                }
            };

            private Table table;
            private Types type;

            Column(Table table, Types type) {
                this.table = table;
                this.type = type;
            }

            public Types getType() {
                return type;
            }
        }

    }

}
