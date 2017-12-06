/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

public class Database {

	public String filePath;

	public Database(String filePath) {
		this.filePath = filePath;
	}

	public boolean userExists(String username) {
		try {
			File fXmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("user");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (getTagValue("username", eElement).equals(username)) {
						return true;
					}
				}
			}
			return false;
		} catch (Exception ex) {
			System.out.println("Database exception : userExists()");
			return false;
		}
	}

	public boolean checkLogin(String username, String password) {
		if (!userExists(username)) {
			return false;
		}
		try {
			File fXmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("user");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (getTagValue("username", eElement).equals(username)) {
						return true;
					}
				}
			}
			System.out.println("Hippie");
			return false;
		} catch (Exception ex) {
			System.out.println("Database exception : userExists()");
			return false;
		}
	}

	public void addUser(String username, String password) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filePath);
			Node data = doc.getFirstChild();
			Element newuser = doc.createElement("user");
			Element newusername = doc.createElement("username");
			newusername.setTextContent(username);
			Element newpassword = doc.createElement("password");
			newpassword.setTextContent(password);
			newuser.appendChild(newusername);
			newuser.appendChild(newpassword);
			data.appendChild(newuser);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(source, result);
		} catch (Exception ex) {
		}
	}
}
