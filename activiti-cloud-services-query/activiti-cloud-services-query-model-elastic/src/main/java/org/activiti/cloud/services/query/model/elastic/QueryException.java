package org.activiti.cloud.services.query.model.elastic;

public class QueryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public QueryException(String message) {
		super(message);
	}

	public QueryException(String message, Throwable cause) {
		super(message, cause);
	}
}
