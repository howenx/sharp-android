package com.hanmimei.utils;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.hanmimei.entity.VersionVo;

public class XMLPaserTools {
	

	public static VersionVo getUpdataInfo(InputStream is) throws Exception {
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");
		int type = parser.getEventType();
		VersionVo info = new VersionVo();
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("void".equals(parser.getName())) {
					if ("adminUserId".equals(parser.getAttributeValue(0))) {
						parser.nextTag();
						info.setAdminUserId(parser.nextText()); 
					} else if ("downloadLink".equals(parser.getAttributeValue(0))) {
						parser.nextTag();
						info.setDownloadLink(parser.nextText());
					} else if ("fileName".equals(parser.getAttributeValue(0))) {
						parser.nextTag();
						info.setFileName(parser.nextText());
					} else if ("productType".equals(parser.getAttributeValue(0))) {
						parser.nextTag();
						info.setProductType(parser.nextText());
					} else if ("releaseDesc".equals(parser.getAttributeValue(0))) {
						parser.nextTag();
						info.setReleaseDesc(parser.nextText());
					} else if ("releaseNumber".equals(parser.getAttributeValue(0))) {
						parser.nextTag();
						info.setReleaseNumber(parser.nextText());
					} else if ("releaseName".equals(parser.getAttributeValue(0))) {
						parser.nextTag();
						info.setReleaseName(parser.nextText());
					}
				}
				break;
			}
			type = parser.next();
		}
		return info;
	}
}
