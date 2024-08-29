package me.rtx4090.member.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.rtx4090.member.Member;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ConfigUtil {

    private static final Gson gsonStatic = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .create();

    private static final Gson gson = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
            .create();

    private static Path dir() {
        return Member.Instance.getDataFolder().toPath();
    }

    public static Path ofPath(String filename) {
        return dir().resolve(filename);
    }

    public static YamlConfiguration readOrDefault(String filename) {
        copyDefault(filename);
        return YamlConfiguration.loadConfiguration(ofPath(filename).toFile());
    }

    public static <T> T read(Path path, Class<T> clazz) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            Object tree = new Yaml().load(inputStream);
            return gson.fromJson(gson.toJsonTree(tree), clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T readStatic(Path path, Class<T> clazz) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            Object tree = new Yaml().load(inputStream);
            return gsonStatic.fromJson(gsonStatic.toJsonTree(tree), clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeStatic(Path path, Object obj) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            new Yaml().dump(gsonStatic.fromJson(gsonStatic.toJsonTree(obj), Map.class), writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(Path path, Object obj) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            new Yaml().dump(gson.fromJson(gson.toJsonTree(obj), Map.class), writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(YamlConfiguration config, String filename) {
        try {
            config.save(ofPath(filename).toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyDefault(String filename) {
        Path destination = ofPath(filename);
        if (Files.exists(destination)) return;
        try {
            Files.copy(Member.Instance.getResource(filename), destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static YamlConfiguration readDefault(String filename) {
        YamlConfiguration config = new YamlConfiguration();
        try (InputStreamReader reader = new InputStreamReader(Member.Instance.getResource(filename))) {
            config.load(reader);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return config;
    }
}
