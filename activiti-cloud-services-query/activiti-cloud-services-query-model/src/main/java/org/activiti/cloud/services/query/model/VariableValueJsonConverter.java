/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.services.query.model;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VariableValueJsonConverter implements AttributeConverter<VariableValue<?>, String> {

    private static ObjectMapper objectMapper;

    public VariableValueJsonConverter() {
    }

    public VariableValueJsonConverter(ObjectMapper objectMapper) {
        VariableValueJsonConverter.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(VariableValue<?> variableValue) {
        try {
            return objectMapper.writeValueAsString(variableValue);
        } catch (JsonProcessingException e) {
            throw new QueryException("Unable to serialize variable.", e);
        }
    }

    @Override
    public VariableValue<?> convertToEntityAttribute(String dbData) {
    	return new LazyVariableValue<Object>(dbData);
    }

    class LazyVariableValue<T> extends VariableValue<T> {
        private String rawValue;
        private VariableValue<T> value;
    	
        LazyVariableValue(String json) {
        	this.rawValue = json;
        }
        
        @SuppressWarnings("unchecked")
		@Override
        public T getValue() {
        	if(value == null) {
	        	try {
	       			value = objectMapper.readValue(rawValue, VariableValue.class);
				} catch (Throwable cause) {
					throw new QueryException("Unable to deserialize variable.", cause);
				}
        	}
        	
        	return value.getValue();
        }
    }
    
}
