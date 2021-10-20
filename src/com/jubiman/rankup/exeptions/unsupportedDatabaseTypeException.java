package com.jubiman.rankup.exeptions;

public class unsupportedDatabaseTypeException extends RuntimeException {
	public unsupportedDatabaseTypeException(String dbType) {
		super("unsupported database type: " + dbType);
	}
}
