package com.util;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlUtil {
    private static final Logger log = Logger.getLogger(YamlUtil.class);
    private static final String YAML_FILE_PATH = "application-dev.yml";
    private static Map<String, Object> yamlData;
    
    static {
        loadYamlData();
    }

    private static void loadYamlData() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = YamlUtil.class.getClassLoader().getResourceAsStream(YAML_FILE_PATH)) {
            yamlData = yaml.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getString(String key) {
        Object value = getProperty(key);
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    private static Object getProperty(String key) {
        String[] keys = key.split("\\.");
        Object value = yamlData;
        for (String k : keys) {
            if (value instanceof Map) {
                value = ((Map<?, ?>) value).get(k);
            } else {
                return null;
            }
        }
        return value;
    }
}
