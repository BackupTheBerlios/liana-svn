package org.papernapkin.liana.xml.sax;

import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Class Description here
 *
 * @author pchapman
 */
public abstract class AbstractDocumentHandler extends DefaultHandler {
    protected StringBuffer textbuffer;

    /**
     * Can be overriden by subclasses rather than overriding @see DefaultHandler#endElement(String, String, String)
     *
     * @param uri       The uri.
     * @param localName The localName.
     * @param qName     The qName.
     * @throws SAXException An exception in parseing the element.
     */
    protected void _endElement(String uri, String localName, String qName) throws SAXException
    {
        // DOES NOTHING. SUPERS MAY OVERRIDE
    }

    /**
     * An overriden method of @see DefaultHandler#endElement(String, String, String).
     * Subclasses of AbstractDocumentHandler may override @see #_endElement(String, String, String)
     * instead.
     *
     * @param uri       The uri.
     * @param localName The localName.
     * @param qName     The qName.
     * @throws SAXException An exception in parseing the element.
     */
    @Override
    public final void endElement(String uri, String localName, String qName) throws SAXException
    {
        _endElement(uri, localName, qName);
        textbuffer = null;
    }

    @Override
    public final void characters(char buf[], int offset, int length) throws SAXException
    {
        String s = new String(buf, offset, length);
        if (textbuffer == null) {
            textbuffer = new StringBuffer(s);
        } else {
            textbuffer.append(s);
        }
    }
}
