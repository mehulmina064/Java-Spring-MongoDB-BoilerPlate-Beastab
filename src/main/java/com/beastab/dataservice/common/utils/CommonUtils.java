package com.beastab.dataservice.common.utils;

import com.beastab.dataservice.identityservice.DTO.UserCompanyRole;
import com.beastab.dataservice.identityservice.db.entity.ClientEntity;
import com.beastab.dataservice.identityservice.db.repository.ClientRepository;
import com.beastab.dataservice.identityservice.enums.UserRole;
import com.beastab.dataservice.common.enums.StateCodeWithPAN;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@Slf4j
public class CommonUtils {

    public static String DEFAULT_PASSWORD = "8|30t`cJ$X11bW>V";
    public static String DEFAULT_AVATAR = "https://prodo-java-files.s3.ap-south-1.amazonaws.com/AVATAR.png";
    public static String NOTIFICATION_ICON = "https://prodo-marketing.s3.ap-south-1.amazonaws.com/icons/notifications_icon.png";

    public static String CUSTOM_PRODUCT_THUMBNAIL = "https://prodo-marketing.s3.ap-south-1.amazonaws.com/icons/custom_product.png";
    @Autowired
    private ClientRepository clientRepository;




    public static HashMap<String, String> getClientAndCompany(HttpServletRequest req) {
        HashMap<String, String> authDetails = new HashMap<>();
        authDetails.put("client_id", String.valueOf(req.getAttribute("client_id")));
        authDetails.put("company_id", String.valueOf(req.getAttribute("company_id")));
        authDetails.put("role", String.valueOf(req.getAttribute("role")));
        return authDetails;
    }

    public static HashMap<String, String> getClientAndCompanyManufacturer(HttpServletRequest req) {
        HashMap<String, String> authDetails = new HashMap<>();
        authDetails.put("client_id", String.valueOf(req.getAttribute("client_id")));
        authDetails.put("company_id", String.valueOf(req.getAttribute("company_id")));
        authDetails.put("role", String.valueOf(req.getAttribute("role")));
        authDetails.put("is_verified", String.valueOf(req.getAttribute("is_verified")));
        return authDetails;
    }

    public static LocalDateTime getLocalDateFormat(String date) {
//        String exampleDate = "24-05-2023";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.of(0, 0); // Set the time
        return LocalDateTime.of(localDate, localTime);
    }

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        LocalDateTime l = convertToIST(localDateTime);
        if (Objects.nonNull(l)) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return l.format(dateTimeFormatter);
        }
        return "";
    }

    public static String convertToDisplayDate(LocalDateTime dateTime) {
        LocalDateTime l = convertToIST(dateTime);
        if (Objects.nonNull(l)) {
            String format = "dd MMM yyyy";
            return convertToDisplayDateWithFormatIST(l, format);
        }
        return "";
    }

    public static LocalDateTime convertToDate(String dateString) {
        String format = "dd MMM yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date.atStartOfDay();
    }

    public static String convertToISODateFormat(LocalDateTime dateTime) {
        LocalDateTime l = convertToIST(dateTime);
        if (Objects.nonNull(l)) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            return l.format(formatter);
        }
        return "";
    }

    public static boolean isValidDateFormat(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            formatter.parse(input);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String formatToLocalDateString(LocalDateTime dateTime) {
        LocalDateTime l = convertToIST(dateTime);
        if (Objects.nonNull(dateTime)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
            return dateTime.format(formatter);
        }
        return "";
    }

    public static String convertToDisplayDateWithFormatIST(LocalDateTime dateTime, String format) {
        if (Objects.nonNull(dateTime)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return dateTime.format(formatter);
        }
        return "";
    }

    public static String convertToDisplayDateWithFormat(LocalDateTime dateTime, String format) {
        LocalDateTime l = convertToIST(dateTime);
        if (Objects.nonNull(l)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return l.format(formatter);
        }
        return "";
    }

    public static boolean gstCheck(String gst) {
        if (!(gst.length() == 15)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GST is not valid. Please provide GST in correct format. Length (" + gst.length() + ") must be 15");
        }

        String[] stateCodes = StateCodeWithPAN.getStateCodes();
        String[] stateChars = StateCodeWithPAN.getStateChars();
        String state = gst.substring(0, 2);
        int stateIndex = -1;

        for (int i = 0; i < stateCodes.length; i++) {
            if (stateCodes[i].equals(state)) {
                stateIndex = i;
                break;
            }
        }

        if (stateIndex == -1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GST is not valid. Please provide correct Gst, State code is not valid");
        }

//        String stateCode = stateCodes[stateIndex];

        if (!(gst.charAt(gst.length() - 2) == 'Z')) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "GST is not valid. It must contain 'Z' by default at the 14th (index = 13) position");
        }
        return true;
    }

    public static LocalDateTime convertDate(String customDateTime) {
        if (StringUtils.isNotEmpty(customDateTime)) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(customDateTime, dateFormatter);
            LocalTime localTime = LocalTime.of(0, 0); // Set the time
            return LocalDateTime.of(localDate, localTime);
        }
        return null;
    }

    public static LocalDateTime convertDateWithHours(String customDateTime) {
        if (StringUtils.isNotEmpty(customDateTime)) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            LocalDateTime istTime = LocalDateTime.parse(customDateTime, dateTimeFormatter);
            // IST timezone
            ZoneId istZoneId = ZoneId.of("Asia/Kolkata");
            // Convert LocalDateTime from IST to ZonedDateTime
            ZonedDateTime zonedDateTimeIST = ZonedDateTime.of(istTime, istZoneId);
            // UTC timezone
            ZoneId utcZoneId = ZoneId.of("UTC");
            // Convert ZonedDateTime from IST to UTC
            ZonedDateTime zonedDateTimeUTC = zonedDateTimeIST.withZoneSameInstant(utcZoneId);
            // Extract the LocalDateTime in UTC
            LocalDateTime localDateTimeUTC = zonedDateTimeUTC.toLocalDateTime();
            return localDateTimeUTC;
        }
        return null;
    }

    public static String convertToCustomDateTime(LocalDateTime localDateTimeUTC) {
        if (localDateTimeUTC != null) {
            // UTC timezone
            ZoneId utcZoneId = ZoneId.of("UTC");
            // Convert LocalDateTime from UTC to ZonedDateTime
            ZonedDateTime zonedDateTimeUTC = localDateTimeUTC.atZone(utcZoneId);
            // IST timezone
            ZoneId istZoneId = ZoneId.of("Asia/Kolkata");
            // Convert ZonedDateTime from UTC to IST
            ZonedDateTime zonedDateTimeIST = zonedDateTimeUTC.withZoneSameInstant(istZoneId);
            // Format the ZonedDateTime in customDateTime format
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            String customDateTime = zonedDateTimeIST.format(dateTimeFormatter);
            return customDateTime;
        }
        return null;
    }

    public static LocalDateTime convertDateWithHoursAndMinute(String customDateTime) {
        if (StringUtils.isNotEmpty(customDateTime)) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            LocalDateTime istTime =  LocalDateTime.parse(customDateTime, dateTimeFormatter);
            // IST timezone
            ZoneId istZoneId = ZoneId.of("Asia/Kolkata");
            // Convert LocalDateTime from IST to ZonedDateTime
            ZonedDateTime zonedDateTimeIST = ZonedDateTime.of(istTime, istZoneId);
            // UTC timezone
            ZoneId utcZoneId = ZoneId.of("UTC");
            // Convert ZonedDateTime from IST to UTC
            ZonedDateTime zonedDateTimeUTC = zonedDateTimeIST.withZoneSameInstant(utcZoneId);
            // Extract the LocalDateTime in UTC
            LocalDateTime localDateTimeUTC = zonedDateTimeUTC.toLocalDateTime();
            return localDateTimeUTC;
        }
        return null;
    }

    public static LocalDateTime convertDateWithSlash(String customDateTime) {
        if (StringUtils.isNotEmpty(customDateTime)) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(customDateTime, dateFormatter);
            LocalTime localTime = LocalTime.of(0, 0); // Set the time
            return LocalDateTime.of(localDate, localTime);
        }
        return null;
    }

    public static String localDateTimeToStringWithSlash(LocalDateTime localDateTime) {
        LocalDateTime l = convertToIST(localDateTime);
        if (Objects.nonNull(l)) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return l.format(dateTimeFormatter);
        }
        return "";
    }

    public static String convertFirstCharToUppercase(String input) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }

        char firstChar = Character.toUpperCase(input.charAt(0));
        String restOfString = input.substring(1);

        return firstChar + restOfString;
    }
    public static String convertToSentenceCase(String paragraph) {
        try {
            String[] sentences = paragraph.split("\\.\\s*");
            StringBuilder result = new StringBuilder();

            for (String sentence : sentences) {
                if (!sentence.isEmpty()) {
                    String sentenceStart = sentence.substring(0, 1).toUpperCase();
                    String sentenceRest = sentence.substring(1).toLowerCase();
                    String convertedSentence = sentenceStart + sentenceRest;
                    result.append(convertedSentence).append(". ");
                }
            }

            return result.toString().trim();
        } catch (Exception ex) {
            return paragraph;
        }
    }

    public static LocalDateTime convertToIST(LocalDateTime utcTime) {
        try {
            // UTC timezone
            ZoneId utcZoneId = ZoneId.of("UTC");
            // Convert LocalDateTime from UTC to ZonedDateTime
            ZonedDateTime zonedDateTimeUTC = utcTime.atZone(utcZoneId);
            // IST timezone
            ZoneId istZoneId = ZoneId.of("Asia/Kolkata");
            // Convert ZonedDateTime from UTC to IST
            ZonedDateTime zonedDateTimeIST = zonedDateTimeUTC.withZoneSameInstant(istZoneId);
            // Extract the LocalDateTime in IST
            LocalDateTime localDateTimeIST = zonedDateTimeIST.toLocalDateTime();

            return localDateTimeIST;
        } catch (Exception ex) {
            return utcTime;
        }
    }

    public static String convertToEDDFormat(LocalDateTime dateTime) {
        LocalDateTime date = convertToIST(dateTime);
        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d' 'MMM' 'uuuu");

        // Format the LocalDateTime object using the formatter
        String formattedDateTime = date.format(formatter);

        // Extract the day suffix (e.g., 1st, 2nd, 3rd, etc.)
        int dayOfMonth = dateTime.getDayOfMonth();
        String daySuffix;
        if (dayOfMonth >= 11 && dayOfMonth <= 13) {
            daySuffix = "TH";
        } else {
            switch (dayOfMonth % 10) {
                case 1:
                    daySuffix = "ST";
                    break;
                case 2:
                    daySuffix = "ND";
                    break;
                case 3:
                    daySuffix = "RD";
                    break;
                default:
                    daySuffix = "TH";
                    break;
            }
        }

        // Add the day suffix to the formatted date
        formattedDateTime = formattedDateTime.replaceFirst("-", daySuffix + " ");

        return formattedDateTime;
    }

    public static String localDateTimeToStringDateTime(LocalDateTime localDateTime, String dateFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        return localDateTime.format(dateTimeFormatter);
    }

    public static boolean areDatesEqualWithTolerance(Date date1, Date date2, long tolerance) {
        long timeDifference = Math.abs(date1.getTime() - date2.getTime());
        return timeDifference <= tolerance;
    }

    public static boolean areDatesEqualWithTolerance(Date date1, LocalDateTime date2, long tolerance) {
        long timeDifference = Math.abs(date1.getTime() - date2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return timeDifference <= tolerance;
    }

    public static boolean areDatesEqualWithTolerance(LocalDateTime date2, Date date1, long tolerance) {
        long timeDifference = Math.abs(date1.getTime() - date2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return timeDifference <= tolerance;
    }

    private boolean isAdmin(ClientEntity clientEntity, String companyId){
        for(UserCompanyRole role : clientEntity.getCompanyRoles()){
            if(StringUtils.equalsIgnoreCase(companyId, role.getCompanyID()) && role.getRole() == UserRole.ADMIN)
                return true;
        }
        return false;
    }
}
