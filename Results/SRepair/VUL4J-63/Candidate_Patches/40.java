/*
 * Copyright 2015-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.ovsdb.rfc.utils;

/**
 * Version utility class.
 */
public final class VersionUtil {

    /**
     * Constructs a VersionUtil object. Utility classes should not have a public
     * or default constructor, otherwise IDE will compile unsuccessfully. This
     * class should not be instantiated.
     */
    private VersionUtil() {
    }

    public static final String DEFAULT_VERSION_STRING = "0.0.0";
    private static final String FORMAT = "(\\d+)\\.(\\d+)\\.(\\d+)";

    /**
     * Match version by the format.
     * @param version the version String
     * @throws IllegalArgumentException this is an illegal argument exception
     */
    public static void versionMatch(String version) {
        if (!version.matches(FORMAT)) {
            throw new IllegalArgumentException("<" + version
                    + "> does not match format " + FORMAT);
        }
    }

    /**
     * Compare fromVersion and toVersion.
     * @param fromVersion the initial version
     * @param toVersion the end of the version
     * @return an int number
     */
import java.util.logging.Logger;

public class VersionCompare {
    private static final Logger LOG = Logger.getLogger(VersionCompare.class.getName());

    public static int versionCompare(String fromVersion, String toVersion) {
        if (fromVersion == null || toVersion == null) {
            throw new IllegalArgumentException("Null values are not allowed");
        }

        String[] fromArr = fromVersion.split("\\.");
        String[] toArr = toVersion.split("\\.");

        int fromFirst = getInteger(fromArr, 0);
        int fromMiddle = getInteger(fromArr, 1);
        int fromEnd = getInteger(fromArr, 2);
        int toFirst = getInteger(toArr, 0);
        int toMiddle = getInteger(toArr, 1);
        int toEnd = getInteger(toArr, 2);

        if (fromFirst - toFirst != 0) {
            return fromFirst - toFirst;
        } else if (fromMiddle - toMiddle != 0) {
            return fromMiddle - toMiddle;
        } else {
            return fromEnd - toEnd;
        }
    }

    private static int getInteger(String[] arr, int index) {
        try {
            return index < arr.length ? Integer.parseInt(arr[index]) : 0;
        } catch (NumberFormatException e) {
            LOG.severe("Invalid number format at index " + index);
            throw new IllegalArgumentException("Invalid number format at index " + index, e);
        }
    }
}}
