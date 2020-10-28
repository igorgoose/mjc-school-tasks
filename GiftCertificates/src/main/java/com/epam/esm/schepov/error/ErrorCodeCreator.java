package com.epam.esm.schepov.error;

public interface ErrorCodeCreator {
    int createErrorCode(int prefix, int suffix);
}
