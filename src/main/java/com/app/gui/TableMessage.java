package com.app.gui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;

public class TableMessage {
    private final SimpleStringProperty date;
    private final SimpleStringProperty from;
    private final SimpleStringProperty to;
    private final SimpleStringProperty subject;

    public Message getMessage() {
        return message.get();
    }

    public void setMessage(Message message) {
        this.message.set(message);
    }

    public SimpleObjectProperty<Message> messageProperty() {
        return message;
    }

    private final SimpleObjectProperty<Message> message;


    public String getTo() {
        return to.get();
    }

    public SimpleStringProperty toProperty() {
        return to;
    }

    public void setTo(String to) {
        this.to.set(to);
    }

    public String getFrom() {
        return from.get();
    }

    public SimpleStringProperty fromProperty() {
        return from;
    }

    public void setFrom(String from) {
        this.from.set(from);
    }


    public String getSubject() {
        return subject.get();
    }

    public SimpleStringProperty subjectProperty() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }


    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    TableMessage(String from, String to, String subject, String date, MimeMessage message) {
        this.from = new SimpleStringProperty(from);
        this.to = new SimpleStringProperty(to);
        this.subject = new SimpleStringProperty(subject);
        this.date = new SimpleStringProperty(date);
        this.message = new SimpleObjectProperty<>(message);
    }

}
