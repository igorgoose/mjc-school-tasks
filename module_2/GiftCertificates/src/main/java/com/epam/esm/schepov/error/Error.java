package com.epam.esm.schepov.error;

/**
 *  The {@code Error} class is used to send error messages to the user
 *  in a convenient way. Includes a {@link String} containing the error message
 *  and the error code.
 *
 * @author Igor Schepov
 * @since 1.0
 *
 */
public class Error {

    private final String errorMessage;
    private final int errorCode;

    /**
     * Initializes the {@code Error} with the error message and code.
     *
     * @param errorCode The error code.
     * @param errorMessage The error message.
     */
    public Error(int errorCode, String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    /**
     * Returns the error message.
     *
     * @return The error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Returns the error code.
     *
     * @return The error code.
     */
    public int getErrorCode() {
        return errorCode;
    }

}
