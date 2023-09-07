package xyz.linyh.kafka.Producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * kafka生产者测试
 */
public class ProducerTest {

    public static void main(String[] args) {

        Properties properties = new Properties();
//        ProducerConfig为kafka里面的配置类

//        配置kafka的远程连接地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"101.37.167.58:9092");

//        配置kafka的key和value的序列化器
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

//        设置消息发送后的应答机制，如果ack设置为0，那么不会等待服务器的应答消息，有消息丢失的风险，但是速度最快
//                                如果设置为1，那么只需要集群的领导节点收到消息，那么就会应答给消费者
//                                如果设置为all，那么就需要等所有的节点都受到消息，那么才会应答
        properties.put(ProducerConfig.ACKS_CONFIG,"all");

//        设置如果出现问题的重试次数，重试10次，每次间隔默认为100ms
        properties.put(ProducerConfig.RETRIES_CONFIG,10);

//        设置发送消息的压缩算法      snappy 占用较少的cpu，能提供叫好的性能和压缩比
//                                lz4 占用较少的cpu，压缩和解压缩速度快
//                                gzip 占用较多的cpu，但会提供更高的压缩比
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,"lz4");

//        创建连接
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);


//        将消息封装
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("topic-001","key-001","value-002");

////        发送消息（同步）
//        try {
//            RecordMetadata recordMetadata = producer.send(producerRecord).get();
//            //获取保存到分区中的偏移量
//            System.out.println(recordMetadata.offset());
////            获取是保存到哪个分区中的
//            System.out.println(recordMetadata.partition());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

//        发送消息（异步）
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if(exception!=null){
                    exception.printStackTrace();
                }
//                获取分区的偏移量
                System.out.println(metadata.offset());
            }
        });

//        关闭producer流
        producer.close();
        System.out.println("发送成功");
    }
}
