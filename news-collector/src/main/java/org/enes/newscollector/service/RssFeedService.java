package org.enes.newscollector.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.rometools.modules.mediarss.MediaEntryModule;
import com.rometools.modules.mediarss.types.MediaContent;
import com.rometools.modules.mediarss.types.Thumbnail;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class RssFeedService {

    private final NewsProducerService newsProducerService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public RssFeedService(NewsProducerService newsProducerService) {
        this.newsProducerService = newsProducerService;
    }

    public void fetchAndSend(String feedUrl, String source) {
        try {
            URL url = new URL(feedUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            for (SyndEntry entry : feed.getEntries()) {

                MediaEntryModule mediaModule = (MediaEntryModule) entry.getModule(MediaEntryModule.URI);
                String thumbnailUrl = "";
                String contentUrl = "";

                if (mediaModule != null) {
                    Thumbnail[] thumbnails = mediaModule.getMetadata().getThumbnail();
                    if (thumbnails != null && thumbnails.length > 0 && thumbnails[0].getUrl() != null) {
                        thumbnailUrl = thumbnails[0].getUrl().toString();
                    }

                    MediaContent[] contents = mediaModule.getMediaContents();
                    if (contents != null && contents.length > 0 && contents[0].getReference() != null) {
                        contentUrl = contents[0].getReference().toString();
                    }
                }

                String description = "";
                if (entry.getDescription() != null && entry.getDescription().getValue() != null) {
                    description = entry.getDescription().getValue();
                }

                String pubDateStr = "";
                Date pubDate = entry.getPublishedDate();
                if (pubDate != null) {
                    pubDateStr = dateFormat.format(pubDate);
                }

                Map<String, Object> newsMap = new HashMap<>();
                newsMap.put("title", entry.getTitle());
                newsMap.put("link", entry.getLink());
                newsMap.put("description", description);
                newsMap.put("pubDate", pubDateStr);
                newsMap.put("source", source);
                newsMap.put("thumbnailUrl", thumbnailUrl);
                newsMap.put("contentUrl", contentUrl);

                String newJson = objectMapper.writeValueAsString(newsMap);
                newsProducerService.sendNews(newJson);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
