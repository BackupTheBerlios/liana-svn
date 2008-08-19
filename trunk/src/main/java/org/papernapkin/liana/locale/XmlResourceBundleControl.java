package org.papernapkin.liana.locale;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * A resource bundle loader which looks for XML resource bundle properties
 * files.  The properties are loaded via
 * {@link Properties#loadFromXML(InputStream)}.  Finding matching XML resource
 * files are the same as that used for .properties files except the file name
 * should end in &quot;.xml&quot;.
 * 
 * @link http://java.sun.com/developer/JDCTechTips/2005/tt1018.html
 * 
 * @author pchapman
 */
public class XmlResourceBundleControl extends ResourceBundle.Control
{
	private static final String XML = "xml";
	
	public List<String> getFormats(String baseName) {
		if (baseName == null)
			throw new NullPointerException();
		return Arrays.asList("xml");
	}
	
	public ResourceBundle newBundle(
			String baseName, Locale locale, String format, ClassLoader loader, boolean reload
		) throws IllegalAccessException, InstantiationException, IOException
	{
      if ((baseName == null) || (locale == null) ||
          (format == null) || (loader == null)) {
        throw new NullPointerException();
      }
      ResourceBundle bundle = null;
      if (format.equals(XML)) {
        String bundleName = toBundleName(baseName, locale);
        String resourceName = 
           toResourceName(bundleName, format);
        URL url = loader.getResource(resourceName);
        if (url != null) {
          URLConnection connection = url.openConnection();
          if (connection != null) {
            if (reload) {
              connection.setUseCaches(false);
            }
            InputStream stream = connection.getInputStream();
            if (stream != null) {
              BufferedInputStream bis = 
                 new BufferedInputStream(stream);
              bundle = new XMLResourceBundle(bis);
              bis.close();
            }
          }
        }
      }
      return bundle;
	}
	
	/**
	 * Converts the given <code>baseName</code> and <code>locale</code>
	 * to the bundle name. This method is called from the default
	 * implementation of the {@link #newBundle(String, Locale, String,
	 * ClassLoader, boolean) newBundle} and {@link #needsReload(String,
	 * Locale, String, ClassLoader, ResourceBundle, long) needsReload}
	 * methods.
	 *
	 * <p>This implementation returns the following value:
	 * <pre>
	 *     baseName + "_" + language + "_" + country + "_" + variant
	 * </pre>
	 * where <code>language</code>, <code>country</code> and
	 * <code>variant</code> are the language, country and variant values
	 * of <code>locale</code>, respectively. Final component values that
	 * are empty Strings are omitted along with the preceding '_'. If
	 * all of the values are empty strings, then <code>baseName</code>
	 * is returned.
	 *
	 * <p>For example, if <code>baseName</code> is
	 * <code>"baseName"</code> and <code>locale</code> is
	 * <code>Locale("ja",&nbsp;"",&nbsp;"XX")</code>, then
	 * <code>"baseName_ja_&thinsp;_XX"</code> is returned. If the given
	 * locale is <code>Locale("en")</code>, then
	 * <code>"baseName_en"</code> is returned.
	 *
	 * <p>Overriding this method allows applications to use different
	 * conventions in the organization and packaging of localized
	 * resources.
	 *
	 * @param baseName
	 *        the base name of the resource bundle, a fully
	 *        qualified class name
	 * @param locale
	 *        the locale for which a resource bundle should be
	 *        loaded
	 * @return the bundle name for the resource bundle
	 * @exception NullPointerException
	 *        if <code>baseName</code> or <code>locale</code>
	 *        is <code>null</code>
	 */
	public String toBundleName(String baseName, Locale locale) {
		if (locale == Locale.ROOT) {
			return baseName;
		}

		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();

		if (language == "" && country == "" && variant == "") {
			return baseName;
		}

		StringBuilder sb = new StringBuilder(baseName);
		sb.append('_');
		if (variant != "") {
			sb.append(language).append('_').append(country).append('_').append(variant);
		} else if (country != "") {
			sb.append(language).append('_').append(country);
		} else {
			sb.append(language);
		}
		return sb.toString();

	}

	private static class XMLResourceBundle extends ResourceBundle
	{
		private Properties props;
		XMLResourceBundle(InputStream stream) throws IOException {
			props = new Properties();
			props.loadFromXML(stream);
		}

		@Override
		protected Object handleGetObject(String key) {
			return props.getProperty(key);
		}

		@Override
		@SuppressWarnings("unchecked")
		public Enumeration getKeys() {
			return props.keys();
		}
	}
}
