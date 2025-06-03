/*
 * Copyright 2002-2006,2009 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opensymphony.xwork2.validator.validators;

import com.opensymphony.xwork2.validator.ValidationException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * URLValidator checks that a given field is a String and a valid URL
 *
 * <pre>
 * &lt;validators&gt;
 *      &lt;!-- Plain Validator Syntax --&gt;
 *      &lt;validator type="url"&gt;
 *          &lt;param name="fieldName"&gt;myHomePage&lt;/param&gt;
 *          &lt;message&gt;Invalid homepage url&lt;/message&gt;
 *      &lt;/validator&gt;
 *
 *      &lt;!-- Field Validator Syntax --&gt;
 *      &lt;field name="myHomepage"&gt;
 *          &lt;field-validator type="url"&gt;
 *              &lt;message&gt;Invalid homepage url&lt;/message&gt;
 *          &lt;/field-validator&gt;
 *      &lt;/field&gt;
 * &lt;/validators&gt;
 * </pre>
 */
public class URLValidator extends FieldValidatorSupport {

    private String urlRegex;
    private String urlRegexExpression;

    public void validate(Object object) throws ValidationException {
        String fieldName = getFieldName();
        Object value = this.getFieldValue(fieldName, object);

        // if there is no value - don't do comparison
        // if a value is required, a required validator should be added to the field
        if (value == null || value.toString().length() == 0) {
            return;
        }

        if (!(value.getClass().equals(String.class)) || !Pattern.compile(getUrlRegex()).matcher((String) value).matches()) {
            addFieldError(fieldName, object);
        }
    }

    /**
     * This is used to support client-side validation, it's based on
     * http://stackoverflow.com/questions/161738/what-is-the-best-regular-expression-to-check-if-a-string-is-a-valid-url
     *
     * @return regex to validate URLs
     */
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.assertEquals;

public class UrlValidator {

    private String urlRegexExpression = "^(https?|ftp)://[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(:[0-9]+)?(/.*)?$";

    public String getUrlRegex() {
        if (StringUtils.isNotEmpty(urlRegexExpression)) {
            return urlRegexExpression;
        } else {
            throw new RuntimeException("UrlRegexExpression is not defined");
        }
    }

    @Test
    public void testValidUrls() {
        assertUrlMatches(getUrlRegex(), "https://www.google.com", true);
        assertUrlMatches(getUrlRegex(), "http://www.google.com", true);
        assertUrlMatches(getUrlRegex(), "ftp://www.google.com", true);
        assertUrlMatches(getUrlRegex(), "https://google.com", true);
    }

    @Test
    public void testInvalidUrls() {
        assertUrlMatches(getUrlRegex(), "not a url", false);
        assertUrlMatches(getUrlRegex(), "http:/www.google.com", false);
        assertUrlMatches(getUrlRegex(), "http://www.google.com/", true);
    }

    private void assertUrlMatches(String regex, String url, boolean shouldMatch) {
        Pattern pattern = Pattern.compile(regex);
        if (shouldMatch) {
            assertEquals(true, pattern.matcher(url).matches());
        } else {
            assertEquals(false, pattern.matcher(url).matches());
        }
    }
}
    public void setUrlRegex(String urlRegex) {
        this.urlRegex = urlRegex;
    }

    public void setUrlRegexExpression(String urlRegexExpression) {
        this.urlRegexExpression = urlRegexExpression;
    }

}
