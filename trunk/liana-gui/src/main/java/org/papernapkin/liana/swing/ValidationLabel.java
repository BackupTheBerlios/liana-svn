package org.papernapkin.liana.swing;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.papernapkin.liana.bean.IValidator;

/**
 * A label which can be used to show the status of validated data.
 * 
 * @author Philip A. Chapman
 */
public class ValidationLabel extends JLabel
{
	public enum ValidationStatus {
		INVALID,
		UNKNOWN,
		VALID
	};
	
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;
	
	private static final String EMPTY_IMAGE_PATH = "org/papernapkin/liana/resources/images/EmptyDot.png";
	private static final String REQUIRED_IMAGE_PATH = "org/papernapkin/liana/resources/images/YellowDot.png";
	//TODO Translations are needed
	private static final String REQUIRED_TOOLTIP_TEXT = "This is a required field";
	private static final String ERROR_IMAGE_PATH = "org/papernapkin/liana/resources/images/RedDot.png";
	private static final String VALID_IMAGE_PATH = "org/papernapkin/liana/resources/images/GreenDot.png";
	private static final ImageIcon emptyIcon = new ImageIcon(ValidationLabel.class.getClassLoader().getResource(EMPTY_IMAGE_PATH));
	private static final ImageIcon errorIcon = new ImageIcon(ValidationLabel.class.getClassLoader().getResource(ERROR_IMAGE_PATH));
	private static final ImageIcon requiredIcon = new ImageIcon(ValidationLabel.class.getClassLoader().getResource(REQUIRED_IMAGE_PATH));
	private static final ImageIcon validIcon = new ImageIcon(ValidationLabel.class.getClassLoader().getResource(VALID_IMAGE_PATH));
	
	private boolean required = false;
	private boolean errorFeedbackUsed = false;
	private ValidationStatus status = ValidationStatus.UNKNOWN;
	
    /**
     * Creates a new Validation Label without caption text.
     */
    public ValidationLabel() {
        super();
		evaluateStatus();
    }

    /**
     * Creates a new Validation Label with caption text.
     * @param text The caption text.
     */
    public ValidationLabel(String text) {
        super(text);
		evaluateStatus();
    }

    /**
     * Creates a new Validation Label with the given caption text and the
     * given alignment.
     * @param text The caption text.
     * @param horizontalAlignment The aliangment.
     */
    public ValidationLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
		evaluateStatus();
    }

	/**
	 * Creates a new Validation Label without caption text.
	 * @param required Indicates that the label should indicate that the field
	 *                 is required.
	 */
	public ValidationLabel(boolean required) {
		super();
		setRequired(required);
	}

	/**
	 * Creates a new Validation Label with caption text.
	 * @param text The caption text.
	 * @param required Indicates that the label should indicate that the field
	 *                 is required.
	 */
	public ValidationLabel(String text, boolean required) {
		super(text);
		setRequired(required);
	}

	/**
	 * Creates a new Validation Label with the given caption text and the
	 * given alignment.
	 * @param text The caption text.
	 * @param horizontalAlignment The aliangment.
	 * @param required Indicates that the label should indicate that the field
	 *                 is required.
	 */
	public ValidationLabel(String text, int horizontalAlignment, boolean required) {
		super(text, horizontalAlignment);
		setRequired(required);
	}

	
	/**
	 * Returns boolean to indicate if error feed back (a beep) will be used
	 * 
	 * @return boolean
	 */
	public boolean isErrorFeedbackUsed()
	{
		return errorFeedbackUsed;
	}
	
	
	/**
	 * indicate if error feed back (a beep) will be used
	 */
	public void setErrorFeedbackUsed(boolean used)
	{
		errorFeedbackUsed = used;
	}
	
	/**
	 * Evaluates the status and updates the UI accordingly.
	 */    
	private void evaluateStatus()
	{
		if (status == ValidationStatus.INVALID) {
			super.setIcon(errorIcon);
			if(errorFeedbackUsed)UIManager.getLookAndFeel().provideErrorFeedback(this);
		} else if (status == ValidationStatus.UNKNOWN) {
			if (required) {
				super.setIcon(requiredIcon);
			} else {
				super.setIcon(emptyIcon);
			}
		} else if (status == ValidationStatus.VALID) {
			super.setIcon(validIcon);
		} else {
			super.setIcon(emptyIcon);
		}
	}
    
    /**
     * Returns whether the related value is required.
     * @return True if the related value is required.
     */
    public boolean getRequired()
    {
    	return required;
    }
    
	/**
	 * @see javax.swing.JLabel#setIcon(javax.swing.Icon)
	 * Changes are ignored.
	 */
	public void setIcon(Icon icon)
	{
		return;
	}

	/**
	 * Indicates that the related component's value is required.
	 * @param required If true, indicates that the value is required.
	 */
	public void setRequired(boolean required)
	{
		this.required = required;
		if (required) {
			this.setToolTipText(REQUIRED_TOOLTIP_TEXT);
		}
		evaluateStatus();
	}
    
    /**
     * Set the status of validation.  If the field is valid, a green dot will
     * be provided, else a red one.
     * @param valid	If true, indicates the value in the related component
     *              is valid.
     * @param validationErrorText Text indicating why the related component's
     *                            value is not valid, or null.
     */
	public void setValidationStatus(ValidationStatus status, String validationErrorText)
	{
		this.status = status;
		setToolTipText(validationErrorText);
		evaluateStatus();
	}
	
	/**
	 * Sets the status of validation based on whether an error message was
	 * found.  In order for this to work, the validation label's name must be
	 * set to that of the bean member being validated.  Also, IValidator's
	 * validate method should have already been called.
	 * @param validator The validator to check for errors.
	 */
	public void setValidationStatus(IValidator<?> validator) {
		String s = validator.getErrorMessage(getName());
		if (s == null)
			setValidationStatus(ValidationStatus.VALID, null);
		else
			setValidationStatus(ValidationStatus.INVALID, s);
	}
}
