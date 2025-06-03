/*
 * The MIT License
 * 
 * Copyright (c) 2004-2009, Sun Microsystems, Inc., Kohsuke Kawaguchi, Jorg Heymans
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.tasks.junit;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * As the name suggest: a resolver for XML entities.
 *
 * <p>
 * Basically, it provides the possibility to intercept online DTD lookups
 * and instead do offline lookup by redirecting to a local directory where
 * .dtd's are stored
 *
 * (useful when parsing testng-results.xml - which points to testng.org)
 *
 * @author Mikael Carneholm
 */
public class XMLEntityResolver implements EntityResolver {

    private static final String TESTNG_NAMESPACE = "http://testng.org/";

    /**
     * Intercepts the lookup of publicId, systemId
     */
public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
    if (systemId != null) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Will try to resolve systemId [" + systemId + "]");
        }

        // Validate systemId against allowed URLs or file paths
        try {
            URI uri = new URI(systemId);
            if (uri.isAbsolute()) {
                // If URI is absolute, it's a URL
                if (allowedURLs.contains(uri.toString())) {
                    return new InputSource(uri.toString());
                } else {
                    LOGGER.warning("SystemId " + systemId + " is not allowed. This could lead to a XXE vulnerability.");
                    return null;
                }
            } else {
                // If URI is relative, it's a file path
                File file = new File(systemId);
                if (file.isFile() && allowedFilePaths.contains(file.getAbsolutePath())) {
                    return new InputSource(new FileInputStream(file));
                } else {
                    LOGGER.warning("SystemId " + systemId + " is not allowed. This could lead to a XXE vulnerability.");
                    return null;
                }
            }
        } catch (URISyntaxException e) {
            LOGGER.warning("SystemId " + systemId + " is not allowed. This could lead to a XXE vulnerability.");
            return null;
        }
    }

    // Default fallback
    return null;
}
    private static final Logger LOGGER = Logger.getLogger(XMLEntityResolver.class.getName());
}
