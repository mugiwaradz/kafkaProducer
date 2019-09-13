package com.azb.kafka;

import com.azb.kafka.incoming.BookPublisher;
import com.azb.kafka.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

import java.util.Collections;
import java.util.List;
import java.util.Map;
 
@SpringBootApplication
public class Library {
 
    @Autowired
    private BookPublisher bookPublisher;
 
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Library.class).web(false).run(args);
        context.getBean(Library.class).run(context);
        context.close();
    }
 
    private void run(ConfigurableApplicationContext context) {
 
        System.out.println("Inside ProducerApplication run method...");
 
        MessageChannel producerChannel = context.getBean("producerChannel", MessageChannel.class);
 
        List<Book> books = bookPublisher.getBooks();

        books.forEach( book -> {
            Map headers = Collections.singletonMap(KafkaHeaders.TOPIC, book.getGenre().toString());
            producerChannel.send(new GenericMessage(book.toString(), headers));
        });
 
        System.out.println("Finished ProducerApplication run method...");
    };
}