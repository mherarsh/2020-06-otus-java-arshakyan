package ru.otus.processor.impl;

import ru.otus.Message;
import ru.otus.processor.Processor;

public class ProcessorSwapFields11And13 implements Processor {
    @Override
    public Message process(Message message) {
        return message.toBuilder().field11(message.getField13()).field13(message.getField11()).build();
    }
}
