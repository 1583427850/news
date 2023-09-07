package xyz.linyh.kafka.Comsumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
// * kafka消费者测试类
 */
public class ConsumerTest {

    public static void main(String[] args) {
        Properties properties = new Properties();

//        配置连接地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"101.37.167.58:9092");

//        配置key和value的反序列器
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"group1");

//        创建连接
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

//        指定订阅的主题
        consumer.subscribe(Collections.singletonList("topic-001"));

        try {
            while(true){
                for (ConsumerRecord<String, String> record : consumer.poll(Duration.ofMillis(1000))) {
                    System.out.println(record.key());
                    System.out.println(record.value());
                    System.out.println(record.partition());
                    System.out.println(record.offset());
                }
//                异步提交偏移量
                consumer.commitSync();

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
//            同步提交偏移量
            consumer.commitSync();
        }

////        一直循环查询
//        while(true) {
//            //一秒查询一次
//            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
//
//            for (ConsumerRecord<String, String> record : consumerRecords) {
//                System.out.println(record.key());
//                System.out.println(record.value());
////                获取消息的偏移量
//                System.out.println(record.offset());
////                同步提交消息的偏移量
////                consumer.commitSync();
//
////                异步提交偏移量
//                consumer.commitAsync(new OffsetCommitCallback() {
//                    @Override
//                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
//                        if(exception!=null) {
//                            exception.printStackTrace();
//                        }
//                    }
//                });
//            }
//        }


    }
}
