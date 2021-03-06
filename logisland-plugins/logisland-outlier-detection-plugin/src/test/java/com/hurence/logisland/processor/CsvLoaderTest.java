package com.hurence.logisland.processor;

import com.hurence.logisland.event.Event;
import com.hurence.logisland.log.LogParserException;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by mike on 15/04/16.
 */
public class CsvLoaderTest {
    private static final Logger logger = LoggerFactory.getLogger(TimeSeriesCsvLoader.class);
    final static Charset ENCODING = StandardCharsets.UTF_8;
    final String RESOURCES_DIRECTORY = "target/test-classes/benchmark_data/";
    private static final DateTimeFormatter inputDateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    //private static final DateTimeFormatter defaultOutputDateFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");


    @Test
    public void CsvLoaderTest() throws IOException, LogParserException {
        File f = new File(RESOURCES_DIRECTORY);

        for (File file : FileUtils.listFiles(f, new SuffixFileFilter(".csv"), TrueFileFilter.INSTANCE)) {
            BufferedReader reader = Files.newBufferedReader(file.toPath(), ENCODING);
            List<Event> events = TimeSeriesCsvLoader.load(reader, true, inputDateFormat);
            Assert.assertTrue(!events.isEmpty());
            //Assert.assertTrue("should be 4032, was : " + events.size(), events.size() == 4032);

            for (Event event : events) {
                Assert.assertTrue("should be sensors, was " + event.getType(), event.getType().equals("sensors"));
                Assert.assertTrue("should be 2, was " + event.entrySet().size(), event.entrySet().size() == 3);
                Assert.assertTrue(event.keySet().contains("timestamp"));
                Assert.assertTrue(event.keySet().contains("value"));
                Assert.assertTrue(event.get("timestamp").getValue() instanceof Long);
                Assert.assertTrue(event.get("value").getValue() instanceof Double);

                Assert.assertTrue(event.keySet().contains("value"));
            }
        }
    }

    @Test
    public void CsvLoaderUnparsableExceptionTest() throws IOException, LogParserException {
        File f = new File(RESOURCES_DIRECTORY + "artificialWithAnomaly/art_daily_flatmiddle.csv");

        DateTimeFormatter wrongInputDateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH-mm-ss");

        BufferedReader reader = Files.newBufferedReader(f.toPath(), ENCODING);
        try {
            TimeSeriesCsvLoader.load(reader, true, wrongInputDateFormat);
        }
        catch (LogParserException e) {
            return;
        }

        Assert.fail("Should have failed with an UnparsableException exception");
    }
}
