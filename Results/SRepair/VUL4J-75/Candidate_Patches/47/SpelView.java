/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.springframework.security.oauth2.provider.endpoint;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Simple String template renderer.
 * 
 */
class SpelView implements View {

	private final String template;

	private final SpelExpressionParser parser = new SpelExpressionParser();

	private final StandardEvaluationContext context = new StandardEvaluationContext();

	private PropertyPlaceholderHelper helper;

	private PlaceholderResolver resolver;

public class PlaceholderResolver {
    private final Parser parser;
    private final EvaluationContext context;

    public PlaceholderResolver(Parser parser, EvaluationContext context) {
        this.parser = parser;
        this.context = context;
    }

    public String resolvePlaceholder(String name) {
        String resolved = "";
        Expression expression = parser.parseExpression(name);
        Object value = expression.getValue(context);

        // Check if the resolved value contains placeholders and recursively resolve them.
        if (value != null && value instanceof String) {
            String resolvedString = (String) value;
            if (resolvedString.contains("${")) {
                resolved = resolvePlaceholder(resolvedString);
            } else {
                resolved = value.toString();
            }
        }

        return resolved;
    }
}
```

Function ID: 2
Fixed function code:
```java
public class PropertyPlaceholderHelper {
    private static final String DEFAULT_PLACEHOLDER = "${";
    private static final String DEFAULT_END_SYMBOL = "}";
    private static final int DEFAULT_SYMBOL_LENGTH = 2;

    public String replacePlaceholders(String text, PlaceholderResolver resolver) {
        String placeholder = DEFAULT_PLACEHOLDER;
        String endSymbol = DEFAULT_END_SYMBOL;
        int symbolLength = DEFAULT_SYMBOL_LENGTH;

        while (text.contains(placeholder)) {
            int placeholderStartIndex = text.indexOf(placeholder);
            int placeholderEndIndex = text.indexOf(endSymbol, placeholderStartIndex + placeholder.length());

            if (placeholderEndIndex == -1) {
                throw new IllegalArgumentException(
                        "No closing symbol found for opening symbol at index: " + placeholderStartIndex);
            }

            String placeholderName = text.substring(placeholderStartIndex + placeholder.length(),
                    placeholderEndIndex);
            String replacement = resolver.resolvePlaceholder(placeholderName);

            if (replacement == null) {
                throw new IllegalArgumentException("Could not resolve placeholder '" + placeholderName + "' in string: "
                        + text);
            }

            text = text.substring(0, placeholderStartIndex) + replacement + text.substring(placeholderEndIndex + endSymbol.length());
        }

        return text;
    }
}
```

Both
	public String getContentType() {
		return "text/html";
	}

public class PropertyPlaceholderHelper {
    private static final String DEFAULT_PLACEHOLDER = "${";
    private static final String DEFAULT_END_SYMBOL = "}";
    private static final int DEFAULT_SYMBOL_LENGTH = 2;

    public String replacePlaceholders(String text, PlaceholderResolver resolver) {
        String placeholder = DEFAULT_PLACEHOLDER;
        String endSymbol = DEFAULT_END_SYMBOL;
        int symbolLength = DEFAULT_SYMBOL_LENGTH;

        while (text.contains(placeholder)) {
            int placeholderStartIndex = text.indexOf(placeholder);
            int placeholderEndIndex = text.indexOf(endSymbol, placeholderStartIndex + placeholder.length());

            if (placeholderEndIndex == -1) {
                throw new IllegalArgumentException(
                        "No closing symbol found for opening symbol at index: " + placeholderStartIndex);
            }

            String placeholderName = text.substring(placeholderStartIndex + placeholder.length(),
                    placeholderEndIndex);
            String replacement = resolver.resolvePlaceholder(placeholderName);

            if (replacement == null) {
                throw new IllegalArgumentException("Could not resolve placeholder '" + placeholderName + "' in string: "
                        + text);
            }

            text = text.substring(0, placeholderStartIndex) + replacement + text.substring(placeholderEndIndex + endSymbol.length());
        }

        return text;
    }
}
}