package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.impl.ListenerMessageHistory;
import ru.otus.processor.Processor;
import ru.otus.processor.impl.LoggerProcessorThrowable;
import ru.otus.processor.impl.ProcessorSwapFields11And13;

import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13
       2. Сделать процессор, который поменяет местами значения field11 и field13
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду
       4. Сделать Listener для ведения истории: старое сообщение - новое
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */

        List<Processor> processors = List.of(new LoggerProcessorThrowable(new ProcessorSwapFields11And13()));
        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            System.out.println(ex.getMessage());
        });

        var listenerMessageHistory = new ListenerMessageHistory();
        complexProcessor.addListener(listenerMessageHistory);

        var message = new Message.Builder()
                .field11("field11")
                .field13("field13")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);
        System.out.println("history: " + listenerMessageHistory.getHistoryList());

        complexProcessor.removeListener(listenerMessageHistory);
    }
}
