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
 * <b>Channel</b> is generated from rss.rng by Relaxer.
 * This class is derived from:
 * 
 * <!-- for programmer
 * <element name="channel">
 *       <element name="title">
 *         <data type="token"/>
 *       </element>
 *       <element name="link">
 *         <data type="token"/>
 *       </element>
 * 	  <element name="description">
 * 	    <data type="token"/>
 * 	  </element>
 *       <element name="language">
 *         <data type="token"/>
 *       </element>
 *       <element name="pubDate">
 *         <data type="token"/>
 *       </element>
 *       <element name="category">
 *         <data type="token"/>
 *       </element>
 *       <element name="docs">
 *         <data type="token"/>
 *       </element>
 *       <element name="ttl">
 *         <data type="token"/>
 *       </element>
 *       <zeroOrMore>
 *         <ref name="item"/>
 *       </zeroOrMore>
 *     </element>-->
 * <!-- for javadoc -->
 * <pre> &lt;element name="channel"&gt;
 *       &lt;element name="title"&gt;
 *         &lt;data type="token"/&gt;
 *       &lt;/element&gt;
 *       &lt;element name="link"&gt;
 *         &lt;data type="token"/&gt;
 *       &lt;/element&gt;
 * 	  &lt;element name="description"&gt;
 * 	    &lt;data type="token"/&gt;
 * 	  &lt;/element&gt;
 *       &lt;element name="language"&gt;
 *         &lt;data type="token"/&gt;
 *       &lt;/element&gt;
 *       &lt;element name="pubDate"&gt;
 *         &lt;data type="token"/&gt;
 *       &lt;/element&gt;
 *       &lt;element name="category"&gt;
 *         &lt;data type="token"/&gt;
 *       &lt;/element&gt;
 *       &lt;element name="docs"&gt;
 *         &lt;data type="token"/&gt;
 *       &lt;/element&gt;
 *       &lt;element name="ttl"&gt;
 *         &lt;data type="token"/&gt;
 *       &lt;/element&gt;
 *       &lt;zeroOrMore&gt;
 *         &lt;ref name="item"/&gt;
 *       &lt;/zeroOrMore&gt;
 *     &lt;/element&gt;</pre>
 *
 * @version rss.rng (Sun May 17 15:22:04 JST 2009)
 * @author  Relaxer 1.1b (http://www.relaxer.org)
 */
public class Channel implements java.io.Serializable, Cloneable {
    private String title_;
    private String link_;
    private String description_;
    private String language_;
    private String pubDate_;
    private String category_;
    private String docs_;
    private String ttl_;
    // List<Item>
    private java.util.List item_ = new java.util.ArrayList();

    /**
     * Creates a <code>Channel</code>.
     *
     */
    public Channel() {
        title_ = "";
        link_ = "";
        description_ = "";
        language_ = "";
        pubDate_ = "";
        category_ = "";
        docs_ = "";
        ttl_ = "";
    }

    /**
     * Creates a <code>Channel</code>.
     *
     * @param source
     */
    public Channel(Channel source) {
        setup(source);
    }

    /**
     * Creates a <code>Channel</code> by the Stack <code>stack</code>
     * that contains Elements.
     * This constructor is supposed to be used internally
     * by the Relaxer system.
     *
     * @param stack
     */
    public Channel(RStack stack) {
        setup(stack);
    }

    /**
     * Creates a <code>Channel</code> by the Document <code>doc</code>.
     *
     * @param doc
     */
    public Channel(Document doc) {
        setup(doc.getDocumentElement());
    }

    /**
     * Creates a <code>Channel</code> by the Element <code>element</code>.
     *
     * @param element
     */
    public Channel(Element element) {
        setup(element);
    }

    /**
     * Creates a <code>Channel</code> by the File <code>file</code>.
     *
     * @param file
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Channel(File file) throws IOException, SAXException, ParserConfigurationException {
        setup(file);
    }

    /**
     * Creates a <code>Channel</code>
     * by the String representation of URI <code>uri</code>.
     *
     * @param uri
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Channel(String uri) throws IOException, SAXException, ParserConfigurationException {
        setup(uri);
    }

    /**
     * Creates a <code>Channel</code> by the URL <code>url</code>.
     *
     * @param url
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Channel(URL url) throws IOException, SAXException, ParserConfigurationException {
        setup(url);
    }

    /**
     * Creates a <code>Channel</code> by the InputStream <code>in</code>.
     *
     * @param in
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Channel(InputStream in) throws IOException, SAXException, ParserConfigurationException {
        setup(in);
    }

    /**
     * Creates a <code>Channel</code> by the InputSource <code>is</code>.
     *
     * @param is
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Channel(InputSource is) throws IOException, SAXException, ParserConfigurationException {
        setup(is);
    }

    /**
     * Creates a <code>Channel</code> by the Reader <code>reader</code>.
     *
     * @param reader
     * @exception IOException
     * @exception SAXException
     * @exception ParserConfigurationException
     */
    public Channel(Reader reader) throws IOException, SAXException, ParserConfigurationException {
        setup(reader);
    }

    /**
     * Initializes the <code>Channel</code> by the Channel <code>source</code>.
     *
     * @param source
     */
    public void setup(Channel source) {
        int size;
        title_ = source.title_;
        link_ = source.link_;
        description_ = source.description_;
        language_ = source.language_;
        pubDate_ = source.pubDate_;
        category_ = source.category_;
        docs_ = source.docs_;
        ttl_ = source.ttl_;
        this.item_.clear();
        size = source.item_.size();
        for (int i = 0;i < size;i++) {
            addItem((Item)source.getItem(i).clone());
        }
    }

    /**
     * Initializes the <code>Channel</code> by the Document <code>doc</code>.
     *
     * @param doc
     */
    public void setup(Document doc) {
        setup(doc.getDocumentElement());
    }

    /**
     * Initializes the <code>Channel</code> by the Element <code>element</code>.
     *
     * @param element
     */
    public void setup(Element element) {
        init(element);
    }

    /**
     * Initializes the <code>Channel</code> by the Stack <code>stack</code>
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
        title_ = URelaxer.getElementPropertyAsString(stack.popElement());
        link_ = URelaxer.getElementPropertyAsString(stack.popElement());
        description_ = URelaxer.getElementPropertyAsString(stack.popElement());
        language_ = URelaxer.getElementPropertyAsString(stack.popElement());
        pubDate_ = URelaxer.getElementPropertyAsString(stack.popElement());
        category_ = URelaxer.getElementPropertyAsString(stack.popElement());
        docs_ = URelaxer.getElementPropertyAsString(stack.popElement());
        ttl_ = URelaxer.getElementPropertyAsString(stack.popElement());
        item_.clear();
        while (true) {
            if (Item.isMatch(stack)) {
                addItem(new Item(stack));
            } else {
                break;
            }
        }
    }

    /**
     * @return Object
     */
    public Object clone() {
        return (new Channel((Channel)this));
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
        Element element = doc.createElement("channel");
        int size;
        URelaxer.setElementPropertyByString(element, "title", this.title_);
        URelaxer.setElementPropertyByString(element, "link", this.link_);
        URelaxer.setElementPropertyByString(element, "description", this.description_);
        URelaxer.setElementPropertyByString(element, "language", this.language_);
        URelaxer.setElementPropertyByString(element, "pubDate", this.pubDate_);
        URelaxer.setElementPropertyByString(element, "category", this.category_);
        URelaxer.setElementPropertyByString(element, "docs", this.docs_);
        URelaxer.setElementPropertyByString(element, "ttl", this.ttl_);
        size = this.item_.size();
        for (int i = 0;i < size;i++) {
            Item value = (Item)this.item_.get(i);
            value.makeElement(element);
        }
        parent.appendChild(element);
    }

    /**
     * Initializes the <code>Channel</code> by the File <code>file</code>.
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
     * Initializes the <code>Channel</code>
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
     * Initializes the <code>Channel</code> by the URL <code>url</code>.
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
     * Initializes the <code>Channel</code> by the InputStream <code>in</code>.
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
     * Initializes the <code>Channel</code> by the InputSource <code>is</code>.
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
     * Initializes the <code>Channel</code> by the Reader <code>reader</code>.
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
     * Gets the String property <b>title</b>.
     *
     * @return String
     */
    public String getTitle() {
        return (title_);
    }

    /**
     * Sets the String property <b>title</b>.
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title_ = title;
    }

    /**
     * Gets the String property <b>link</b>.
     *
     * @return String
     */
    public String getLink() {
        return (link_);
    }

    /**
     * Sets the String property <b>link</b>.
     *
     * @param link
     */
    public void setLink(String link) {
        this.link_ = link;
    }

    /**
     * Gets the String property <b>description</b>.
     *
     * @return String
     */
    public String getDescription() {
        return (description_);
    }

    /**
     * Sets the String property <b>description</b>.
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description_ = description;
    }

    /**
     * Gets the String property <b>language</b>.
     *
     * @return String
     */
    public String getLanguage() {
        return (language_);
    }

    /**
     * Sets the String property <b>language</b>.
     *
     * @param language
     */
    public void setLanguage(String language) {
        this.language_ = language;
    }

    /**
     * Gets the String property <b>pubDate</b>.
     *
     * @return String
     */
    public String getPubDate() {
        return (pubDate_);
    }

    /**
     * Sets the String property <b>pubDate</b>.
     *
     * @param pubDate
     */
    public void setPubDate(String pubDate) {
        this.pubDate_ = pubDate;
    }

    /**
     * Gets the String property <b>category</b>.
     *
     * @return String
     */
    public String getCategory() {
        return (category_);
    }

    /**
     * Sets the String property <b>category</b>.
     *
     * @param category
     */
    public void setCategory(String category) {
        this.category_ = category;
    }

    /**
     * Gets the String property <b>docs</b>.
     *
     * @return String
     */
    public String getDocs() {
        return (docs_);
    }

    /**
     * Sets the String property <b>docs</b>.
     *
     * @param docs
     */
    public void setDocs(String docs) {
        this.docs_ = docs;
    }

    /**
     * Gets the String property <b>ttl</b>.
     *
     * @return String
     */
    public String getTtl() {
        return (ttl_);
    }

    /**
     * Sets the String property <b>ttl</b>.
     *
     * @param ttl
     */
    public void setTtl(String ttl) {
        this.ttl_ = ttl;
    }

    /**
     * Gets the Item property <b>item</b>.
     *
     * @return Item[]
     */
    public Item[] getItem() {
        Item[] array = new Item[item_.size()];
        return ((Item[])item_.toArray(array));
    }

    /**
     * Sets the Item property <b>item</b>.
     *
     * @param item
     */
    public void setItem(Item[] item) {
        this.item_.clear();
        for (int i = 0;i < item.length;i++) {
            addItem(item[i]);
        }
    }

    /**
     * Sets the Item property <b>item</b>.
     *
     * @param item
     */
    public void setItem(Item item) {
        this.item_.clear();
        addItem(item);
    }

    /**
     * Adds the Item property <b>item</b>.
     *
     * @param item
     */
    public void addItem(Item item) {
        this.item_.add(item);
    }

    /**
     * Adds the Item property <b>item</b>.
     *
     * @param item
     */
    public void addItem(Item[] item) {
        for (int i = 0;i < item.length;i++) {
            addItem(item[i]);
        }
    }

    /**
     * Gets number of the Item property <b>item</b>.
     *
     * @return int
     */
    public int sizeItem() {
        return (item_.size());
    }

    /**
     * Gets the Item property <b>item</b> by index.
     *
     * @param index
     * @return Item
     */
    public Item getItem(int index) {
        return ((Item)item_.get(index));
    }

    /**
     * Sets the Item property <b>item</b> by index.
     *
     * @param index
     * @param item
     */
    public void setItem(int index, Item item) {
        this.item_.set(index, item);
    }

    /**
     * Adds the Item property <b>item</b> by index.
     *
     * @param index
     * @param item
     */
    public void addItem(int index, Item item) {
        this.item_.add(index, item);
    }

    /**
     * Remove the Item property <b>item</b> by index.
     *
     * @param index
     */
    public void removeItem(int index) {
        this.item_.remove(index);
    }

    /**
     * Remove the Item property <b>item</b> by object.
     *
     * @param item
     */
    public void removeItem(Item item) {
        this.item_.remove(item);
    }

    /**
     * Clear the Item property <b>item</b>.
     *
     */
    public void clearItem() {
        this.item_.clear();
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
        buffer.append("<channel");
        buffer.append(">");
        buffer.append("<title>");
        buffer.append(URelaxer.escapeCharData(URelaxer.getString(getTitle())));
        buffer.append("</title>");
        buffer.append("<link>");
        buffer.append(URelaxer.escapeCharData(URelaxer.getString(getLink())));
        buffer.append("</link>");
        buffer.append("<description>");
        buffer.append(URelaxer.escapeCharData(URelaxer.getString(getDescription())));
        buffer.append("</description>");
        buffer.append("<language>");
        buffer.append(URelaxer.escapeCharData(URelaxer.getString(getLanguage())));
        buffer.append("</language>");
        buffer.append("<pubDate>");
        buffer.append(URelaxer.escapeCharData(URelaxer.getString(getPubDate())));
        buffer.append("</pubDate>");
        buffer.append("<category>");
        buffer.append(URelaxer.escapeCharData(URelaxer.getString(getCategory())));
        buffer.append("</category>");
        buffer.append("<docs>");
        buffer.append(URelaxer.escapeCharData(URelaxer.getString(getDocs())));
        buffer.append("</docs>");
        buffer.append("<ttl>");
        buffer.append(URelaxer.escapeCharData(URelaxer.getString(getTtl())));
        buffer.append("</ttl>");
        size = this.item_.size();
        for (int i = 0;i < size;i++) {
            Item value = (Item)this.item_.get(i);
            value.makeTextElement(buffer);
        }
        buffer.append("</channel>");
    }

    /**
     * Makes an XML text representation.
     *
     * @param buffer
     * @exception IOException
     */
    public void makeTextElement(Writer buffer) throws IOException {
        int size;
        buffer.write("<channel");
        buffer.write(">");
        buffer.write("<title>");
        buffer.write(URelaxer.escapeCharData(URelaxer.getString(getTitle())));
        buffer.write("</title>");
        buffer.write("<link>");
        buffer.write(URelaxer.escapeCharData(URelaxer.getString(getLink())));
        buffer.write("</link>");
        buffer.write("<description>");
        buffer.write(URelaxer.escapeCharData(URelaxer.getString(getDescription())));
        buffer.write("</description>");
        buffer.write("<language>");
        buffer.write(URelaxer.escapeCharData(URelaxer.getString(getLanguage())));
        buffer.write("</language>");
        buffer.write("<pubDate>");
        buffer.write(URelaxer.escapeCharData(URelaxer.getString(getPubDate())));
        buffer.write("</pubDate>");
        buffer.write("<category>");
        buffer.write(URelaxer.escapeCharData(URelaxer.getString(getCategory())));
        buffer.write("</category>");
        buffer.write("<docs>");
        buffer.write(URelaxer.escapeCharData(URelaxer.getString(getDocs())));
        buffer.write("</docs>");
        buffer.write("<ttl>");
        buffer.write(URelaxer.escapeCharData(URelaxer.getString(getTtl())));
        buffer.write("</ttl>");
        size = this.item_.size();
        for (int i = 0;i < size;i++) {
            Item value = (Item)this.item_.get(i);
            value.makeTextElement(buffer);
        }
        buffer.write("</channel>");
    }

    /**
     * Makes an XML text representation.
     *
     * @param buffer
     */
    public void makeTextElement(PrintWriter buffer) {
        int size;
        buffer.print("<channel");
        buffer.print(">");
        buffer.print("<title>");
        buffer.print(URelaxer.escapeCharData(URelaxer.getString(getTitle())));
        buffer.print("</title>");
        buffer.print("<link>");
        buffer.print(URelaxer.escapeCharData(URelaxer.getString(getLink())));
        buffer.print("</link>");
        buffer.print("<description>");
        buffer.print(URelaxer.escapeCharData(URelaxer.getString(getDescription())));
        buffer.print("</description>");
        buffer.print("<language>");
        buffer.print(URelaxer.escapeCharData(URelaxer.getString(getLanguage())));
        buffer.print("</language>");
        buffer.print("<pubDate>");
        buffer.print(URelaxer.escapeCharData(URelaxer.getString(getPubDate())));
        buffer.print("</pubDate>");
        buffer.print("<category>");
        buffer.print(URelaxer.escapeCharData(URelaxer.getString(getCategory())));
        buffer.print("</category>");
        buffer.print("<docs>");
        buffer.print(URelaxer.escapeCharData(URelaxer.getString(getDocs())));
        buffer.print("</docs>");
        buffer.print("<ttl>");
        buffer.print(URelaxer.escapeCharData(URelaxer.getString(getTtl())));
        buffer.print("</ttl>");
        size = this.item_.size();
        for (int i = 0;i < size;i++) {
            Item value = (Item)this.item_.get(i);
            value.makeTextElement(buffer);
        }
        buffer.print("</channel>");
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
    public String getTitleAsString() {
        return (URelaxer.getString(getTitle()));
    }

    /**
     * Gets the property value as String.
     *
     * @return String
     */
    public String getLinkAsString() {
        return (URelaxer.getString(getLink()));
    }

    /**
     * Gets the property value as String.
     *
     * @return String
     */
    public String getDescriptionAsString() {
        return (URelaxer.getString(getDescription()));
    }

    /**
     * Gets the property value as String.
     *
     * @return String
     */
    public String getLanguageAsString() {
        return (URelaxer.getString(getLanguage()));
    }

    /**
     * Gets the property value as String.
     *
     * @return String
     */
    public String getPubDateAsString() {
        return (URelaxer.getString(getPubDate()));
    }

    /**
     * Gets the property value as String.
     *
     * @return String
     */
    public String getCategoryAsString() {
        return (URelaxer.getString(getCategory()));
    }

    /**
     * Gets the property value as String.
     *
     * @return String
     */
    public String getDocsAsString() {
        return (URelaxer.getString(getDocs()));
    }

    /**
     * Gets the property value as String.
     *
     * @return String
     */
    public String getTtlAsString() {
        return (URelaxer.getString(getTtl()));
    }

    /**
     * Sets the property value by String.
     *
     * @param string
     */
    public void setTitleByString(String string) {
        setTitle(string);
    }

    /**
     * Sets the property value by String.
     *
     * @param string
     */
    public void setLinkByString(String string) {
        setLink(string);
    }

    /**
     * Sets the property value by String.
     *
     * @param string
     */
    public void setDescriptionByString(String string) {
        setDescription(string);
    }

    /**
     * Sets the property value by String.
     *
     * @param string
     */
    public void setLanguageByString(String string) {
        setLanguage(string);
    }

    /**
     * Sets the property value by String.
     *
     * @param string
     */
    public void setPubDateByString(String string) {
        setPubDate(string);
    }

    /**
     * Sets the property value by String.
     *
     * @param string
     */
    public void setCategoryByString(String string) {
        setCategory(string);
    }

    /**
     * Sets the property value by String.
     *
     * @param string
     */
    public void setDocsByString(String string) {
        setDocs(string);
    }

    /**
     * Sets the property value by String.
     *
     * @param string
     */
    public void setTtlByString(String string) {
        setTtl(string);
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
     * for the <code>Channel</code>.
     *
     * @param element
     * @return boolean
     */
    public static boolean isMatch(Element element) {
        if (!URelaxer.isTargetElement(element, "channel")) {
            return (false);
        }
        RStack target = new RStack(element);
        boolean $match$ = false;
        Element child;
        child = target.popElement();
        if (child == null) {
            return (false);
        }
        if (!URelaxer.isTargetElement(child, "title")) {
            return (false);
        }
        $match$ = true;
        child = target.popElement();
        if (child == null) {
            return (false);
        }
        if (!URelaxer.isTargetElement(child, "link")) {
            return (false);
        }
        $match$ = true;
        child = target.popElement();
        if (child == null) {
            return (false);
        }
        if (!URelaxer.isTargetElement(child, "description")) {
            return (false);
        }
        $match$ = true;
        child = target.popElement();
        if (child == null) {
            return (false);
        }
        if (!URelaxer.isTargetElement(child, "language")) {
            return (false);
        }
        $match$ = true;
        child = target.popElement();
        if (child == null) {
            return (false);
        }
        if (!URelaxer.isTargetElement(child, "pubDate")) {
            return (false);
        }
        $match$ = true;
        child = target.popElement();
        if (child == null) {
            return (false);
        }
        if (!URelaxer.isTargetElement(child, "category")) {
            return (false);
        }
        $match$ = true;
        child = target.popElement();
        if (child == null) {
            return (false);
        }
        if (!URelaxer.isTargetElement(child, "docs")) {
            return (false);
        }
        $match$ = true;
        child = target.popElement();
        if (child == null) {
            return (false);
        }
        if (!URelaxer.isTargetElement(child, "ttl")) {
            return (false);
        }
        $match$ = true;
        while (true) {
            if (!Item.isMatchHungry(target)) {
                break;
            }
            $match$ = true;
        }
        if (!target.isEmptyElement()) {
            return (false);
        }
        return (true);
    }

    /**
     * Tests if elements contained in a Stack <code>stack</code>
     * is valid for the <code>Channel</code>.
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
     * is valid for the <code>Channel</code>.
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
