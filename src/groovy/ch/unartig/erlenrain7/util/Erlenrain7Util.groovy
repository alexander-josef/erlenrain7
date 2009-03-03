package ch.unartig.erlenrain7.util

import javax.mail.Session
import javax.mail.Message
import javax.mail.internet.MimeMessage
import javax.mail.internet.InternetAddress
import java.text.SimpleDateFormat


public class Erlenrain7Util {

  /**
   * Generic email helper for erlenrain7
   */
  static void sendEmail(String emailToAddress, java.lang.String subject, java.lang.String text) {

    Properties props = new Properties();
    props.put("mail.host", "localhost");
    Session session = Session.getDefaultInstance(props, null);
    Message message = new MimeMessage(session)
    String messageType = "UTF-8"
    message.setFrom(new InternetAddress("erlenrain7@gmail.com"))
    message.setSubject(subject, messageType)
    message.setText(text, messageType)
    InternetAddress[] addressTo = new InternetAddress[1];
    addressTo[0] = new InternetAddress(emailToAddress)
    message.setRecipients(Message.RecipientType.TO, addressTo)
    message.setHeader("Content-Type", "text/plain; charset=UTF-8");
    javax.mail.Transport.send(message)

  }


  /**
   * After a tentative entry has been confirmed by an administrator, inform the guest that his reservation has been fixed.
   */
  static void sendConfirmationEmailFor(String toAddress, java.util.Date startDate, java.util.Date endDate) {

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    println toAddress
    println startDate
    println endDate

    // todo use proper localisation for date format.

    // todo add link for changing or canceling reservation
    def subject = "Wohnung reserviert: ${dateFormat.format(startDate)} bis ${dateFormat.format(endDate)}"
    def text =
"""Deine Reservation vom ${dateFormat.format(startDate)} bis zum ${dateFormat.format(endDate)} wurde bestätigt und definitiv reserviert.

Reservation anpassen: Todo Link zu Reservation

Schönen Aufenthalt in Engelberg wünscht dir erlenrain7.ch !"""
    Erlenrain7Util.sendEmail(toAddress, subject, text)
  }
}