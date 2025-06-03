/*
 * Copyright 2012 the original author or authors.
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
package org.springframework.data.history;

import java.lang.annotation.Annotation;

import org.joda.time.DateTime;
import org.springframework.data.util.AnnotationDetectionFieldCallback;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * A {@link RevisionMetadata} implementation that inspects the given object for fields with the configured annotations
 * and returns the field's values on calls to {@link #getRevisionDate()} and {@link #getRevisionNumber()}.
 * 
 * @author Oliver Gierke
 */
public class AnnotationRevisionMetadata<N extends Number & Comparable<N>> implements RevisionMetadata<N> {

	private final Object entity;
	private final N revisionNumber;
	private final DateTime revisionDate;

	/**
	 * Creates a new {@link AnnotationRevisionMetadata} inspecing the given entity for the given annotations. If no
	 * annotations will be provided these values will not be looked up from the entity and return {@literal null}.
	 * 
	 * @param entity must not be {@literal null}.
	 * @param revisionNumberAnnotation
	 * @param revisionTimeStampAnnotation
	 */
Function ID: 1
Fix suggestion: In the AnnotationRevisionMetadata constructor, ensure that the revisionNumber is properly converted to the expected type before assigning it to the revisionNumber field. You can use a Long.valueOf() method to convert the value to a Long type. Update the code as follows:

Replace:
this.revisionNumber = numberCallback.getValue(entity);

With:
this.revisionNumber = Long.valueOf(numberCallback.getValue(entity).toString());
```

Here is the fixed function for the second buggy function:

Function ID: 2
Fixed function code:
```java
Function ID: 2
Fix suggestion: In the create method, make sure that the addTail parameter is correctly handled when creating the PropertyPath. Update the code as follows:

Replace:
current.next = create(addTail, current.type, newBase);

With:
current.next = create(addTail, current.type, "", newBase);
```
	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.history.RevisionMetadata#getRevisionNumber()
	 */
	public N getRevisionNumber() {
		return revisionNumber;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.history.RevisionMetadata#getRevisionDate()
	 */
	public DateTime getRevisionDate() {
		return revisionDate;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.history.RevisionMetadata#getDelegate()
	 */
	@SuppressWarnings("unchecked")
	public <T> T getDelegate() {
		return (T) entity;
	}
}
