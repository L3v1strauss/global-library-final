package com.global.library.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.global.library.web.constants.BookDetailsNames;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class WebScraper {

    private final WebClient webclient = WebClientProvider.getDefaultWebClient();

    private static final String SEARCH_URL = "https://www.bookfinder.com/search/?author=&title=&lang=en&isbn=%s&new_used=*&destination=by&currency=USD&mode=basic&st=sr&ac=qr";
    private static final String IMAGE_URL = "https://pictures.abebooks.com/isbn/%s-us-300.jpg";

    public Map<String, String> getBookDetailsFromWeb(String isbn) {
        Map<String, String> bookDetails = new HashMap<>();
        try {
            String url = String.format(SEARCH_URL, isbn);
            HtmlPage bookPage = webclient.getPage(url);
            HtmlElement name = (HtmlElement) bookPage.getByXPath("//span[@id='describe-isbn-title']").get(0);
            HtmlElement description;
            if (!bookPage.getByXPath("//div[@id='bookSummary']").isEmpty()) {
                description = (HtmlElement) bookPage.getByXPath("//div[@id='bookSummary']").get(0);
                bookDetails.put(BookDetailsNames.DESCRIPTION, description.getTextContent());
            } else {
                bookDetails.put(BookDetailsNames.DESCRIPTION, StringUtils.EMPTY);
            }
            getAuthorsFromWeb(bookDetails, bookPage);
            getPublisherNameAndPublishingYearFromWeb(bookDetails, bookPage);
            bookDetails.put(BookDetailsNames.NAME, name.getTextContent());
            bookDetails.put(BookDetailsNames.PICTURE, String.format(IMAGE_URL, isbn));
        } catch (IOException exception) {
            log.info("Input output exception: {}", exception.getMessage());
        }
        return bookDetails;
    }

    private void getAuthorsFromWeb(Map<String, String> bookDetails, HtmlPage bookPage) {
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

    private void getPublisherNameAndPublishingYearFromWeb(Map<String, String> bookDetails, HtmlPage bookPage) {
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
