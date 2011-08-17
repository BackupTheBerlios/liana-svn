package org.papernapkin.liana.locale;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides text to be displayed in the application by base name and locale.
 * The text will be searched for in the file {basename}_{locale}.xml.  This is
 * basically an extension of java.util.ResourceBundle.  This class allows for
 * loading the resource bundle through xml style properties files.  It also
 * adds a method which also provides for argument replacement in the strings
 * contained in the bundle.
 * 
 * @author pchapman
 */
public class Translation
{
	private static XmlResourceBundleControl xmlcontrol = new XmlResourceBundleControl();
	
	private ResourceBundle bundle;
	
	private Translation(ResourceBundle bundle) {
		super();
		this.bundle = bundle;
	}

	/** Gets the underlying resource bundle. */
	public ResourceBundle getResourceBundle() {
		return bundle;
	}

	/**
	 * Gets the translation for the default Locale.
	 * @see #getTranslation(java.lang.String, java.util.Locale)
	 * @see java.util.Locale#getDefault()
	 * @return A translation instance.
	 */
	public static Translation getTranslation(Class<?> clazz) {
		return getTranslation(clazz, Locale.getDefault());
	}
	
	/**
	 * Gets the appropriate translation for the indicated locale.  If no
	 * appropriate translation can be found fir the locale, the translation
	 * for en_US is returned, if found.
	 * @param clazz The class whos name is to be used as base name.  If none
	 *              is found for the class and there is an enclosing class,
	 *              an attempt will be made to use the name of the enclosing
	 *              class.
	 * @param locale The locale to return a translation for.
	 * @return A translation instance.
	 */
	public static Translation getTranslation(Class<?> clazz, Locale locale) {
		Translation t = getTranslation(clazz.getName(), locale);
		if (t == null && clazz.getEnclosingClass() != null) {
			return getTranslation(clazz.getEnclosingClass(), locale);
		}
		return t;
	}

	/**
	 * Gets the translation for the default Locale.
	 * @see #getTranslation(java.lang.String, java.util.Locale)
	 * @see java.util.Locale#getDefault()
	 * @return A translation instance.
	 */
	public static Translation getTranslation(String basename) {
		return getTranslation(basename, Locale.getDefault());
	}
	
	/**
	 * Gets the appropriate translation for the indicated locale.  If no
	 * appropriate translation can be found for the locale, the translation
	 * for en_US is returned, if found.
	 * @param locale The locale to return a translation for.
	 * @return A translation instance.
	 */
	public static Translation getTranslation(String basename, Locale locale) {
		Translation trans = null;
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(basename, locale, xmlcontrol);
			if (bundle == null) {
				LoggerFactory.getLogger(Translation.class).warn("Translation not found for basename{}, locale {}", basename, locale);
				if (! locale.equals(Locale.US)) {
					// Try defaulting to US English
					trans = getTranslation(basename, Locale.US);
				}
			} else {
				trans = new Translation(bundle);
			}
		} catch (MissingResourceException mre) {
			LoggerFactory.getLogger(Translation.class).error("Error loading translation for basename " + basename + ", locale " + locale, mre);
			if (! locale.equals(Locale.US)) {
				// Try defaulting to US English
				trans = getTranslation(basename, Locale.US);
			}
		} catch (Exception ioe) {
			LoggerFactory.getLogger(Translation.class).error("Error loading translation for basename " + basename + ", locale " + locale, ioe);
			if (! locale.equals(Locale.US)) {
				// Try defaulting to US English
				trans = getTranslation(basename, Locale.US);
			}
		}
		return trans;
	}
	
	/**
	 * Returns text in the translation based on the key.  Before the text is
	 * returned, placeholders in the text is replaced with values in the params
	 * based on position.
	 * 
	 * <h3>The properties file</h3>
	 * <code>
	 *   &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
	 *   &lt;!DOCTYPE properties SYSTEM &quot;http://java.sun.com/dtd/properties.dtd&quot;&gt;
	 *   &lt;properties&gt;
	 *     &lt;entry key=&quot;widget.report&quot;&gt;There are {1} widgets in {2}.&lt;/entry&gt;
	 *   &lt;/properties&gt;
	 * </code>
	 * 
	 * <h3>The code:</h3>
	 * <code>
	 *   // Load using base name
	 *   Translation trans = new Translation(&quot;mypackage.MyClass&quot;, Locale.US);
	 *   System.out.println(trans.getText(&quot;widget.report&quot;, 42, &quot;warehouse&quot;);
	 *   // Load using class name
	 *   Translation trans = new Translation(getClass(), Locale.US);
	 *   System.out.println(trans.getText(&quot;widget.report&quot;, 42, &quot;warehouse&quot;);
	 * </code>
	 * 
	 * @param key A key identifying the text.
	 * @param params Params to be used to replace placeholders in the text.
	 * @return The translated text.
	 */
	public String getString(String key, Object ... params) {
		String s;

		// Look up the string.
		try {
			s = bundle.getString(key).trim();
		} catch (MissingResourceException e) {
			LoggerFactory.getLogger(getClass()).warn("Unable to find string {}", key, e);
			return null;
		}
		
		// Replace placeholders with param values
		StringBuilder sb;
		String val;
		for (int i = 0; i < params.length; i++) {
			sb = new StringBuilder();
			sb.append('{');
			sb.append(i + 1);
			sb.append('}');
			val = params[i] == null ? "null" : params[i].toString();
			s = s.replace(sb.toString(), val);
		}
		return s;
	}
}
