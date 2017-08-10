package database;

import user.User;
import user.UserBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static database.Database.Types.*;

/**
 * Class that represents the structure of the database.
 */
public class Database {

    /**
     * Interface implemented by Table enum.
     * Allows retrieval of table entity and table key.
     */
    private interface ITable {

        /**
         * Receive an object instance of the calling table.
         * @param attributes parameters for instantiating the object.
         * @return created object.
         */
        User getEntity(List<String> attributes);

        /**
         * Return primary table key.
         * @return primary key.
         */
        Table.Column getTableKey();
    }

    public enum Types {
        SERIAL, TEXT, INTEGER;
    }

    public enum Table implements ITable {

        USERS(null) {
            @Override
            public User getEntity(List<String> attributes) {
                // TODO: throw exception?
                return null;
            }

            @Override
            public Column getTableKey() {
                return Column.U_ID;
            }
        }, CUSTOMERS(USERS) {
            @Override
            public User getEntity(List<String> attributes) {
                return new UserBuilder()
                        .setId(Integer.parseInt(attributes.get(0)))
                        .setName(attributes.get(1))
                        .createCustomer()
                        .setRank(attributes.get(2))
                        .getCustomer();
            }

            @Override
            public Column getTableKey() {
                return Column.C_ID;
            }
        }, EMPLOYEES(USERS) {
            @Override
            public User getEntity(List<String> attributes) {
                return new UserBuilder()
                        .setId(Integer.parseInt(attributes.get(0)))
                        .setName(attributes.get(1))
                        .createEmployee()
                        .setSalary(Integer.parseInt(attributes.get(2)))
                        .setLocation(attributes.get(3))
                        .getEmployee();
            }

            @Override
            public Column getTableKey() {
                return Column.E_ID;
            }
        };

        private Table inheritsFrom;

        Table(Table inheritsFrom) {
            this.inheritsFrom = inheritsFrom;
        }

        public static List<Column> getColumns(Table table, boolean inherited) {
            return Arrays.stream(Column.values())
                    .filter(x -> inherited ? x.table.equals(table.inheritsFrom) || x.table.equals(table) : x.table.equals(table))
                    .collect(Collectors.toList());
        }

        public Table getInheritsFrom() {
            return inheritsFrom;
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
            },
            U_NAME(USERS, TEXT) {
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
            }, C_RANK(CUSTOMERS, TEXT) {
                @Override
                public Object getAttribute(User user) {
                    return ((User.Customer) user).getRank();
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
                    return ((User.Employee) user).getSalary();
                }
            }, E_LOCATION(EMPLOYEES, TEXT) {
                @Override
                public Object getAttribute(User user) {
                    return ((User.Employee) user).getLocation();
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
