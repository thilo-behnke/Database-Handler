package files;

import database.Database;
import user.User;
import user.DatabaseController;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: Make FileReader generic
public class FileReader {

    public static class ReadInfo{
        String file;
        Database.Table table;

        public ReadInfo(String file, Database.Table table) {
            this.file = file;
            this.table = table;
        }
    }

    public static List<User> readUsersFromFile(ReadInfo...readInfos) {
        List<User> userList = new ArrayList<>();
        // TODO: Make file paths relative
        for(ReadInfo r : readInfos){
            userList.addAll(getUsersFromFile(Paths.get(r.file), r.table));
        }
        return userList;
    }

    private static List<User> getUsersFromFile(Path path, Database.Table table) {
        List<User> userList = new ArrayList<>();
        try (Stream<String> lines = Files.lines(path, Charset.defaultCharset())) {
            lines.skip(1)
                    .map(x -> new ArrayList<>(Arrays.asList(x.split(";"))))
                    .map(x -> x.stream().collect(Collectors.toMap(y -> DatabaseController.getColumnByIndex(table, x.indexOf(y)), Function.identity())))
                    .forEach(x -> userList.add(DatabaseController.createUserByAttributes(table, x)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return userList;
    }
}
