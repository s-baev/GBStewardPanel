package com.gillbus.steward;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



public class Utils {
	
	private static final String[] datePattern = {"dd.MM.yyyy", "yyyy.dd.MM", "MM.dd.yyyy", "yyyy.MM.dd", "dd.MM.yy", "MM.dd.yy", "yy.MM.dd", "yy.dd.MM",
												"dd-MM-yyyy", "yyyy-dd-MM", "MM-dd-yyyy", "yyyy-MM-dd", "dd-MM-yy", "MM-dd-yy", "yy-MM-dd", "yy-dd-MM",
												"dd/MM/yyyy", "yyyy/dd/MM", "MM/dd/yyyy", "yyyy/MM/dd", "dd/MM/yy", "MM/dd/yy", "yy/MM/dd", "yy/dd/MM",
												"dd-MMM-yy"};
	private static int i = 0;
	
	
	
	/**
	 * Возвращает значение элемента XML-документа по названию тега.
	 * @param thisElement Элемент XML-документа.
	 * @param tagName Название тега.
	 * @return Значение тега.
	 */
	public static String getElementsByTagName(Element thisElement, String tagName){
		try {
			return thisElement.getElementsByTagName(tagName).item(0).getFirstChild().getNodeValue();
		} catch (Exception e) {
//			System.err.println("Cannot get element by tag name " + tagName);
		}
		return "";
	}
	
	
	/**
	 * Формирует дату со строки.
	 * @param date Строка.
	 * @return Дата.
	 */
	public static Date makeDate(String date){
		Date newDate = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat(datePattern[i]);
			dateFormat.setLenient(false);
			newDate = dateFormat.parse(date);
			newDate = new Date(newDate.getTime() + 2*TimeZone.getDefault().getRawOffset());
			
		} catch (Exception e) {
			if (datePattern.length > i){
				i++;
				makeDate(date);
			}
		}
		i = 0;
		return newDate;
	}
	
	
	/**
	 * Формирует строку с даты в формате "dd.MM.yyyy HH:mm:ss".
	 * @param date Дата.
	 * @return Строка.
	 */
	public static String makeDate(Date date){
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		dateFormat.setLenient(false);
		if (date != null)
			return dateFormat.format(date);
		else
			return null;
	}
	

	/**
	 * Формирует целое число со строки.
	 * @param string Строка.
	 * @return Целое число.
	 */
	public static int returnInteger(String string){
		if (string.trim().isEmpty()) return 0;
		return Integer.parseInt(string.trim());
	}
	
	
	/**
	 * Формирует длинное целое число со строки.
	 * @param string Строка.
	 * @return Длинное целое число.
	 */
	public static long returnLong(String string){
		if (string.trim().isEmpty()) return 0;
		return Long.parseLong(string.trim());
	}
	
	
	/**
	 * Формирует число с точкой со строки.
	 * @param string Строка.
	 * @return Число с точкой.
	 */
	public static float returnFloat(String string){
		if (string.trim().isEmpty()) return 0;
		return Float.parseFloat(string.replace(",", "."));
	}
	
	
	/**
	 * Формирует число с точкой со строки.
	 * @param string Строка.
	 * @return Число с точкой.
	 */
	public static BigDecimal returnBigDecimal(String string){
		if (string.trim().isEmpty()) return new BigDecimal(0);
		return new BigDecimal(string.replace(",", "."));
	}
	
	
	/**
	 * Формирует логиский параметр true или false из строки bool.
	 * @param bool Строка.
	 * @return Если входящий параметр равен "1" или "true",
	 * то возвращается true, иначе - false.
	 */
	public static boolean returnBoolean(String bool){
		if (bool.trim().isEmpty()) return false;
		return Boolean.valueOf(
				bool.equals("1") || bool.equals("true") || bool.toLowerCase().equals("y") ? "true" : "false");
	}
	
	
	/**
	 * Формирует ФИО вида Фамилия И.О.
	 * @param lastName Фамилия.
	 * @param firstName Имя.
	 * @param secondName Отчество.
	 * @return ФИО.
	 */
	public static String getFullName(String lastName, String firstName, String secondName){
		try {
			String fullName = "";
			if (!lastName.isEmpty()) fullName = fullName + lastName + " ";
			if (!firstName.isEmpty()) fullName = fullName + firstName.charAt(0) + ".";
			if (!secondName.isEmpty()) fullName = fullName + secondName.charAt(0) + ".";
			return fullName;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * Создает XML документ.
	 * @return XML документ.
	 * @throws ParserConfigurationException
	 */
    public static Document createDocument() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
 
        documentBuilderFactory.setValidating(false);
        documentBuilderFactory.setNamespaceAware(false);
 
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
 
        return document;
    }
    
    
    /**
     * Добавляет новый элемент в XML документ.
     * @param document XML документ.
     * @param parent Родительский элемент.
     * @param name �?мя нового элемента.
     * @param value Значение нового ээлемента.
     * @return Новый элемент.
     */
	public static Element addElement(Document document, Node parent, String name, String value) {
		Element element = document.createElement(name);

		if (parent != null) {
			parent.appendChild(element);
		}

		if (value != null) {
			element.appendChild(document.createTextNode(value));
		}

		return element;
	}
 
    
    /**
     * Добавляет новый атрибут в XML документ.
      * @param document XML документ.
     * @param parent Родительский элемент.
     * @param name �?мя нового элемента.
     * @param value Значение нового ээлемента.
     * @return Новый атрибут.
     */
	public static Attr addAttribute(Document document, Node parent, String name, String value) {
		Attr attribute = document.createAttribute(name);

		if (parent != null) {
			parent.getAttributes().setNamedItem(attribute);
		}

		if (value != null) {
			attribute.setValue(value);
		}

		return attribute;
	}
	
	
    /**
     * Читает данный с входящего потока в исходящий.
     * Использует массив byte[1024].
     * @param in Входяший поток данных.
     * @param out Исходящий поток данных.
     */
    public static void readWriteStream(InputStream in, OutputStream out){
		try {
			byte[] buffer = new byte[1024];

			int i = in.read(buffer);

			while (i != -1) {
				out.write(buffer, 0, i);
				i = in.read(buffer);
			}
		} catch (Exception e) {
		}
	}
	
	
	/**
	 * Возвращает индех указанного по счету(charCount) символа в строке.
	 * @param str Строка.
	 * @param ch Символ.
	 * @param charCount Порядковый номер следующего по счету символа в строке.
	 * @return Индекс символа.
	 */
	public static int getIndex(String str, char ch, int charCount){
		int index = 0;
		for (int i = 0; i < str.length(); i++) {
			if(index < charCount && str.charAt(i) == ch){
				index++;
				if(index == charCount){
					index = i;
					return index;
				}
			}
		}
		return -1;
	}
	
}
