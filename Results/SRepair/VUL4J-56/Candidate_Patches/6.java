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
        
        // Check if the systemId is a valid URL
        try {
            new URL(systemId);
        } catch (MalformedURLException e) {
            // If the systemId is not a valid URL, throw an exception
            throw new SAXException("Invalid URL: " + systemId);
        }

        // TestNG system-ids
        if (systemId.startsWith(TESTNG_NAMESPACE)) {
            LOGGER.fine("It's a TestNG document, will try to lookup DTD in classpath");
            String dtdFileName = systemId.substring(TESTNG_NAMESPACE.length());

            // Check if the dtdFileName is a valid file path
            File dtdFile = new File(dtdFileName);
            if (dtdFile.exists() && dtdFile.isFile()) {
                return new InputSource(dtdFileName);
            } else {
                // If the dtdFile doesn't exist or is not a file, throw an exception
                throw new SAXException("DTD file not found: " + dtdFileName);
            }
        }
    }
    // Default fallback
    return null;
}
    private static final Logger LOGGER = Logger.getLogger(XMLEntityResolver.class.getName());
}
