package com.app.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailSender {
    private Message msg;

    private final ExecutorService executorService;

    public ExecutorService getExecutorService() {
        return executorService;
    }
    public EmailSender() {
        int messagesSimultaneously = 5;
        executorService = Executors.newFixedThreadPool(messagesSimultaneously);
    }
    public void setBasic(Session session, String subject) {
        try {
            msg = new MimeMessage(session);
            msg.setSubject(subject);
            msg.setFrom(new InternetAddress(session.getProperty("username")));
            msg.setSentDate(new Date());
            msg.setFrom(new InternetAddress(session.getProperty("username")));
        } catch (MessagingException e) {
            System.out.println("Couldn't create message");
            throw new RuntimeException(e);
        }
    }
    public void sendEmail(Session session) {
        try (Transport trans = session.getTransport("smtp")) {
            trans.connect();
            Transport.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void addFilesAndText(List<String> paths, String text) {
        MimeMultipart allParts = new MimeMultipart();
        try {
            for (String path : paths) {
                try {
                    MimeBodyPart partForFile = new MimeBodyPart();
                    File file = new File(path);
                    partForFile.attachFile(file);
                    allParts.addBodyPart(partForFile);
                } catch (MessagingException | IOException e) {
                    System.out.println("File is not supported");
                    throw new RuntimeException(e);
                }
            }
            MimeBodyPart partForText = new MimeBodyPart();
            partForText.setContent(text, "text/plain");
            allParts.addBodyPart(partForText);

            msg.setContent(allParts);
            msg.saveChanges();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRecipients(List<InternetAddress> recipientsTO, List<InternetAddress> recipientsCC,
                            List<InternetAddress> recipientsBCC) {
        try {
            msg.setRecipients(Message.RecipientType.TO, recipientsTO.toArray(new Address[0]));
            msg.setRecipients(Message.RecipientType.CC, recipientsCC.toArray(new Address[0]));
            msg.setRecipients(Message.RecipientType.BCC, recipientsBCC.toArray(new Address[0]));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        if (executorService != null && !executorService.isShutdown()) executorService.shutdownNow();
    }
}
