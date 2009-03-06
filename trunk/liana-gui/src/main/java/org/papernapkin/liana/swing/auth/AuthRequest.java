package org.papernapkin.liana.swing.auth;

public interface AuthRequest
{
	/**
	 * Indicates whether the authentication as handled by the listeners was
	 * successful.
	 */
	boolean isAuthSuccessful();
	
	String getUsername();
	
	String getPass();
	
	String getAdditionalValue(String field);
	
	/**
	 * If the authentication is successful, the listener is to set this member
	 * true, else false.
	 */
	void setAuthSuccessful(boolean successful);
}
