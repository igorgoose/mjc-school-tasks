package com.epam.esm.schepov.error;

import org.springframework.stereotype.Component;

@Component
public class ErrorCodeCreatorImpl implements ErrorCodeCreator {

    @Override
    public int createErrorCode(int prefix, int suffix) {
        String prefixStr = String.valueOf(prefix);
        String suffixStr = String.valueOf(suffix);
        return Integer.parseInt(prefixStr + suffixStr);
    }
}
