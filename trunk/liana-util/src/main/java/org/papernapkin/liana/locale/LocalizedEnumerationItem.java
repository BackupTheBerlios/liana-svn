package org.papernapkin.liana.locale;

/**
 * Wraps an Enum object and provides a localized description for the
 * Enum from a provided Translation object.  The Translation object
 * looks for a toString text keyed by the full class name of the
 * enumeration object followed by a dot and its value name.
 *
 * @author <a href="mail:pchapman@pcsw.us">Philip A. Chapman</a>
 */
public class LocalizedEnumerationItem<T extends Enum>
{
	private T enumValue;
	public T getEnumValue() {
		return enumValue;
	}
	public void setEnumValue(T enumValue) {
		this.enumValue = enumValue;
	}

	private String localizedString;
	public String getLocalizedString() {
		return localizedString;
	}
	public void setLocalizedString(String localizedString) {
		this.localizedString = localizedString;
	}

	private LocalizedEnumerationItem(T enumValue, String localizedString) {
		this.enumValue = enumValue;
		this.localizedString = localizedString;
	}

	/**
	 * Attempts to find a translation based on the enums' class name.  Failing
	 * that, the name of the enum as defined in the source code is used.
	 * @param enums The enums to return LocalizedEnumerationItems for.
	 * @param <T> A subclass of Enum.
	 * @return LocalizedEnumerationItems which wrap the enums.
	 */
	public static <T extends Enum> LocalizedEnumerationItem<T>[] getLocalizedItems(T[] enums)
	{
		if (enums == null || enums.length == 0) {
			return new LocalizedEnumerationItem[0];
		} else {
			LocalizedEnumerationItem<T>[] items = new LocalizedEnumerationItem[enums.length];
			Translation trans = Translation.getTranslation(items[0].getClass());
			return getLocalizedItems(trans, enums);
		}
	}

	/**
	 * Attempts to localized names for enums' in the translation keyed by class
	 * name followed by a dot, then enum name.  Failing that, the name of the
	 * enum as defined in the source code is used.
	 * @param trans The translation to use for getting localized names for the
	 *              enums.
	 * @param enums The enums to return LocalizedEnumerationItems for.
	 * @param <T> A subclass of Enum.
	 * @return LocalizedEnumerationItems which wrap the enums.
	 */
	public static <T extends Enum> LocalizedEnumerationItem<T>[] getLocalizedItems(Translation trans, T[] enums)
	{
		if (enums == null || enums.length == 0) {
			return new LocalizedEnumerationItem[0];
		} else {
			LocalizedEnumerationItem<T>[] items = new LocalizedEnumerationItem[enums.length];
			String baseName = enums[0].getClass().getName() + ".";
			String localizedString;
			for (int i = 0; i < enums.length; i++) {
				localizedString = trans.getString(baseName + enums[i].name());
				if (localizedString == null) {
					// Fall back to default
					localizedString = enums[i].toString();
				}
				items[i] = new LocalizedEnumerationItem<T>(enums[i], localizedString);
			}
			return items;
		}
	}

	@Override
	public String toString() {
		return localizedString;
	}
}
