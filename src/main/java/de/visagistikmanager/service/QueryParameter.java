package de.visagistikmanager.service;

import java.util.HashMap;
import java.util.Map;

public class QueryParameter {

	public static QueryParameter with(String name, Object value) {
		return new QueryParameter(name, value);
	}

	private Map<String, Object> parameters = new HashMap<>();

	private QueryParameter(String name, Object value) {
		parameters.put(name, value);
	}

	public QueryParameter and(String name, Object value) {
		parameters.put(name, value);
		return this;
	}

	public Map<String, Object> parameters() {
		return parameters;
	}

}