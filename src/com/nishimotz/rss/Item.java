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
 * <b>Item</b> is generated from rss.rng by Relaxer.
 * This class is derived from:
 * 
 * <!-- for programmer
 * <element name="item">
 *       <zeroOrMore>
 *         <choice>
 * 	      <element name="title">
 * 	        <data type="token"/>
 * 	      </element>
 * 	      <element name="link">
 * 	        <data type="token"/>
 * 	      </element>
 * 	      <element name="pubDate">
 * 	        <data type="token"/>
 * 	      </element>
 * 	      <element name="author">
 * 	        <data type="token"/>
 * 	      </element>
 * 	      <element name="category">
 * 	        <data type="token"/>
 * 	      </element>
 * 		  <element name="description">
 * 		    <data type="token"/>
 * 		  </element>
 * 	      <ref name="enclosure"/>
 *         </choice>
 *       </zeroOrMore>
 *     </element>-->
 * <!-- for javadoc -->
 * <pre> &lt;element name="item"&gt;
 *       &lt;zeroOrMore&gt;
 *         &lt;choice&gt;
 * 	      &lt;element name="title"&gt;
 * 	        &lt;data type="token"/&gt;
 * 	      &lt;/element&gt;
 * 	      &lt;element name="link"&gt;
 * 	        &lt;data type="token"/&gt;
 * 	      &lt;/element&gt;
 * 	      &lt;element name="pubDate"&gt;
 * 	        &lt;data type="token"/&gt;
 * 	      &lt;/element&gt;
 * 	      &lt;element name="author"&gt;
 * 	        &lt;data type="token"/&gt;
 * 	      &lt;/element&gt;
 * 	      &lt;element name="category"&gt;
 * 	        &lt;data type="token"/&gt;
 * 	      &lt;/element&gt;
 * 		  &lt;element name="description"&gt;
 * 		    &lt;data type="token"/&gt;
 * 		  &lt;/element&gt;
 * 	      &lt;ref name="enclosure"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/zeroOrMore&gt;
 *     &lt;/element&gt;</pre>
 *
 * @version rss.rng (Sat Jan 21 16:05:54 JST 2006)
 * @author  Relaxer 1.1b (http://www.relaxer.org)
 */
public class Item implements java.io.Serializable, Cloneable {
    // List<IItemChoice>
    private java.util.List content_ = new java.util.ArrayList();

    /**
     * Creates a <code>Item</code>.
     *
     */
    public Item() {
    }

    /**
     * Creates a <code>Item</code>.
     *
     * @param source
     */
    public Item(Item source) {
        setup(source);
    }

    /**
     * Creates a <code>Item</code> by the Stack <code>stack</code>
     * that contains Elements.
     * This constructor is supposed to be used internally
     * by the Relaxer system.
     *
     * @param stack
     */
    public Item(RStack stack) {
        setup(stack);
    }

    /**
     * Creates a <code>Item</code> by the Document <code>doc</code>.
     *
     * @param doc
     */
    public Item(Document doc) {
        setup(doc.getDocumentElement());
    }

    /**
     * Creates a <code>Item</code> by the Element <code>element</code>.
     *
     * @param element
     */
    public Item(Element element) {
        setup(element);
    }

    /**
     * Creates a <code>Item</code> by the File <code>file</code>.
     *
     * @param file
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Item(File file) throws IOException, SAXException, ParserConfigurationException {
        setup(file);
    }

    /**
     * Creates a <code>Item</code>
     * by the String representation of URI <code>uri</code>.
     *
     * @param uri
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Item(String uri) throws IOException, SAXException, ParserConfigurationException {
        setup(uri);
    }

    /**
     * Creates a <code>Item</code> by the URL <code>url</code>.
     *
     * @param url
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Item(URL url) throws IOException, SAXException, ParserConfigurationException {
        setup(url);
    }

    /**
     * Creates a <code>Item</code> by the InputStream <code>in</code>.
     *
     * @param in
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Item(InputStream in) throws IOException, SAXException, ParserConfigurationException {
        setup(in);
    }

    /**
     * Creates a <code>Item</code> by the InputSource <code>is</code>.
     *
     * @param is
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Item(InputSource is) throws IOException, SAXException, ParserConfigurationException {
        setup(is);
    }

    /**
     * Creates a <code>Item</code> by the Reader <code>reader</code>.
     *
     * @param reader
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Item(Reader reader) throws IOException, SAXException, ParserConfigurationException {
        setup(reader);
    }

    /**
     * Initializes the <code>Item</code> by the Item <code>source</code>.
     *
     * @param source
     */
    public void setup(Item source) {
        int size;
        this.content_.clear();
        size = source.content_.size();
        for (int i = 0;i < size;i++) {
            addContent((IItemChoice)source.getContent(i).clone());
        }
    }

    /**
     * Initializes the <code>Item</code> by the Document <code>doc</code>.
     *
     * @param doc
     */
    public void setup(Document doc) {
        setup(doc.getDocumentElement());
    }

    /**
     * Initializes the <code>Item</code> by the Element <code>element</code>.
     *
     * @param element
     */
    public void setup(Element element) {
        init(element);
    }

    /**
     * Initializes the <code>Item</code> by the Stack <code>stack</code>
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
        content_.clear();
        while (true) {
            if (Enclosure.isMatch(stack)) {
                addContent(new Enclosure(stack));
            } else if (ItemTitle.isMatch(stack)) {
                addContent(new ItemTitle(stack));
            } else if (ItemLink.isMatch(stack)) {
                addContent(new ItemLink(stack));
            } else if (ItemPubDate.isMatch(stack)) {
                addContent(new ItemPubDate(stack));
            } else if (ItemAuthor.isMatch(stack)) {
                addContent(new ItemAuthor(stack));
            } else if (ItemCategory.isMatch(stack)) {
                addContent(new ItemCategory(stack));
            } else if (ItemDescription.isMatch(stack)) {
                addContent(new ItemDescription(stack));
            } else {
                break;
            }
        }
    }

    /**
     * @return Object
     */
    public Object clone() {
        return (new Item((Item)this));
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
        Element element = doc.createElement("item");
        int size;
        size = this.content_.size();
        for (int i = 0;i < size;i++) {
            IItemChoice value = (IItemChoice)this.content_.get(i);
            value.makeElement(element);
        }
        parent.appendChild(element);
    }

    /**
     * Initializes the <code>Item</code> by the File <code>file</code>.
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
     * Initializes the <code>Item</code>
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
     * Initializes the <code>Item</code> by the URL <code>url</code>.
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
     * Initializes the <code>Item</code> by the InputStream <code>in</code>.
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
     * Initializes the <code>Item</code> by the InputSource <code>is</code>.
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
     * Initializes the <code>Item</code> by the Reader <code>reader</code>.
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
     * Gets the IItemChoice property <b>content</b>.
     *
     * @return IItemChoice[]
     */
    public IItemChoice[] getContent() {
        IItemChoice[] array = new IItemChoice[content_.size()];
        return ((IItemChoice[])content_.toArray(array));
    }

    /**
     * Sets the IItemChoice property <b>content</b>.
     *
     * @param content
     */
    public void setContent(IItemChoice[] content) {
        this.content_.clear();
        for (int i = 0;i < content.length;i++) {
            addContent(content[i]);
        }
    }

    /**
     * Sets the IItemChoice property <b>content</b>.
     *
     * @param content
     */
    public void setContent(IItemChoice content) {
        this.content_.clear();
        addContent(content);
    }

    /**
     * Adds the IItemChoice property <b>content</b>.
     *
     * @param content
     */
    public void addContent(IItemChoice content) {
        this.content_.add(content);
    }

    /**
     * Adds the IItemChoice property <b>content</b>.
     *
     * @param content
     */
    public void addContent(IItemChoice[] content) {
        for (int i = 0;i < content.length;i++) {
            addContent(content[i]);
        }
    }

    /**
     * Gets number of the IItemChoice property <b>content</b>.
     *
     * @return int
     */
    public int sizeContent() {
        return (content_.size());
    }

    /**
     * Gets the IItemChoice property <b>content</b> by index.
     *
     * @param index
     * @return IItemChoice
     */
    public IItemChoice getContent(int index) {
        return ((IItemChoice)content_.get(index));
    }

    /**
     * Sets the IItemChoice property <b>content</b> by index.
     *
     * @param index
     * @param content
     */
    public void setContent(int index, IItemChoice content) {
        this.content_.set(index, content);
    }

    /**
     * Adds the IItemChoice property <b>content</b> by index.
     *
     * @param index
     * @param content
     */
    public void addContent(int index, IItemChoice content) {
        this.content_.add(index, content);
    }

    /**
     * Remove the IItemChoice property <b>content</b> by index.
     *
     * @param index
     */
    public void removeContent(int index) {
        this.content_.remove(index);
    }

    /**
     * Remove the IItemChoice property <b>content</b> by object.
     *
     * @param content
     */
    public void removeContent(IItemChoice content) {
        this.content_.remove(content);
    }

    /**
     * Clear the IItemChoice property <b>content</b>.
     *
     */
    public void clearContent() {
        this.content_.clear();
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
        buffer.append("<item");
        size = this.content_.size();
        for (int i = 0;i < size;i++) {
            IItemChoice value = (IItemChoice)this.content_.get(i);
            value.makeTextAttribute(buffer);
        }
        buffer.append(">");
        size = this.content_.size();
        for (int i = 0;i < size;i++) {
            IItemChoice value = (IItemChoice)this.content_.get(i);
            value.makeTextElement(buffer);
        }
        buffer.append("</item>");
    }

    /**
     * Makes an XML text representation.
     *
     * @param buffer
     * @exception IOException
     */
    public void makeTextElement(Writer buffer) throws IOException {
        int size;
        buffer.write("<item");
        size = this.content_.size();
        for (int i = 0;i < size;i++) {
            IItemChoice value = (IItemChoice)this.content_.get(i);
            value.makeTextAttribute(buffer);
        }
        buffer.write(">");
        size = this.content_.size();
        for (int i = 0;i < size;i++) {
            IItemChoice value = (IItemChoice)this.content_.get(i);
            value.makeTextElement(buffer);
        }
        buffer.write("</item>");
    }

    /**
     * Makes an XML text representation.
     *
     * @param buffer
     */
    public void makeTextElement(PrintWriter buffer) {
        int size;
        buffer.print("<item");
        size = this.content_.size();
        for (int i = 0;i < size;i++) {
            IItemChoice value = (IItemChoice)this.content_.get(i);
            value.makeTextAttribute(buffer);
        }
        buffer.print(">");
        size = this.content_.size();
        for (int i = 0;i < size;i++) {
            IItemChoice value = (IItemChoice)this.content_.get(i);
            value.makeTextElement(buffer);
        }
        buffer.print("</item>");
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
     * for the <code>Item</code>.
     *
     * @param element
     * @return boolean
     */
    public static boolean isMatch(Element element) {
        if (!URelaxer.isTargetElement(element, "item")) {
            return (false);
        }
        RStack target = new RStack(element);
        boolean $match$ = false;
        Element child;
        while (true) {
            if (Enclosure.isMatchHungry(target)) {
                $match$ = true;
            } else if (ItemTitle.isMatchHungry(target)) {
                $match$ = true;
            } else if (ItemLink.isMatchHungry(target)) {
                $match$ = true;
            } else if (ItemPubDate.isMatchHungry(target)) {
                $match$ = true;
            } else if (ItemAuthor.isMatchHungry(target)) {
                $match$ = true;
            } else if (ItemCategory.isMatchHungry(target)) {
                $match$ = true;
            } else if (ItemDescription.isMatchHungry(target)) {
                $match$ = true;
            } else {
                break;
            }
        }
        if (!target.isEmptyElement()) {
            return (false);
        }
        return (true);
    }

    /**
     * Tests if elements contained in a Stack <code>stack</code>
     * is valid for the <code>Item</code>.
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
     * is valid for the <code>Item</code>.
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
