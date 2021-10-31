package com.jubiman.rankup.exeptions;

public class UnsupportedDatabaseTypeException extends RuntimeException {
	public UnsupportedDatabaseTypeException(String dbType) {
		super("Unsupported database type: " + dbType);
	}
}
