package com.ot.cm.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.tgms.entity.Workflow;
import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

public class CommonUtil {

	public static boolean isEmpty(String str) {
		return (str == null) || str.trim().length() == 0;
	}

	public static boolean isEmpty(String[] str) {
		return (str == null) || str.length == 0;
	}

	public static String formatDate(String inputFormat, String outputFormat, String date)
			throws ParseException, TGOCPBaseException {
		try {
			SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
			Date inputDate = inputDateFormat.parse(date);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String outputdate = sdf.format(inputDate);
			return outputdate;
		} catch (ParseException e) {
			throw new TGOCPBaseException(e.getMessage());
		}
	}

	public static String getStackTrace(Throwable e) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append(" ");
		}
		return sb.toString();
	}

	public static String encodeParam(String param) {
		try {
			return URLEncoder.encode(param, "UTF-8").replaceAll("\\+", "%20");
		} catch (Exception e) {
			return param;
		}
	}

	public static String toTitleCase(String input) {
		if (null != input) {
			StringBuilder titleCase = new StringBuilder();
			boolean nextTitleCase = true;
			for (char c : input.toLowerCase().toCharArray()) {
				if (!Character.isLetterOrDigit((int) c)) {
					nextTitleCase = true;
				} else if (nextTitleCase) {
					c = Character.toTitleCase(c);
					nextTitleCase = false;
				}
				titleCase.append(c);
			}
			return titleCase.toString();
		} else {
			return input;
		}

	}

	public static String jaxbObjectToXML(Object classToBeBound) {
		String xmlString = "";
		try {
			JAXBContext context = JAXBContext.newInstance(classToBeBound.getClass());
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(CharacterEscapeHandler.class.getName(), new XmlCharacterHandler());
			StringWriter sw = new StringWriter();
			m.marshal(classToBeBound, sw);
			xmlString = sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xmlString;
	}

	public static String formatDateToSolr(String inputFormat, String outputFormat, String date)
			throws ParseException, TGOCPBaseException {
		try {
			SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
			Date inputDate = inputDateFormat.parse(date);
			SimpleDateFormat sdf = new SimpleDateFormat(outputFormat);
			String outputdate = sdf.format(inputDate);
			return outputdate;
		} catch (ParseException e) {
			throw new TGOCPBaseException(e.getMessage());
		}
	}

	public static Workflow XMLStringTojavaXObject(String xmlString) throws JAXBException {
		JAXBContext jaxbContext;
		jaxbContext = JAXBContext.newInstance(Workflow.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		Workflow workflow = (Workflow) jaxbUnmarshaller.unmarshal(new StringReader(xmlString));
		return workflow;
	}
}
