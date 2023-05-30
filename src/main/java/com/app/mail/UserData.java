package com.app.mail;

import com.app.gui.Mail;

import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserData {
    public static Mail getUser(String userName) {
        Properties props = new Properties();
        Mail user;
        try {
            props.load(new FileReader(Paths.get("").toRealPath() +
                    "/src/main/resources/props/" + userName + ".properties"));
            user = new Mail(props);
        } catch (IOException e) {
            System.out.println("Can't get user");
            throw new RuntimeException(e);
        }
        return user;
    }

    public static void saveUser(Mail user) {
        try (PrintWriter writer = new PrintWriter(new FileWriter((Paths.get("").toRealPath() +
                "/src/main/resources/props/" + user + ".properties")))) {
            for (Object props : user.getSession().getProperties().keySet()) {
                writer.write(props + "=" + user.getSession().getProperties().getProperty((String) props) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Couldn't save user");
            throw new RuntimeException(e);
        }
    }

    public static Mail[] getAllUsers() {
        Mail[] allUsers;
        List<Path> listOfUsers = getPathsOfUsers();
        allUsers = new Mail[listOfUsers.size() - 1];
        for (int i = 0; i < listOfUsers.size(); i++) {
            String file = "";
            String toWork = listOfUsers.get(i).toString();
            for (int j = toWork.length() - 1; j >= 0; j--) {
                if (toWork.charAt(j) == '\\') break;
                file = toWork.charAt(j) + file;
            }
            if (file.equals("basicConfigs.properties")) continue;
            allUsers[i - 1] = getUser(file.replace(".properties", ""));
        }
        return allUsers;
    }

    public static List<Path> getPathsOfUsers() {
        try {
            return Files.list(Paths
                    .get(Paths.get("").toRealPath() + "/src/main/resources/props")).toList();
        } catch (IOException e) {
            System.out.println("Can't get paths of users");
            throw new RuntimeException(e);
        }
    }

    public static List<String> getCachedUsers() {
        List<String> cachedUsers = new ArrayList<>();
        try (BufferedReader file = new BufferedReader(
                new FileReader(Paths.get("").toRealPath() + "/src/main/resources/configs/users.txt"))) {
            while (true) {
                String user = file.readLine();
                if (user == null) break;
                cachedUsers.add(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cachedUsers;
    }

    public static void setCachedUsers(List<String> users) {
        try (FileWriter cachedUser = new FileWriter(
                Paths.get("").toRealPath() + "/src/main/resources/configs/users.txt")) {
            for (String user : users) {
                cachedUser.write(user + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decode(String str) {
        if (str == null) return "";
        byte[] bytes;
        try {
            if (str.startsWith("=?")) bytes = MimeUtility.decodeWord(str).getBytes(StandardCharsets.UTF_8);
            else bytes = str.getBytes(StandardCharsets.UTF_8);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (ParseException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
