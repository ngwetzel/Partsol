package io.reflectoring.demo;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
@SpringBootApplication
public class RSSProcessor {
    private static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/rssdata";
    @Value("${spring.datasource.username")
    private static  String USERNAME;
    @Value("${spring.datasource.password")
    private static String PASSWORD ;
    public static void main(String[] args) {
        String feedUrl = "https://feeds.bbci.co.uk/news/world/rss.xml";
        // Replace with your RSS feed URL
        try {
             //Parse RSS feed
             URL url = new URL(feedUrl);
             SyndFeedInput input = new SyndFeedInput();
             SyndFeed feed = input.build(new XmlReader(url));
             //Connect to the database

             Connection connection = DriverManager.getConnection(POSTGRES_URL, USERNAME, PASSWORD);
             //Insert feed entries into the database
            String sql = "INSERT INTO rss_entries (title, link, description, published_date) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
             List<SyndEntry> entries = feed.getEntries(); for (SyndEntry entry : entries) { statement.setString(1, entry.getTitle());
             statement.setString(2, entry.getLink());
             statement.setString(3, entry.getDescription().getValue());
             statement.setTimestamp(4, new java.sql.Timestamp(entry.getPublishedDate().getTime()));
             statement.executeUpdate();
             }
             statement.close();
             connection.close();
             System.out.println("RSS feed data inserted successfully.");
            } catch (Exception e) { e.printStackTrace(); }
    }
}
