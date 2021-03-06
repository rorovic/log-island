package com.hurence.logisland.processor.parser;

import com.hurence.logisland.components.ComponentsFactory;
import com.hurence.logisland.config.LogislandSessionConfigReader;
import com.hurence.logisland.config.LogislandSessionConfiguration;
import com.hurence.logisland.engine.StandardEngineContext;
import com.hurence.logisland.engine.StandardEngineInstance;
import com.hurence.logisland.engine.StreamProcessingEngine;
import com.hurence.logisland.event.Event;
import com.hurence.logisland.log.StandardParserInstance;
import com.hurence.logisland.processor.StandardProcessorInstance;
import com.hurence.logisland.serializer.EventKryoSerializer;
import com.hurence.logisland.serializer.EventSerializer;
import com.hurence.logisland.utils.kafka.KafkaUnit;
import kafka.producer.KeyedMessage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by gregoire on 25/07/16.
 */
public class SplitTextMultiLine2Test {

    private static Logger logger = LoggerFactory.getLogger(SplitTextMultiLine2Test.class);
    KafkaUnit kafkaServer = new KafkaUnit(2181, 9092);

    @Before
    public void setUp() throws Exception {
        kafkaServer.startup();
    }

    @After
    public void close() throws Exception {
        kafkaServer.shutdown();
    }

    @Test
    public void testStreamSplitTextMultiLine() throws Exception {
        try {
            logger.info("START JOB");
            String configFile = SplitTextMultiLine2Test.class.getResource("/traker.yml").getFile();

            // load the YAML config
            LogislandSessionConfiguration sessionConf = new LogislandSessionConfigReader().loadConfig(configFile);

            // instanciate engine and all the processor from the config
            List<StandardParserInstance> parsers = ComponentsFactory.getAllParserInstances(sessionConf);
            List<StandardProcessorInstance> processors = ComponentsFactory.getAllProcessorInstances(sessionConf);
            Optional<StandardEngineInstance> engineInstance = ComponentsFactory.getEngineInstance(sessionConf);

            // start the engine
            if (engineInstance.isPresent()) {
                StandardEngineContext engineContext = new StandardEngineContext(engineInstance.get());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        engineInstance.get().getEngine().start(engineContext, processors, parsers);
                    }
                };
                Thread t = new Thread(myRunnable);
                logger.info("STARTING THREAD {}", t.getId());
                t.start();
//                Thread.sleep(10000);
                //creates topics
                logger.info("sending input into topic {}", "logisland-mock-in");
                KeyedMessage<String, String> keyedMessage = new KeyedMessage<>("logisland-mock-in", "10.105.64.777",
                        "2015-11-09 00:00:00.235  INFO   [http-1580-exec-15             ] --- o.a.c.interceptor.LoggingOutInterceptor  - SESSION[PLAYER-WEB-LVS:402206286:3FA833BF4D9BA6AE6389B7357EE897E6:oadp] - USERID[NONE] - Outbound Message\n" +
                                "---------------------------\n" +
                                "ID: 519739\n" +
                                "Response-Code: 200\n" +
                                "Content-Type: application/vnd.lotsys.motors.ongoing.gamesessions.response-1+json\n" +
                                "Headers: {Content-Type=[application/vnd.lotsys.motors.ongoing.gamesessions.response-1+json], Date=[Sun, 08 Nov 2015 23:00:00 GMT]}\n" +
                                "Payload: {\"resultStatus\":\"ok\",\"data\":{\"games\":[]}}\n" +
                                "--------------------------------------");

                //when

                //then
//                assertEquals(Collections.singletonList("2015-11-09 00:00:00.235  INFO   [http-1580-exec-15             ] --- o.a.c.interceptor.LoggingOutInterceptor  - SESSION[PLAYER-WEB-LVS:402206286:3FA833BF4D9BA6AE6389B7357EE897E6:oadp] - USERID[NONE] - Outbound Message\n" +
//                        "---------------------------\n" +
//                        "ID: 519739\n" +
//                        "Response-Code: 200\n" +
//                        "Content-Type: application/vnd.lotsys.motors.ongoing.gamesessions.response-1+json\n" +
//                        "Headers: {Content-Type=[application/vnd.lotsys.motors.ongoing.gamesessions.response-1+json], Date=[Sun, 08 Nov 2015 23:00:00 GMT]}\n" +
//                        "Payload: {\"resultStatus\":\"ok\",\"data\":{\"games\":[]}}\n" +
//                        "--------------------------------------"), kafkaServer.readMessages("logisland-mock-in"));
//                assertEquals(Collections.singletonList("2015-11-09 00:00:00.235  INFO   [http-1580-exec-15             ] --- o.a.c.interceptor.LoggingOutInterceptor  - SESSION[PLAYER-WEB-LVS:402206286:3FA833BF4D9BA6AE6389B7357EE897E6:oadp] - USERID[NONE] - Outbound Message\n" +
//                        "---------------------------\n" +
//                        "ID: 519739\n" +
//                        "Response-Code: 200\n" +
//                        "Content-Type: application/vnd.lotsys.motors.ongoing.gamesessions.response-1+json\n" +
//                        "Headers: {Content-Type=[application/vnd.lotsys.motors.ongoing.gamesessions.response-1+json], Date=[Sun, 08 Nov 2015 23:00:00 GMT]}\n" +
//                        "Payload: {\"resultStatus\":\"ok\",\"data\":{\"games\":[]}}\n" +
//                        "--------------------------------------"), kafkaServer.readMessages("logisland-mock-in"));
//                assertEquals(Collections.singletonList("2015-11-09 00:00:00.235  INFO   [http-1580-exec-15             ] --- o.a.c.interceptor.LoggingOutInterceptor  - SESSION[PLAYER-WEB-LVS:402206286:3FA833BF4D9BA6AE6389B7357EE897E6:oadp] - USERID[NONE] - Outbound Message\n" +
//                        "---------------------------\n" +
//                        "ID: 519739\n" +
//                        "Response-Code: 200\n" +
//                        "Content-Type: application/vnd.lotsys.motors.ongoing.gamesessions.response-1+json\n" +
//                        "Headers: {Content-Type=[application/vnd.lotsys.motors.ongoing.gamesessions.response-1+json], Date=[Sun, 08 Nov 2015 23:00:00 GMT]}\n" +
//                        "Payload: {\"resultStatus\":\"ok\",\"data\":{\"games\":[]}}\n" +
//                        "--------------------------------------"), kafkaServer.readMessages("logisland-mock-in"));


                kafkaServer.sendMessages(keyedMessage);
//                Thread.sleep(10000);
                int size = 0;
                while (size == 0) {
                    kafkaServer.sendMessages(keyedMessage);
                    Thread.sleep(5000);
                    logger.info("reading topic");
                    List<String> messagesOut = kafkaServer.readMessages("logisland-mock-out");
                    size = messagesOut.size();
                }

                Assert.assertEquals(1, size);

            }
            logger.info("END JOB");
        } catch (Exception e) {
            logger.error("unable to launch runner :", e);
        }
    }

    @Test
    public void testStreamSplitText() throws Exception {
        try {
            logger.info("START JOB");
            String configFile = SplitTextMultiLine2Test.class.getResource("/traker.yml").getFile();

            // load the YAML config
            LogislandSessionConfiguration sessionConf = new LogislandSessionConfigReader().loadConfig(configFile);

            // instanciate engine and all the processor from the config
            List<StandardParserInstance> parsers = ComponentsFactory.getAllParserInstances(sessionConf);
            List<StandardProcessorInstance> processors = ComponentsFactory.getAllProcessorInstances(sessionConf);
            Optional<StandardEngineInstance> engineInstanceO = ComponentsFactory.getEngineInstance(sessionConf);

            // start the engine
            if (engineInstanceO.isPresent()) {
                StandardEngineContext engineContext = new StandardEngineContext(engineInstanceO.get());


                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        engineInstanceO.get().getEngine().start(engineContext, processors, parsers);
                    }
                });
                StreamProcessingEngine engineInstance = engineInstanceO.get().getEngine();

                logger.info("STARTING THREAD that start engine ,id:{}, name: {}", t.getId(), t.getName());
                t.start();
                Thread.sleep(5000);
                //creates topics

                logger.info("sending input into topic {}", "logisland-mock-in");
                KeyedMessage<String, String> keyedMessage = new KeyedMessage<>("logisland-mock-in", "10.105.64.777",
                        "Jul 21 00:59:54 traker1.prod.fdj.fr/traker1.prod.fdj.fr date=2016-07-21 time=00:59:53 devname=traker1M devid=FG3K0B3I11700181 logid=0000000013 type=traffic subtype=forward level=notice vd=root srcip=10.3.41.100 srcport=22769 srcintf=\"port15\" dstip=194.2.0.20 dstport=53 dstintf=\"aggregate-inet\" poluuid=7d2ce9d0-bd34-51e4-80b8-1ab8854b36cb sessionid=2881568097 proto=17 action=accept policyid=259 dstcountry=\"France\" srccountry=\"Reserved\" trandisp=snat transip=194.4.210.8 transport=22769 service=\"DNS\" duration=40 sentbyte=80 rcvdbyte=40 sentpkt=1 rcvdpkt=1 appcat=\"unscanned\"");

                //when


                kafkaServer.sendMessages(keyedMessage);

//                Thread.sleep(10000);
                int size = 0;
                List<Event> messagesOut = new LinkedList();
                List<Event> messagesOutErr = new LinkedList();
                EventSerializer serializer = new EventKryoSerializer(true);
                while (size == 0) {
                    kafkaServer.sendMessages(keyedMessage);
                    Thread.sleep(5000);
                    logger.info("reading topic");
                    messagesOut = kafkaServer.readEvent("logisland-mock-out", serializer);
                    size = messagesOut.size();
                    messagesOutErr = kafkaServer.readEvent("logisland-error", serializer);
                    if (size == 0) {
                        size = messagesOutErr.size();
                    }
                }
                Assert.assertNotEquals(0, size);
                Event event = messagesOut.get(0);

                Assert.assertEquals(33, event.keySet().size());
                Assert.assertEquals("2016-07-21", (String) event.get("date").getValue());
                Assert.assertEquals("FG3K0B3I11700181", (String) event.get("devid").getValue());
                Assert.assertEquals("259", (String) event.get("policy_id").getValue());
                Assert.assertEquals("40", (String) event.get("bytes_in").getValue());
                Assert.assertEquals("1", (String) event.get("packets_out").getValue());
                Assert.assertEquals("traffic", (String) event.get("type").getValue());
                Assert.assertEquals("10.3.41.100", (String) event.get("src_ip").getValue());
                Assert.assertEquals("40", (String) event.get("duration").getValue());
                Assert.assertEquals("port15", (String) event.get("src_inf").getValue());
                Assert.assertEquals("aggregate-inet", (String) event.get("dest_inf").getValue());
                Assert.assertEquals("forward", (String) event.get("subtype").getValue());
                Assert.assertEquals("traker1.prod.fdj.fr/traker1.prod.fdj.fr", (String) event.get("host").getValue());
                Assert.assertEquals("accept", (String) event.get("action").getValue());
                Assert.assertEquals("traker1M", (String) event.get("devname").getValue());
                Assert.assertEquals("53", (String) event.get("dest_port").getValue());
                Assert.assertEquals("194.4.210.8", (String) event.get("tran_ip").getValue());
                Assert.assertEquals("Jul 21 00:59:54 traker1.prod.fdj.fr/traker1.prod.fdj.fr date=2016-07-21 time=00:59:53 devname=traker1M devid=FG3K0B3I11700181 logid=0000000013 type=traffic subtype=forward level=notice vd=root srcip=10.3.41.100 srcport=22769 srcintf=\"port15\" dstip=194.2.0.20 dstport=53 dstintf=\"aggregate-inet\" poluuid=7d2ce9d0-bd34-51e4-80b8-1ab8854b36cb sessionid=2881568097 proto=17 action=accept policyid=259 dstcountry=\"France\" srccountry=\"Reserved\" trandisp=snat transip=194.4.210.8 transport=22769 service=\"DNS\" duration=40 sentbyte=80 rcvdbyte=40 sentpkt=1 rcvdpkt=1 appcat=\"unscanned\"", (String) event.get("raw_content").getValue());
                Assert.assertEquals("notice", (String) event.get("level").getValue());
                Assert.assertEquals("2881568097", (String) event.get("session_id").getValue());
                Assert.assertEquals("1", (String) event.get("packets_in").getValue());
                Assert.assertEquals("root", (String) event.get("vd").getValue());
                Assert.assertEquals("22769", (String) event.get("src_port").getValue());
                Assert.assertEquals("Jul 21 00:59:54", (String) event.get("line_date").getValue());
                Assert.assertEquals("80", (String) event.get("bytes_out").getValue());
                Assert.assertEquals("Reserved trandisp=snat", (String) event.get("src_country").getValue());
                Assert.assertEquals("DNS", (String) event.get("service").getValue());
                Assert.assertEquals("194.2.0.20", (String) event.get("dest_ip").getValue());
                Assert.assertEquals("17", (String) event.get("proto").getValue());
                Assert.assertEquals("7d2ce9d0-bd34-51e4-80b8-1ab8854b36cb", (String) event.get("pol_uuid").getValue());
                Assert.assertEquals("0000000013", (String) event.get("logid").getValue());
                Assert.assertEquals("00:59:53", (String) event.get("time").getValue());
                Assert.assertEquals("22769", (String) event.get("tran_port").getValue());
                Assert.assertEquals("France", (String) event.get("dest_country").getValue());
                Assert.assertEquals("Tue Jul 26 12:35:52 CEST 2016", (String) event.get("creationDate").getValue());

            }
            logger.info("END JOB");
        } catch (Exception e) {
            logger.error("unable to launch runner :", e);
        }
    }


}
