package org.example.product;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Getter
@Builder
public class Product {
    private static final Pattern VALID_PART_NUMBER_REGEX = Pattern.compile("^C-[0-9]{4}-[0-9]{4}$");

    private String id;

    private String partNumber;

    private LocalDate firstAvailabilityDate;

    private LocalDate unPublicationDate;

    public boolean isActive() {
        final LocalDate currentTime = LocalDate.now();
        return currentTime.isAfter(firstAvailabilityDate) && currentTime.isBefore(unPublicationDate);
    }

    public boolean isValidPartNumber() {
        return VALID_PART_NUMBER_REGEX.matcher(this.partNumber).matches();
    }
}
