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
 * <b>ItemGuid</b> is generated from rss.rng by Relaxer.
 * This class is derived from:
 * 
 * <!-- for programmer
 * <element name="guid">
 *             <optional>
 *               <attribute name="isPermaLink">
 *                 <data type="token"/>
 *               </attribute>
 *             </optional>
 *             <data type="token"/>
 *           </element>-->
 * <!-- for javadoc -->
 * <pre> &lt;element name="guid"&gt;
 *             &lt;optional&gt;
 *               &lt;attribute name="isPermaLink"&gt;
 *                 &lt;data type="token"/&gt;
 *               &lt;/attribute&gt;
 *             &lt;/optional&gt;
 *             &lt;data type="token"/&gt;
 *           &lt;/element&gt;</pre>
 *
 * @version rss.rng (Sun May 17 15:22:04 JST 2009)
 * @author  Relaxer 1.1b (http://www.relaxer.org)
 */
public class ItemGuid implements java.io.Serializable, Cloneable, IItemChoice {
    private String content_;
    private String isPermaLink_;

    /**
     * Creates a <code>ItemGuid</code>.
     *
     */
    public ItemGuid() {
    }

    /**
     * Creates a <code>ItemGuid</code>.
     *
     * @param source
     */
    public ItemGuid(ItemGuid source) {
        setup(source);
    }

    /**
     * Creates a <code>ItemGuid</code> by the Stack <code>stack</code>
     * that contains Elements.
     * This constructor is supposed to be used internally
     * by the Relaxer system.
     *
     * @param stack
     */
    public ItemGuid(RStack stack) {
        setup(stack);
    }

    /**
     * Creates a <code>ItemGuid</code> by the Document <code>doc</code>.
     *
     * @param doc
     */
    public ItemGuid(Document doc) {
        setup(doc.getDocumentElement());
    }

    /**
     * Creates a <code>ItemGuid</code> by the Element <code>element</code>.
     *
     * @param element
     */
    public ItemGuid(Element element) {
        setup(element);
    }

    /**
     * Creates a <code>ItemGuid</code> by the File <code>file</code>.
     *
     * @param file
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public ItemGuid(File file) throws IOException, SAXException, ParserConfigurationException {
        setup(file);
    }

    /**
     * Creates a <code>ItemGuid</code>
     * by the String representation of URI <code>uri</code>.
     *
     * @param uri
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public ItemGuid(String uri) throws IOException, SAXException, ParserConfigurationException {
        setup(uri);
    }

    /**
     * Creates a <code>ItemGuid</code> by the URL <code>url</code>.
     *
     * @param url
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public ItemGuid(URL url) throws IOException, SAXException, ParserConfigurationException {
        setup(url);
    }

    /**
     * Creates a <code>ItemGuid</code> by the InputStream <code>in</code>.
     *
     * @param in
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public ItemGuid(InputStream in) throws IOException, SAXException, ParserConfigurationException {
        setup(in);
    }

    /**
     * Creates a <code>ItemGuid</code> by the InputSource <code>is</code>.
     *
     * @param is
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public ItemGuid(InputSource is) throws IOException, SAXException, ParserConfigurationException {
        setup(is);
    }

    /**
     * Creates a <code>ItemGuid</code> by the Reader <code>reader</code>.
     *
     * @param reader
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public ItemGuid(Reader reader) throws IOException, SAXException, ParserConfigurationException {
        setup(reader);
    }

    /**
     * Initializes the <code>ItemGuid</code> by the ItemGuid <code>source</code>.
     *
     * @param source
     */
    public void setup(ItemGuid source) {
        int size;
        content_ = source.content_;
        isPermaLink_ = source.isPermaLink_;
    }

    /**
     * Initializes the <code>ItemGuid</code> by the Document <code>doc</code>.
     *
     * @param doc
     */
    public void setup(Document doc) {
        setup(doc.getDocumentElement());
    }

    /**
     * Initializes the <code>ItemGuid</code> by the Element <code>element</code>.
     *
     * @param element
     */
    public void setup(Element element) {
        init(element);
    }

    /**
     * Initializes the <code>ItemGuid</code> by the Stack <code>stack</code>
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
        content_ = URelaxer.getElementPropertyAsString(element);
        isPermaLink_ = URelaxer.getAttributePropertyAsString(element, "isPermaLink");
    }

    /**
     * @return Object
     */
    public Object clone() {
        return (new ItemGuid((ItemGuid)this));
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
        Element element = doc.createElement("guid");
        URelaxer.setElementPropertyByString(element, this.content_);
        int size;
        if (this.isPermaLink_ != null) {
            URelaxer.setAttributePropertyByString(element, "isPermaLink", this.isPermaLink_);
        }
        parent.appendChild(element);
    }

    /**
     * Initializes the <code>ItemGuid</code> by the File <code>file</code>.
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
     * Initializes the <code>ItemGuid</code>
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
     * Initializes the <code>ItemGuid</code> by the URL <code>url</code>.
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
     * Initializes the <code>ItemGuid</code> by the InputStream <code>in</code>.
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
     * Initializes the <code>ItemGuid</code> by the InputSource <code>is</code>.
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
     * Initializes the <code>ItemGuid</code> by the Reader <code>reader</code>.
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
     * Gets the String property <b>content</b>.
     *
     * @return String
     */
    public String getContent() {
        return (content_);
    }

    /**
     * Sets the String property <b>content</b>.
     *
     * @param content
     */
    public void setContent(String content) {
        this.content_ = content;
    }

    /**
     * Gets the String property <b>isPermaLink</b>.
     *
     * @return String
     */
    public String getIsPermaLink() {
        return (isPermaLink_);
    }

    /**
     * Sets the String property <b>isPermaLink</b>.
     *
     * @param isPermaLink
     */
    public void setIsPermaLink(String isPermaLink) {
        this.isPermaLink_ = isPermaLink;
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
        buffer.append("<guid");
        if (isPermaLink_ != null) {
            buffer.append(" isPermaLink=\"");
            buffer.append(URelaxer.escapeAttrQuot(URelaxer.getString(getIsPermaLink())));
            buffer.append("\"");
        }
        buffer.append(">");
        buffer.append(URelaxer.escapeCharData(URelaxer.getString(getContent())));
        buffer.append("</guid>");
    }

    /**
     * Makes an XML text representation.
     *
     * @param buffer
     * @exception IOException
     */
    public void makeTextElement(Writer buffer) throws IOException {
        int size;
        buffer.write("<guid");
        if (isPermaLink_ != null) {
            buffer.write(" isPermaLink=\"");
            buffer.write(URelaxer.escapeAttrQuot(URelaxer.getString(getIsPermaLink())));
            buffer.write("\"");
        }
        buffer.write(">");
        buffer.write(URelaxer.escapeCharData(URelaxer.getString(getContent())));
        buffer.write("</guid>");
    }

    /**
     * Makes an XML text representation.
     *
     * @param buffer
     */
    public void makeTextElement(PrintWriter buffer) {
        int size;
        buffer.print("<guid");
        if (isPermaLink_ != null) {
            buffer.print(" isPermaLink=\"");
            buffer.print(URelaxer.escapeAttrQuot(URelaxer.getString(getIsPermaLink())));
            buffer.print("\"");
        }
        buffer.print(">");
        buffer.print(URelaxer.escapeCharData(URelaxer.getString(getContent())));
        buffer.print("</guid>");
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
    public String getContentAsString() {
        return (URelaxer.getString(getContent()));
    }

    /**
     * Gets the property value as String.
     *
     * @return String
     */
    public String getIsPermaLinkAsString() {
        return (URelaxer.getString(getIsPermaLink()));
    }

    /**
     * Sets the property value by String.
     *
     * @param string
     */
    public void setContentByString(String string) {
        setContent(string);
    }

    /**
     * Sets the property value by String.
     *
     * @param string
     */
    public void setIsPermaLinkByString(String string) {
        setIsPermaLink(string);
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
     * Tests if a Element <code>element</code> is valid
     * for the <code>ItemGuid</code>.
     *
     * @param element
     * @return boolean
     */
    public static boolean isMatch(Element element) {
        if (!URelaxer.isTargetElement(element, "guid")) {
            return (false);
        }
        RStack target = new RStack(element);
        boolean $match$ = false;
        Element child;
        if (!target.isEmptyElement()) {
            return (false);
        }
        return (true);
    }

    /**
     * Tests if elements contained in a Stack <code>stack</code>
     * is valid for the <code>ItemGuid</code>.
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
     * is valid for the <code>ItemGuid</code>.
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
