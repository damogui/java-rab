package one;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.sun.corba.se.spi.activation.Server;
import org.apache.commons.lang.SerializationUtils;

import java.util.List;

public class Recv {
    //队列名称
    private final static String QUEUE_NAME = "queue";

    public static void main(String[] argv) throws java.io.IOException,
            java.lang.InterruptedException {
        //打开连接和创建频道，与发送端一样
        ConnectionFactory factory = new ConnectionFactory();
        //设置MabbitMQ所在主机ip或者主机名
        factory.setHost("127.0.0.1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
        //设置队列的属性第一个参数为队列名。第二个参数为是否创建一个持久队列，第三个是否创建一个专用队列
        //第四个参数为是否自动删除队列，第五个参数为其他属性（结构参数）
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //System.out.println("Waiting for messages. To exit press CTRL+C");

        //创建队列消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //指定消费队列
//        第一个参数为队列名，第二个参数是否考虑已发送的消息，第三个参数为消费队形的接口
        channel.basicConsume(QUEUE_NAME, true, consumer);
        System.out.println("等待发送消息");
        while (true) {
//            HttpServletRequest request=ServletActionContext.getRequest();
//            request.getRemotePort();
//一、接受字符串
            //nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//            接受普通的字符串
            //String message = new String(delivery.getBody());
            //System.out.println("Received '" + message + "'");

//            二、接受一个单体类
            //反序列化
//             @SuppressWarnings("deprecation")
//            UserCustomer userCustomer = (UserCustomer) SerializationUtils.deserialize(delivery.getBody());
//
//            System.out.println(userCustomer.getName());
//            System.out.println(userCustomer.getAge());
//            System.out.println(userCustomer.getAddress());
//            System.out.println(userCustomer.getPhone());

//            三、接受json对象

            //将json数据转成对象
            ObjectMapper mapper = new ObjectMapper();

            String message = new String(delivery.getBody());
//            一、第一种获取集合类型
            //mapper.readValue(message.getBytes("utf-8"),UserCustomer.class);//单类读取
//            List<UserCustomer> userCustomerList = mapper.readValue(message.getBytes("utf-8"), new
//                    TypeReference<List<UserCustomer>>() {
//                    });
            //二、第二种获取集合类型
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, UserCustomer.class);
            List<UserCustomer> userCustomerList = mapper.readValue(message.getBytes("utf-8"),javaType);


            for (UserCustomer userCustomer : userCustomerList
                    ) {
                System.out.println(userCustomer.getName());
//                System.out.println(userCustomer.getAge());
//                System.out.println(userCustomer.getAddress());
//                System.out.println(userCustomer.getPhone());
            }


//            System.out.println(userCustomer.getName());
//            System.out.println(userCustomer.getAge());
//            System.out.println(userCustomer.getAddress());
//            System.out.println(userCustomer.getPhone());

        }

    }
}
