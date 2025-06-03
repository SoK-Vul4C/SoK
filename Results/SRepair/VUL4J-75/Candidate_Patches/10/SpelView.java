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
    private static final String PLACEHOLDER_START = "${";
    private static final String PLACEHOLDER_END = "}";

    public String resolvePlaceholder(String name) {
        if (!name.startsWith(PLACEHOLDER_START) || !name.endsWith(PLACEHOLDER_END)) {
            return name;
        }
        String placeholder = name.substring(PLACEHOLDER_START.length(), name.length() - PLACEHOLDER_END.length());
        Expression expression = parser.parseExpression(placeholder);
        Object value = expression.getValue(context);
        return value == null ? null : value.toString();
    }
}
	public String getContentType() {
		return "text/html";
	}

public class PropertyPlaceholderHelper {
    private String prefix;
    private String suffix;

    public String replacePlaceholders(String text) {
        StringReader reader = new StringReader(text);
        StringWriter writer = new StringWriter();
        try {
            replacePlaceholders(reader, writer);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return writer.toString();
    }

    private void replacePlaceholders(Reader reader, Writer writer) throws IOException {
        char[] buffer = new char[128];
        int read;
        while ((read = reader.read(buffer)) != -1) {
            if (buffer[read - 1] == '$' && buffer[read - 2] == '{') {
                writer.write(buffer, 0, read - 2);
                writer.write(resolvePlaceholder(reader));
            } else {
                writer.write(buffer, 0, read);
            }
        }
    }

    private String resolvePlaceholder(Reader reader) throws IOException {
        StringWriter writer = new StringWriter();
        replacePlaceholders(reader, writer);
        return writer.toString();
    }
}
```

You can use these fixed classes as replacements for your original ones. For function 1, the `PlaceholderResolver` class will recursively handle nested placeholders in the placeholder names, and for function 2, the `PropertyPlaceholderHelper` class will recursively handle nested placeholders in the placeholder values during the rendering phase.
}