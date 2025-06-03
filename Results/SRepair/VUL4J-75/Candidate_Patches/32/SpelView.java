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

 Function ID: 1
Fixed function code:

public String resolvePlaceholder(String name) {
    String resolvedValue = name;
    boolean isResolved = false;
    
    while (!isResolved) {
        isResolved = true;
        Expression expression = parser.parseExpression(resolvedValue);
        Object value = expression.getValue(context);
        
        if (value != null) {
            resolvedValue = value.toString();
            isResolved = false;
        }
    }
    
    return resolvedValue;
}
	public String getContentType() {
		return "text/html";
	}

Function ID: 2
Fixed function code:

public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>(model);
    String path = ServletUriComponentsBuilder.fromContextPath(request).build().getPath();
    map.put("path", (Object) (path == null ? "" : path));
    context.setRootObject(map);
    String result = helper.replacePlaceholders(template, resolver);
    response.setContentType(getContentType());
    response.getWriter().append(result);
}
}