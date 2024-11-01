public class EmailSender {
    // Private constructor to prevent instantiation
    private EmailSender() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void sendEmail(String customerEmail, String subject, String message) {
        System.out.println("Email to: " + customerEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + message);
    }
}
