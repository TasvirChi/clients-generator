package com.borhan.client.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.borhan.client.BorhanApiException;
import com.borhan.client.BorhanClient;
import com.borhan.client.enums.BorhanMetadataObjectType;
import com.borhan.client.types.BorhanFilterPager;
import com.borhan.client.types.BorhanMetadata;
import com.borhan.client.types.BorhanMetadataFilter;
import com.borhan.client.types.BorhanMetadataListResponse;
import com.borhan.client.types.BorhanMetadataProfile;
import com.borhan.client.types.BorhanMetadataProfileFilter;
import com.borhan.client.types.BorhanMetadataProfileListResponse;

public class MetadataUtils {

	@SuppressWarnings("serial")
	public static class MetadataUtilsFieldNotSetException extends Exception {
		private String xPath;
		
		public MetadataUtilsFieldNotSetException(String xPath) {
			this.xPath = xPath;
		}
		
		public String getMessage(){
			return "No value defined for xPath [" + xPath + "]";
		}
	}
	
	public static void deleteMetadata(BorhanClient client, String objectId, BorhanMetadataObjectType objectType, String profileSystemName) throws BorhanApiException{
		BorhanMetadataProfile profile = getProfile(client, objectType, profileSystemName);
		deleteMetadata(client, objectId, objectType, profile.id);
	}
	
	public static void deleteMetadata(BorhanClient client, String objectId, BorhanMetadataObjectType objectType, int profileId) throws BorhanApiException {
		BorhanMetadata metadata = get(client, objectId, objectType, profileId);
		if(metadata != null) {
			deleteMetadata(client, metadata.id);
		}
	}
	
	public static void deleteMetadata(BorhanClient client, int metadataId) throws BorhanApiException {
		client.getMetadataService().delete(metadataId);
	}
	
	public static String getValue(BorhanClient client, String objectId, BorhanMetadataObjectType objectType, String profileSystemName, String xPath) throws BorhanApiException, XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		BorhanMetadataProfile profile = getProfile(client, objectType, profileSystemName);
		return getValue(client, objectId, objectType, profile.id, xPath);
	}
		
	public static String getValue(BorhanClient client, String objectId, BorhanMetadataObjectType objectType, int profileId, String xPath) throws BorhanApiException, XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		BorhanMetadata metadata = get(client, objectId, objectType, profileId);
		if(metadata != null) {
			return getValue(metadata.xml, xPath);
		}
		
		return null;
	}
	
	public static String getValue(String xml, String xPath) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);

		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes()));

		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		XPathExpression compiledExpression = xpath.compile(xPath);

		return (String) compiledExpression.evaluate(doc, XPathConstants.STRING);
	}

	public static void setValue(BorhanClient client, String objectId, BorhanMetadataObjectType objectType, String profileSystemName, String xPath, String value) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, TransformerException, BorhanApiException, MetadataUtilsFieldNotSetException {
		BorhanMetadataProfile profile = getProfile(client, objectType, profileSystemName);
		setValue(client, objectId, objectType, profile.id, xPath, value);
	}

	public static void setValue(BorhanClient client, String objectId, BorhanMetadataObjectType objectType, int profileId, String xPath, String value) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, TransformerException, BorhanApiException, MetadataUtilsFieldNotSetException {
		BorhanMetadata metadata = get(client, objectId, objectType, profileId);
		if(metadata == null) {
			addMetadata(client, objectId, objectType, profileId, xPath, value);
		}
		else if (hasValue(metadata, xPath)) {
			setValue(client, metadata, xPath, value);
		}
		else {
			throw new MetadataUtilsFieldNotSetException(xPath);
		}
	}

	public static void setValue(BorhanClient client, BorhanMetadata metadata, String xPath, String value) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, TransformerException, BorhanApiException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);

		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new ByteArrayInputStream(metadata.xml.getBytes()));

		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		XPathExpression compiledExpression = xpath.compile(xPath);

		Node node = (Node) compiledExpression.evaluate(doc, XPathConstants.NODE);
		node.setTextContent(value);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String xml = writer.getBuffer().toString().replaceAll("\n|\r", "");

		metadata = client.getMetadataService().update(metadata.id, xml);
	}

	public static BorhanMetadata addMetadata(BorhanClient client, String objectId, BorhanMetadataObjectType objectType, String profileSystemName, String xPath, String value) throws ParserConfigurationException, TransformerException, BorhanApiException {
		BorhanMetadataProfile profile = getProfile(client, objectType, profileSystemName);
		return addMetadata(client, objectId, objectType, profile.id, xPath, value);
	}
	
	public static BorhanMetadata addMetadata(BorhanClient client, String objectId, BorhanMetadataObjectType objectType, int profileId, String xPath, String value) throws ParserConfigurationException, TransformerException, BorhanApiException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();

		String[] elements = xPath.split("/");
		Node parentElement = doc;
		Element element = null;
		for (String elementName : elements) {
			if (elementName.length() > 0) {
				element = doc.createElement(elementName);
				parentElement.appendChild(element);
				parentElement = element;
			}
		}
		parentElement.setTextContent(value);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String xml = writer.getBuffer().toString().replaceAll("\n|\r", "");
		
		return addMetadata(client, objectId, objectType, profileId, xml);
	}

	public static BorhanMetadata addMetadata(BorhanClient client, String objectId, BorhanMetadataObjectType objectType, String profileSystemName, String xml) throws ParserConfigurationException, TransformerException, BorhanApiException {
		BorhanMetadataProfile profile = getProfile(client, objectType, profileSystemName);
		return client.getMetadataService().add(profile.id, objectType, objectId, xml);
	}

	public static BorhanMetadata addMetadata(BorhanClient client, String objectId, BorhanMetadataObjectType objectType, int profileId, String xml) throws ParserConfigurationException, TransformerException, BorhanApiException {
		return client.getMetadataService().add(profileId, objectType, objectId, xml);
	}

	public static boolean hasValue(BorhanClient client, String objectId, BorhanMetadataObjectType objectType, String profileSystemName, String xPath) throws BorhanApiException, XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		BorhanMetadataProfile profile = getProfile(client, objectType, profileSystemName);
		return hasValue(client, objectId, objectType, profile.id, xPath);
	}
		
	public static boolean hasValue(BorhanClient client, String objectId, BorhanMetadataObjectType objectType, int profileId, String xPath) throws BorhanApiException, XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		BorhanMetadata metadata = get(client, objectId, objectType, profileId);
		return hasValue(metadata, xPath);
	}
	
	public static boolean hasValue(BorhanMetadata metadata, String xPath) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		return hasValue(metadata.xml, xPath);
	}
	
	public static boolean hasValue(String xml, String xPath) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);

		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes()));

		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		XPathExpression compiledExpression = xpath.compile(xPath);

		return (Boolean) compiledExpression.evaluate(doc, XPathConstants.BOOLEAN);
	}
	
	public static BorhanMetadataProfile getProfile(BorhanClient client, BorhanMetadataObjectType objectType, String profileSystemName) throws BorhanApiException {
		BorhanMetadataProfileFilter filter = new BorhanMetadataProfileFilter();
		filter.metadataObjectTypeEqual = objectType;
		filter.systemNameEqual = profileSystemName;
		
		BorhanFilterPager pager = new BorhanFilterPager();
		pager.pageSize = 1;
		
		BorhanMetadataProfileListResponse metadataProfileList = client.getMetadataProfileService().list(filter, pager);
		if(metadataProfileList.objects.size() > 0){
			return metadataProfileList.objects.get(0);
		}
		
		return null;
	}
	
	public static BorhanMetadata get(BorhanClient client, String objectId, BorhanMetadataObjectType objectType, int profileId) throws BorhanApiException {
		BorhanMetadataFilter filter = new BorhanMetadataFilter();
		filter.objectIdEqual = objectId;
		filter.metadataObjectTypeEqual = objectType;
		filter.metadataProfileIdEqual = profileId;
		
		BorhanFilterPager pager = new BorhanFilterPager();
		pager.pageSize = 1;
		
		BorhanMetadataListResponse metadataList = client.getMetadataService().list(filter, pager);
		if(metadataList.objects.size() > 0){
			return metadataList.objects.get(0);
		}
		
		return null;
	}

}
