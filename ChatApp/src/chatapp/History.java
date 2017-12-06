package chatapp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class History {

	public String filePath;

	public History(String filePath) {
		this.filePath = filePath;
	}

	public void addMessage(Message msg, String time) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filePath);
			Node data = doc.getFirstChild();
			Element message = doc.createElement("message");
			Element _sender = doc.createElement("sender");
			_sender.setTextContent(msg.sender);
			Element _content = doc.createElement("content");
			_content.setTextContent(msg.content);
			Element _recipient = doc.createElement("recipient");
			_recipient.setTextContent(msg.recipient);
			Element _time = doc.createElement("time");
			_time.setTextContent(msg.time);
		} catch (Exception ex) {
		}
	}
}
