import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailSender {
   private static final Logger logger = Logger.getLogger(EmailSender.class.getName());
   // Private constructor to prevent instantiation
   private EmailSender() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
   //logging instead of system.out/system.err
   public static void sendEmail(String customerEmail, String subject, String message) {
      logger.log(Level.INFO, "Email to: {0}", customerEmail);
      logger.log(Level.INFO, "Subject: {0}", subject);
      logger.log(Level.INFO, "Body: {0}", message);
   }
}
