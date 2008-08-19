package org.papernapkin.liana.locale;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Tests the org.papernapkin.liana.locale.Translation class.
 * 
 * @author pchapman
 */
public class TestTranslation
{
	private static final String IDENTIFIER_KEY = "identifier";
	
	/** Tests loading a translation by name. **/
	@Test
	public void testLoadByBaseName() {
		String text;
		Translation trans;

		// en_US translation does not exist, but base does
		trans = Translation.getTranslation("org.papernapkin.liana.locale.Translation", Locale.US);
		assertNotNull("The translation with name \"org.papernapkin.liana.locale.Translation\" could not be found.", trans);
		text = trans.getString(IDENTIFIER_KEY);
		assertEquals("Wrong translation loaded", "Translation_en_US.xml", text);
		// en_GB translation exists
		trans = Translation.getTranslation("org.papernapkin.liana.locale.Translation", Locale.UK);
		assertNotNull("The translation with name \"org.papernapkin.liana.locale.Translation\" could not be found.", trans);
		text = trans.getString(IDENTIFIER_KEY);
		assertEquals("Wrong translation loaded", "Translation_en_GB.xml", text);
		
		trans = Translation.getTranslation("DoesNotExist", Locale.US);
		assertNull("The translation with name \"DoesNotExist\" should not be found.", trans);
	}
	
	/** Tests loading a translation by class name. **/
	@Test
	public void testLoadByClassName() {
		String text;
		Translation trans;

		// Test loading by this class's name
		// en_US translation exists,
		trans = Translation.getTranslation(getClass(), Locale.US);
		assertNotNull("The translation for class \"org.papernapkin.liana.local.TestTranslation\" locale en_US could not be found.", trans);
		text = trans.getString(IDENTIFIER_KEY);
		assertEquals("Wrong translation loaded", "TestTranslation_en_US.xml", text);
		
		// en_GB translation exists,
		trans = Translation.getTranslation(getClass(), Locale.UK);
		assertNotNull("The translation for class \"org.papernapkin.liana.local.TestTranslation\" locale en_UK could not be found.", trans);
		text = trans.getString(IDENTIFIER_KEY);
		assertEquals("Wrong translation loaded", "TestTranslation_en_GB.xml", text);
		
		// en_CA translation does not exist.  Should return en_US translation.
		trans = Translation.getTranslation(getClass(), Locale.CANADA);
		assertNotNull("The translation for class \"org.papernapkin.liana.local.TestTranslation\" locale en_CA could not be found.", trans);
		text = trans.getString(IDENTIFIER_KEY);
		assertEquals("Wrong translation loaded", "TestTranslation_en_US.xml", text);

		// Test loading by enclosing class's name
		// Anonymous inner class.
		new Runnable() {
			public void run() {
				String text;
				Translation trans;

				trans = Translation.getTranslation(getClass(), Locale.US);
				assertNotNull("The translation for class \"org.papernapkin.liana.local.TestTranslation\" locale en_US could not be found.", trans);
				text = trans.getString(IDENTIFIER_KEY);
				assertEquals("Wrong translation loaded", "TestTranslation_en_US.xml", text);
				
				trans = Translation.getTranslation(getClass(), Locale.UK);
				assertNotNull("The translation for class \"org.papernapkin.liana.local.TestTranslation\" locale en_US could not be found.", trans);
				text = trans.getString(IDENTIFIER_KEY);
				assertEquals("Wrong translation loaded", "TestTranslation_en_GB.xml", text);
			}
		}.run();
		new EnclosingClassLoadTester().run();
	}

	/** A private class to test loading by enclosing class. */
	private class EnclosingClassLoadTester implements Runnable {
		public void run() {
			String text;
			Translation trans;

			trans = Translation.getTranslation(getClass(), Locale.US);
			assertNotNull("The translation for class \"org.papernapkin.liana.local.TestTranslation\" locale en_US could not be found.", trans);
			text = trans.getString(IDENTIFIER_KEY);
			assertEquals("Wrong translation loaded", "TestTranslation_en_US.xml", text);
			
			trans = Translation.getTranslation(getClass(), Locale.UK);
			assertNotNull("The translation for class \"org.papernapkin.liana.local.TestTranslation\" locale en_US could not be found.", trans);
			text = trans.getString(IDENTIFIER_KEY);
			assertEquals("Wrong translation loaded", "TestTranslation_en_GB.xml", text);
		}
	}
	
	/** Tests getting text and replacing arguments with parameters. */
	@Test
	public void testTextWithParameters() {
		String text;
		Translation trans = Translation.getTranslation(getClass(), Locale.US);
		assertNotNull("Translation for locale en_US not found.", trans);
		text = trans.getString("test.text1", "foo");
		assertEquals("Argument replacement failed.", "Parameter 1 is foo", text);
		text = trans.getString("test.text2", "foo", "bar", "baz");
		assertEquals("Argument replacement failed.", "Parameter 1 is foo Parameter 2 is bar Parameter 3 is baz", text);
	}
}
