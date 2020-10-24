package com.epam.esm.schepov.formatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatter implements Formatter<Date> {

    private final MessageSource messageSource;

    @Autowired
    public DateFormatter(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        SimpleDateFormat dateFormat = createDateFormat(locale);
        return dateFormat.parse(text);
    }

    @Override
    public String print(Date object, Locale locale) {
        SimpleDateFormat dateFormat = createDateFormat(locale);
        return dateFormat.format(object);
    }

    private SimpleDateFormat createDateFormat(Locale locale){
        String format = messageSource.getMessage("date.format",null, locale);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        return dateFormat;
    }

}
