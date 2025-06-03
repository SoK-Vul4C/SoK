package io.swagger.parser.util;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.util.Json;
import io.swagger.util.Yaml;

import java.io.IOException;

/**
 * Created by russellb337 on 7/14/15.
 */
public class DeserializationUtils {
    public static JsonNode deserializeIntoTree(String contents, String fileOrHost) {
        JsonNode result;

        try {
            if (isJson(contents)) {
                result = Json.mapper().readTree(contents);
            } else {
                result = readYamlTree(contents);
            }
        } catch (IOException e) {
            throw new RuntimeException("An exception was thrown while trying to deserialize the contents of " + fileOrHost + " into a JsonNode tree", e);
        }

        return result;
    }

    public static <T> T deserialize(Object contents, String fileOrHost, Class<T> expectedType) {
        T result;

        boolean isJson = false;

        if(contents instanceof String && isJson((String)contents)) {
            isJson = true;
        }

        try {
            if (contents instanceof String) {
                if (isJson) {
                    result = Json.mapper().readValue((String) contents, expectedType);
                } else {
                    result = Yaml.mapper().readValue((String) contents, expectedType);
                }
            } else {
                result = Json.mapper().convertValue(contents, expectedType);
            }
        } catch (Exception e) {
            throw new RuntimeException("An exception was thrown while trying to deserialize the contents of " + fileOrHost + " into type " + expectedType, e);
        }

        return result;
    }

    private static boolean isJson(String contents) {
        return contents.toString().trim().startsWith("{");
    }

 Function ID: 1
Fix suggestion: To fix this bug, add a validation step to check the YAML content before deserializing it. One approach is to use a custom Constructor to restrict the types that can be deserialized. For example, you can create a Constructor that only allows deserialization of safe types and blocks deserialization of classes like java.io.FileOutputStream.
    public static JsonNode readYamlTree(String contents) {
        org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
        yaml.setAllowDuplicates(false);
        yaml.setBeanAccess(SnakeYaml.BEAN_ACCESS_FIELD);
        yaml.setTypeFilter(new TypeFilter() {
            @Override
            public boolean allow(String type) {
                return type.startsWith("java.lang.");
            }
        });
        return Json.mapper().convertValue(yaml.load(contents), JsonNode.class);
    }
Function ID: 2
Fix suggestion: Similar to Function ID 1, add a validation step in the readYamlValue function to check the YAML content before deserializing it. Utilize a custom Constructor to restrict the types that can}
