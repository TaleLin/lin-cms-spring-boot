package io.github.talelin.merak.extensions.message;

public @interface Message {
    /**
     * message to send
     * @return message body
     */
    String value();

    /**
     * the type of message that we call event.
     * when it's "", represent send all message
     * @return name of message type
     */
    String event() default "";
}
