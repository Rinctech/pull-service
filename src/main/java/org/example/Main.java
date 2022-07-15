package org.example;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
//import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.CsvReader;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema;
//import org.apache.flink.shaded.netty4.io.netty.util.CharsetUtil;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
//import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testRecord.DataRecordAvro;


import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) throws Exception {
//		SpringApplication.run(EnrichmentjaApplication.class, args);
        initPr();
//		decodeData();
      /*  OndemandParameters cmd = new OndemandParameters();

        cmd.MeterIP = "2402:3a80:1700:175::2";
        cmd.MeterPort = 4059;
        cmd.start = System.currentTimeMillis();
        Client client = new Client(cmd);
        Thread t1 = new Thread(client);
        t1.start();*/

    }

    static void initPr() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties kafkaProps = new Properties();
        kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                KafkaAvroDeserializer.class);
        kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, "demo-consumer-1");
        kafkaProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-l7pr2.ap-south-1.aws.confluent.cloud:9092");
        kafkaProps.put("security.protocol", "SASL_SSL");
        kafkaProps.put("sasl.mechanism", "PLAIN");
        kafkaProps.put("client.dns.lookup", "use_all_dns_ips");
        kafkaProps.put("session.timeout.ms", "45000");
        kafkaProps.put("acks", "all");
        kafkaProps.put("sasl.jaas.config",
                "org.apache.kafka.common.security.plain.PlainLoginModule   required username='4ZMANDR2ONJI2IWN'   password='cOs9AfZiNLtE9+Orq6gE6s9OaVKJe4/M8hjNWgWycqBbmB6OYyPHrOMBEv0bBr19';");

        kafkaProps.put("schema.registry.url",
                "https://psrc-kjwmg.ap-southeast-2.aws.confluent.cloud"); //<----- Run Schema Registry on 8081
        kafkaProps.put("basic.auth.credentials.source",
                "USER_INFO");
        kafkaProps.put("basic.auth.user.info",
                "3PKXAMTSXCDERYAS:wLwB+C4/rGu6quOLn54NW/SsyBciKIIuheAzqsJ0pHBb7xOa4QXYl7DKcWG+iMv0");

        Map <String, String> config = new HashMap();
        config.put("basic.auth.credentials.source",
                "USER_INFO");
        config.put("basic.auth.user.info",
                "3PKXAMTSXCDERYAS:wLwB+C4/rGu6quOLn54NW/SsyBciKIIuheAzqsJ0pHBb7xOa4QXYl7DKcWG+iMv0");

        KafkaSource<DataRecordAvro> source = KafkaSource.<DataRecordAvro>builder()
                .setProperties(kafkaProps)
                .setTopics("ondemand")
                .setGroupId("my-group")
                .setStartingOffsets(OffsetsInitializer.latest())
//              .setDeserializer(KafkaRecordDeserializationSchema.valueOnly(StringDeserializer.class))
                .setValueOnlyDeserializer(ConfluentRegistryAvroDeserializationSchema.forSpecific(DataRecordAvro.class, "https://psrc-kjwmg.ap-southeast-2.aws.confluent.cloud", config))
                .build();
        DataStream<DataRecordAvro> inputStream = env.fromSource(source, WatermarkStrategy.forMonotonousTimestamps(), "Kafka Source");

          System.out.println("Testing 123");
        SingleOutputStreamOperator<String> counts =
                // The text lines read from the source are split into words
                // using a user-defined function. The tokenizer, implemented below,
                // will output each word as a (2-tuple) containing (word, 1)
                inputStream.flatMap(new Pull_Services())
                        .keyBy(m->m.Meterserialnumber)
                        .window(SlidingProcessingTimeWindows.of(Time.seconds(240), Time.seconds((120))))
                        .process(
                                new MyProcessWindowFunction()
                        )
                        .name("Pull Services");

        MeterData data = null;
        System.out.println(String.valueOf(counts));
       // log.info(String.valueOf(counts));

        env.execute("Streaming Data Pipeline from Kafka Source To Kafka Sink via Flink");
    }


    public static class Pull_Services implements FlatMapFunction<DataRecordAvro, MeterData> {

        static ExecutionEnvironment env1 = ExecutionEnvironment.getExecutionEnvironment();

       // List<DataObject> totalList = new LinkedList<DataObject>();

        @Override
        public void flatMap(DataRecordAvro value, Collector<MeterData> out) throws JsonProcessingException {
            OndemandParameters cmd = new OndemandParameters();
            System.out.println(value.toString());
            cmd.MeterSerialnumber= (String) value.getMeterSerialnumber().toString();
            cmd.MeterIP=(String)value.getIp().toString();
            cmd.MeterPort = (int) value.getPort();
            cmd.MeterFlag=(String)value.getMeterFlag().toString();
            cmd.Obis=(String)value.getObis().toString();
            cmd.Ic=(String)value.getIc().toString();
            cmd.Attributes=(String)value.getAttributes().toString();
            cmd.payload=(String)value.getPayload().toString();
            cmd.EKEY=(String)value.getEKEY().toString();
           //cmd.AKEY=(String)value.getAKEY().toString();
            cmd.Security_Id=(String)value.getSecurityId().toString();
            cmd.msg_type=(String)value.getMsgType().toString();
            cmd.start = System.currentTimeMillis();
            cmd.AKEY= (String)value.getSecurityId().toString()+(String)value.getAKEY().toString();
           // cmd.AKEY.concat( (String)value.getAKEY().toString());
            Client client = new Client(cmd);
            Thread t1 = new Thread(client);
            t1.start();

        }

        private static String asciiToHex(String asciiStr) {
            char[] chars = asciiStr.toCharArray();
            StringBuilder hex = new StringBuilder();
            for (char ch : chars) {
                hex.append(Integer.toHexString((int) ch));
            }
           return hex.toString();
        }




    }

}


