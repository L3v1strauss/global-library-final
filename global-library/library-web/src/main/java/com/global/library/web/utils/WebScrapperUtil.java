package com.global.library.web.utils;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.global.library.web.constants.BookDetailsNames;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class WebScrapperUtil {

    public void getAuthorsFromWeb(Map<String, String> bookDetails, HtmlPage bookPage) {
        HtmlElement author = (HtmlElement) bookPage.getByXPath("//span[@itemprop='author']").get(0);
        String[] authorNames = author.getTextContent().split("; ");
        int nameCounter = 0;
        for (String authorName : authorNames) {
            String customAuthorName = authorName.replaceAll(",", "");
            bookDetails.put(BookDetailsNames.AUTHOR + nameCounter, customAuthorName);
            nameCounter++;
        }
        bookDetails.put(BookDetailsNames.AUTHORS_NAMES_COUNTER, String.valueOf(nameCounter));

    }

    public void getPublisherNameAndPublishingYearFromWeb(Map<String, String> bookDetails, HtmlPage bookPage) {
        HtmlElement publisher = (HtmlElement) bookPage.getByXPath("//span[@itemprop='publisher']").get(0);
        String[] publisherNameAndYear = publisher.getTextContent().split(", ");
        Pattern pattern = Pattern.compile("^[12][0-9]{3}$");
        for (String string : publisherNameAndYear) {
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                bookDetails.put(BookDetailsNames.YEAR_OF_PUBLISHING, string);
            } else {
                bookDetails.put(BookDetailsNames.PUBLISHER, string);
            }
        }
    }
}
