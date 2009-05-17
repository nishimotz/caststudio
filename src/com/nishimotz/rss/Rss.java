/*
 * The Relaxer artifact
 * Copyright (c) 2000-2004, ASAMI Tomoharu, All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer. 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.nishimotz.rss;

import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * <b>Rss</b> is generated from rss.rng by Relaxer.
 * This class is derived from:
 * 
 * <!-- for programmer
 * <element name="rss">
 *       <attribute name="version">
 *         <data type="float"/>
 *       </attribute>
 *       <ref name="channel"/>
 *     </element>-->
 * <!-- for javadoc -->
 * <pre> &lt;element name="rss"&gt;
 *       &lt;attribute name="version"&gt;
 *         &lt;data type="float"/&gt;
 *       &lt;/attribute&gt;
 *       &lt;ref name="channel"/&gt;
 *     &lt;/element&gt;</pre>
 *
 * @version rss.rng (Sun May 17 14:40:26 JST 2009)
 * @author  Relaxer 1.1b (http://www.relaxer.org)
 */
public class Rss implements java.io.Serializable, Cloneable {
    private float version_;
    private Channel channel_;

    /**
     * Creates a <code>Rss</code>.
     *
     */
    public Rss() {
    }

    /**
     * Creates a <code>Rss</code>.
     *
     * @param source
     */
    public Rss(Rss source) {
        setup(source);
    }

    /**
     * Creates a <code>Rss</code> by the Stack <code>stack</code>
     * that contains Elements.
     * This constructor is supposed to be used internally
     * by the Relaxer system.
     *
     * @param stack
     */
    public Rss(RStack stack) {
        setup(stack);
    }

    /**
     * Creates a <code>Rss</code> by the Document <code>doc</code>.
     *
     * @param doc
     */
    public Rss(Document doc) {
        setup(doc.getDocumentElement());
    }

    /**
     * Creates a <code>Rss</code> by the Element <code>element</code>.
     *
     * @param element
     */
    public Rss(Element element) {
        setup(element);
    }

    /**
     * Creates a <code>Rss</code> by the File <code>file</code>.
     *
     * @param file
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Rss(File file) throws IOException, SAXException, ParserConfigurationException {
        setup(file);
    }

    /**
     * Creates a <code>Rss</code>
     * by the String representation of URI <code>uri</code>.
     *
     * @param uri
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Rss(String uri) throws IOException, SAXException, ParserConfigurationException {
        setup(uri);
    }

    /**
     * Creates a <code>Rss</code> by the URL <code>url</code>.
     *
     * @param url
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Rss(URL url) throws IOException, SAXException, ParserConfigurationException {
        setup(url);
    }

    /**
     * Creates a <code>Rss</code> by the InputStream <code>in</code>.
     *
     * @param in
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Rss(InputStream in) throws IOException, SAXException, ParserConfigurationException {
        setup(in);
    }

    /**
     * Creates a <code>Rss</code> by the InputSource <code>is</code>.
     *
     * @param is
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Rss(InputSource is) throws IOException, SAXException, ParserConfigurationException {
        setup(is);
    }

    /**
     * Creates a <code>Rss</code> by the Reader <code>reader</code>.
     *
     * @param reader
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Rss(Reader reader) throws IOException, SAXException, ParserConfigurationException {
        setup(reader);
    }

    /**
     * Initializes the <code>Rss</code> by the Rss <code>source</code>.
     *
     * @param source
     */
    public void setup(Rss source) {
        int size;
        version_ = source.version_;
        if (source.channel_ != null) {
            setChannel((Channel)source.getChannel().clone());
        }
    }

    /**
     * Initializes the <code>Rss</code> by the Document <code>doc</code>.
     *
     * @param doc
     */
    public void setup(Document doc) {
        setup(doc.getDocumentElement());
    }

    /**
     * Initializes the <code>Rss</code> by the Element <code>element</code>.
     *
     * @param element
     */
    public void setup(Element element) {
        init(element);
    }

    /**
     * Initializes the <code>Rss</code> by the Stack <code>stack</code>
     * that contains Elements.
     * This constructor is supposed to be used internally
     * by the Relaxer system.
     *
     * @param stack
     */
    public void setup(RStack stack) {
        init(stack.popElement());
    }

    /**
     * @param element
     */
    private void init(Element element) {
        RStack stack = new RStack(element);
        version_ = URelaxer.getAttributePropertyAsFloat(element, "version");
        setChannel(new Channel(stack));
    }

    /**
     * @return Object
     */
    public Object clone() {
        return (new Rss((Rss)this));
    }

    /**
     * Creates a DOM representation of the object.
     * Result is appended to the Node <code>parent</code>.
     *
     * @param parent
     */
    public void makeElement(Node parent) {
        Document doc;
        if (parent instanceof Document) {
            doc = (Document)parent;
        } else {
            doc = parent.getOwnerDocument();
        }
        Element element = doc.createElement("rss");
        int size;
        URelaxer.setAttributePropertyByFloat(element, "version", this.version_);
        this.channel_.makeElement(element);
        parent.appendChild(element);
    }

    /**
     * Initializes the <code>Rss</code> by the File <code>file</code>.
     *
     * @param file
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public void setup(File file) throws IOException, SAXException, ParserConfigurationException {
        setup(file.toURL());
    }

    /**
     * Initializes the <code>Rss</code>
     * by the String representation of URI <code>uri</code>.
     *
     * @param uri
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public void setup(String uri) throws IOException, SAXException, ParserConfigurationException {
        setup(UJAXP.getDocument(uri, UJAXP.FLAG_NONE));
    }

    /**
     * Initializes the <code>Rss</code> by the URL <code>url</code>.
     *
     * @param url
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public void setup(URL url) throws IOException, SAXException, ParserConfigurationException {
        setup(UJAXP.getDocument(url, UJAXP.FLAG_NONE));
    }

    /**
     * Initializes the <code>Rss</code> by the InputStream <code>in</code>.
     *
     * @param in
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public void setup(InputStream in) throws IOException, SAXException, ParserConfigurationException {
        setup(UJAXP.getDocument(in, UJAXP.FLAG_NONE));
    }

    /**
     * Initializes the <code>Rss</code> by the InputSource <code>is</code>.
     *
     * @param is
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public void setup(InputSource is) throws IOException, SAXException, ParserConfigurationException {
        setup(UJAXP.getDocument(is, UJAXP.FLAG_NONE));
    }

    /**
     * Initializes the <code>Rss</code> by the Reader <code>reader</code>.
     *
     * @param reader
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public void setup(Reader reader) throws IOException, SAXException, ParserConfigurationException {
        setup(UJAXP.getDocument(reader, UJAXP.FLAG_NONE));
    }

    /**
     * Creates a DOM document representation of the object.
     *
     * @exception ParserConfigurationException
     * @return Document
     */
    public Document makeDocument() throws ParserConfigurationException {
        Document doc = UJAXP.makeDocument();
        makeElement(doc);
        return (doc);
    }

    /**
     * Gets the float property <b>version</b>.
     *
     * @return float
     */
    public float getVersion() {
        return (version_);
    }

    /**
     * Sets the float property <b>version</b>.
     *
     * @param version
     */
    public void setVersion(float version) {
        this.version_ = version;
    }

    /**
     * Gets the float property <b>version</b>.
     *
     * @return Float
     */
    public Float getVersionAsFloat() {
        return (new Float(version_));
    }

    /**
     * Sets the float property <b>version</b>.
     *
     * @param version
     */
    public void setVersion(Float version) {
        this.version_ = version.floatValue();
    }

    /**
     * Gets the Channel property <b>channel</b>.
     *
     * @return Channel
     */
    public Channel getChannel() {
        return (channel_);
    }

    /**
     * Sets the Channel property <b>channel</b>.
     *
     * @param channel
     */
    public void setChannel(Channel channel) {
        this.channel_ = channel;
    }

    /**
     * Makes an XML text representation.
     *
     * @return String
     */
    public String makeTextDocument() {
        StringBuffer buffer = new StringBuffer();
        makeTextElement(buffer);
        return (new String(buffer));
    }

    /**
     * Makes an XML text representation.
     *
     * @param buffer
     */
    public void makeTextElement(StringBuffer buffer) {
        int size;
        buffer.append("<rss");
        buffer.append(" version=\"");
        buffer.append(URelaxer.getString(getVersion()));
        buffer.append("\"");
        buffer.append(">");
        channel_.makeTextElement(buffer);
        buffer.append("</rss>");
    }

    /**
     * Makes an XML text representation.
     *
     * @param buffer
     * @exception IOException
     */
    public void makeTextElement(Writer buffer) throws IOException {
        int size;
        buffer.write("<rss");
        buffer.write(" version=\"");
        buffer.write(URelaxer.getString(getVersion()));
        buffer.write("\"");
        buffer.write(">");
        channel_.makeTextElement(buffer);
        buffer.write("</rss>");
    }

    /**
     * Makes an XML text representation.
     *
     * @param buffer
     */
    public void makeTextElement(PrintWriter buffer) {
        int size;
        buffer.print("<rss");
        buffer.print(" version=\"");
        buffer.print(URelaxer.getString(getVersion()));
        buffer.print("\"");
        buffer.print(">");
        channel_.makeTextElement(buffer);
        buffer.print("</rss>");
    }

    /**
     * Makes an XML text representation.
     *
     * @param buffer
     */
    public void makeTextAttribute(StringBuffer buffer) {
    }

    /**
     * Makes an XML text representation.
     *
     * @param buffer
     * @exception IOException
     */
    public void makeTextAttribute(Writer buffer) throws IOException {
    }

    /**
     * Makes an XML text representation.
     *
     * @param buffer
     */
    public void makeTextAttribute(PrintWriter buffer) {
    }

    /**
     * Gets the property value as String.
     *
     * @return String
     */
    public String getVersionAsString() {
        return (URelaxer.getString(getVersion()));
    }

    /**
     * Sets the property value by String.
     *
     * @param string
     */
    public void setVersionByString(String string) {
        setVersion(Float.parseFloat(string));
    }

    /**
     * Returns a String representation of this object.
     * While this method informs as XML format representaion, 
     *  it's purpose is just information, not making 
     * a rigid XML documentation.
     *
     * @return String
     */
    public String toString() {
        try {
            return (makeTextDocument());
        } catch (Exception e) {
            return (super.toString());
        }
    }

    /**
     * Gets attribute.
     *
     * @return String
     */
    public String getChannel_Title() {
        if (channel_ == null) {
            return (null);
        }
        return (channel_.getTitle());
    }

    /**
     * Gets attribute.
     *
     * @param title
     */
    public void setChannel_Title(String title) {
        if (channel_ == null) {
            channel_ = new Channel();
        }
        channel_.setTitle(title);
    }

    /**
     * Gets attribute.
     *
     * @return String
     */
    public String getChannel_Link() {
        if (channel_ == null) {
            return (null);
        }
        return (channel_.getLink());
    }

    /**
     * Gets attribute.
     *
     * @param link
     */
    public void setChannel_Link(String link) {
        if (channel_ == null) {
            channel_ = new Channel();
        }
        channel_.setLink(link);
    }

    /**
     * Gets attribute.
     *
     * @return String
     */
    public String getChannel_Description() {
        if (channel_ == null) {
            return (null);
        }
        return (channel_.getDescription());
    }

    /**
     * Gets attribute.
     *
     * @param description
     */
    public void setChannel_Description(String description) {
        if (channel_ == null) {
            channel_ = new Channel();
        }
        channel_.setDescription(description);
    }

    /**
     * Gets attribute.
     *
     * @return String
     */
    public String getChannel_Language() {
        if (channel_ == null) {
            return (null);
        }
        return (channel_.getLanguage());
    }

    /**
     * Gets attribute.
     *
     * @param language
     */
    public void setChannel_Language(String language) {
        if (channel_ == null) {
            channel_ = new Channel();
        }
        channel_.setLanguage(language);
    }

    /**
     * Gets attribute.
     *
     * @return String
     */
    public String getChannel_PubDate() {
        if (channel_ == null) {
            return (null);
        }
        return (channel_.getPubDate());
    }

    /**
     * Gets attribute.
     *
     * @param pubDate
     */
    public void setChannel_PubDate(String pubDate) {
        if (channel_ == null) {
            channel_ = new Channel();
        }
        channel_.setPubDate(pubDate);
    }

    /**
     * Gets attribute.
     *
     * @return String
     */
    public String getChannel_Category() {
        if (channel_ == null) {
            return (null);
        }
        return (channel_.getCategory());
    }

    /**
     * Gets attribute.
     *
     * @param category
     */
    public void setChannel_Category(String category) {
        if (channel_ == null) {
            channel_ = new Channel();
        }
        channel_.setCategory(category);
    }

    /**
     * Gets attribute.
     *
     * @return String
     */
    public String getChannel_Docs() {
        if (channel_ == null) {
            return (null);
        }
        return (channel_.getDocs());
    }

    /**
     * Gets attribute.
     *
     * @param docs
     */
    public void setChannel_Docs(String docs) {
        if (channel_ == null) {
            channel_ = new Channel();
        }
        channel_.setDocs(docs);
    }

    /**
     * Gets attribute.
     *
     * @return String
     */
    public String getChannel_Ttl() {
        if (channel_ == null) {
            return (null);
        }
        return (channel_.getTtl());
    }

    /**
     * Gets attribute.
     *
     * @param ttl
     */
    public void setChannel_Ttl(String ttl) {
        if (channel_ == null) {
            channel_ = new Channel();
        }
        channel_.setTtl(ttl);
    }

    /**
     * Tests if a Element <code>element</code> is valid
     * for the <code>Rss</code>.
     *
     * @param element
     * @return boolean
     */
    public static boolean isMatch(Element element) {
        if (!URelaxer.isTargetElement(element, "rss")) {
            return (false);
        }
        RStack target = new RStack(element);
        boolean $match$ = false;
        Element child;
        if (!URelaxer.hasAttributeHungry(target, "version")) {
            return (false);
        }
        $match$ = true;
        if (!Channel.isMatchHungry(target)) {
            return (false);
        }
        $match$ = true;
        if (!target.isEmptyElement()) {
            return (false);
        }
        return (true);
    }

    /**
     * Tests if elements contained in a Stack <code>stack</code>
     * is valid for the <code>Rss</code>.
     * This mehtod is supposed to be used internally
     * by the Relaxer system.
     *
     * @param stack
     * @return boolean
     */
    public static boolean isMatch(RStack stack) {
        Element element = stack.peekElement();
        if (element == null) {
            return (false);
        }
        return (isMatch(element));
    }

    /**
     * Tests if elements contained in a Stack <code>stack</code>
     * is valid for the <code>Rss</code>.
     * This method consumes the stack contents during matching operation.
     * This mehtod is supposed to be used internally
     * by the Relaxer system.
     *
     * @param stack
     * @return boolean
     */
    public static boolean isMatchHungry(RStack stack) {
        Element element = stack.peekElement();
        if (element == null) {
            return (false);
        }
        if (isMatch(element)) {
            stack.popElement();
            return (true);
        } else {
            return (false);
        }
    }
}
