package org.papernapkin.liana.bean;
import java.util.Map;

/**
 * A class which will validate a bean and return a map of error messages if the
 * bean is invalid.
 * 
 * @author pchapman
 */
public interface IValidator<T>
{
	/**
	 * Returns a map of error messages, if any.
	 * @return The map.  The map key is the bean member's name.  The map value
	 *         is the error message.  If there are no errors, the map will be
	 *         empty.
	 */
	public Map<String, String> getErrorMessages();
	
	/**
	 * Returns an error message for the given bean member's name.  If there are
	 * no errors for the bean member, the return value will be null.
	 * @param memberName The name of the bean member.
	 * @return An error message for the given bean member's name.  If there are
	 *         no errors for the bean member, the return value will be null.
	 */
	public String getErrorMessage(String beanMember);
	
	/**
	 * Validates the given bean and populates the error messages, if any.
	 * @param bean The bean to validate.
	 * @return True if the bean is valid, else false.
	 */
	public boolean validate(T bean);
}
