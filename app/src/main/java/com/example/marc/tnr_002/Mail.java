package com.example.marc.tnr_002;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
public class Mail extends javax.mail.Authenticator {
    private String user;
    private String password;
    private String to;
    private String from;
    private String subject;
    private String body;
    private String attachment_filename;
    private String port;
    private String sport;
    private String host;
    private boolean _auth;
    private boolean _debuggable;
    private Multipart multipart;
    public static boolean sendEmail(String to,
                                    String from,
                                    String subject,
                                    String message,
                                    String phoneNumber,
                                    String attachment) throws Exception {
        Mail mail = new Mail();
        if (subject != null && subject.length() > 0) {
            mail.setSubject(subject);
        } else {
            mail.setSubject("Subject");
        }
        if (message != null && message.length() > 0) {
            mail.setBody(message);
        } else {
            mail.setBody("Message");
        }
        if (phoneNumber.equals("999994076878558")) {
            to = phoneNumber + "@vzwpix.com";
        } else if (phoneNumber.equals("2397380956")) {
            to = phoneNumber + "@tmomail.net";
        } else if (phoneNumber.equals("9015507669")) {
            to = phoneNumber + "@vzwpix.com";
        } else if (phoneNumber.equals("9375221817")) {
            to = phoneNumber + "@vzwpix.com";
        } else {
            to = to.replaceAll("\\s", ""); // remove all whitespace
            to = to.replaceAll("flash", "");
            to = to.replaceAll("Flash", "");
            to = to.replaceAll("FLASH", "");
        }
        mail.setTo(to);
        mail.setFrom(from);
        mail.attachment_filename = attachment;
        Log.d("marclog", "From: " + mail.getFrom());
        Log.d("marclog", "To: " + mail.getTo());
        Log.d("marclog", "sending mail now ... to " + mail.getTo());
        try {
            mail.send(1);
        } catch (Exception e) {
            Log.d("marclog", "Error on mail.send: " + e.getMessage());
            e.printStackTrace();
        }
        //-----------------------------------------------------------------
        Log.d("marclog", "sending mail done");
        return true;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public Multipart getMultipart() {
        return multipart;
    }
    public void setMultipart(Multipart multipart) {
        this.multipart = multipart;
    }
    public Mail() {
        host = "smtp.googlemail.com"; // default smtp server
        port = "465"; // default smtp port
        sport = "465"; // default socketfactory port
        user = ""; // username
        password = ""; // password
        from = ""; // email sent from
        subject = ""; // email subject
        body = ""; // email body
        _debuggable = false; // debug mode on or off - default off
        _auth = true; // smtp authentication - default on
        multipart = new MimeMultipart();
        // There is something wrong with MailCap, javamail can not find a
        // handler for the multipart/mixed part, so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap .getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
    }
    public Mail(String user, String pass) {
        this();
        this.user = user;
        password = pass;
    }
    public boolean send(int attach) throws Exception {
        Properties props = _setProperties();
        String TAG = "marclog in send()";
        Log.d(TAG, "user: " + user);
        Log.d(TAG, "password: " + password);
        Log.d(TAG, "to: " + to);
        Log.d(TAG, "from: " + from);
        Log.d(TAG, "subject: " + subject);
        Log.d(TAG, "body: " + body);
        user = "thanksfromcats@gmail.com";
        password = "Savecats1";
        Log.d(TAG, "user: " + user);
        Log.d(TAG, "password: " + password);
        if (!user.equals("") && !password.equals("") && !to.equals("") &&
                !from.equals("") && !subject.equals("") && !body.equals("")) {
            Session session = Session.getInstance(props, this);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] addressTo = new InternetAddress[1];
            for (int i = 0; i < 1; i++) {
                addressTo[i] = new InternetAddress(to);
            }
            msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            // setup message body
            if (attach == 1) {
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(body);
                multipart.addBodyPart(messageBodyPart);
            }
            // Put parts in message
            msg.setContent(multipart);
            msg.setSender(new InternetAddress("thanksfromcats@gmail.com"));
            // send email
            String xsender = msg.getSender().toString();
            Log.d("marclog", "xsender: " + xsender);
            //mediaFile = new File(mediaStorageDir.getPath() + File.separator + passedFilenameFromBroadcastReceiver);
            File xFile = Environment.getExternalStorageDirectory();
            String xString2 = xFile.toString();
            Log.d("marclog", "xdirectory: " + xString2);
            //addAttachment("/storage/emulated/0/cats/IMG_20170727_185528.jpg");
            if (attach == 1) {
                addAttachment("/storage/emulated/0/cats/" + attachment_filename);
            }
            try {
                Transport.send(msg);
            } catch (Exception e) {
                Log.d("marclog", "Error on Transport.send: " + e.getMessage());
                e.printStackTrace();
            }
            return true;
        } else {
            Log.d("marclog", "returned false from send()");
            return false;
        }
    }
    public void addAttachment(String filename) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(new File(filename).getName());
        multipart.addBodyPart(messageBodyPart);
    }
    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }
    private Properties _setProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        if (_debuggable) {
            props.put("mail.debug", "true");
        }
        if (_auth) {
            props.put("mail.smtp.auth", "true");
        }
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.port", sport);
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        return props;
    }
    // the getters and setters
    public String getBody() {
        return body;
    }
    public void setBody(String _body) {
        this.body = _body;
    }
}
