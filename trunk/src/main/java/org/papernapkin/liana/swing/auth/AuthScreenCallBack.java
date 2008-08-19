package org.papernapkin.liana.swing.auth;

/**
 * Notifies listeners of a request to authenticate.  The listeners can then
 * use the information in the AuthRequest object to attempt authentication.
 * The listener should then set the authSuccessful member true on success or
 * false on failure.  If authentication was successful, the AuthScreen will
 * dispose of itself.
 */
public interface AuthScreenCallBack
{
	/**
	 * Indicates that the user has cancelled authentication.  The AuthScreen
	 * disposes itself before making this call.
	 */
	public void receiveAuthCancellation();
	
	/**
	 * Indicates that the user has provided authentication info and has
	 * requested authentication.
	 */
	public void receiveAuthRequest(AuthRequest request);
}
