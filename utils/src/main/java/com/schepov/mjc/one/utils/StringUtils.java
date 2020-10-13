package com.schepov.mjc.one.utils;

public class StringUtils {

    private static final int MAX_POINT_SPLIT_PARTS = 2;
    private static final int MAX_E_SPLIT_PARTS = 2;
    private static final int FIRST_PART_INDEX = 0;
    private static final int SECOND_PART_INDEX = 1;
    private static final int FIRST_CHARACTER_INDEX = 0;
    private static final int SECOND_CHARACTER_INDEX = 1;

    private StringUtils(){

    }

    public static boolean isPositiveNumber(String str) {
        return isNumber(str) && str.charAt(FIRST_CHARACTER_INDEX) != '-' && !isZero(str);
    }

    private static boolean isZero(String str){
        return str.matches("0+.?0*[Ee]?-?\\d*");
    }

    private static boolean isNumber(String str) {
        String[] pointSplitParts = org.apache.commons.lang3.StringUtils.split(str, ".");
        if (pointSplitParts.length > MAX_POINT_SPLIT_PARTS) {
            return false;
        }
        if (pointSplitParts.length == MAX_POINT_SPLIT_PARTS) {
            String[] eSplitParts = org.apache.commons.lang3.StringUtils.
                    split(pointSplitParts[SECOND_PART_INDEX], "Ee");
            if (eSplitParts.length > MAX_E_SPLIT_PARTS) {
                return false;
            }
            for (String stringPart : eSplitParts) {
                if(!isWholeNumber(stringPart)){
                    return false;
                }
            }
        }
        return isWholeNumber(pointSplitParts[FIRST_PART_INDEX]);
    }

    private static boolean isWholeNumber(String str) {
        return str.charAt(FIRST_CHARACTER_INDEX) == '-' ?
                org.apache.commons.lang3.StringUtils.isNumeric(str.substring(SECOND_CHARACTER_INDEX)) :
                org.apache.commons.lang3.StringUtils.isNumeric(str);

    }

}
