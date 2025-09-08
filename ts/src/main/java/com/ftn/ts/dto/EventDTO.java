package com.ftn.ts.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class EventDTO {

    @NotBlank(message = "Event name is required")
    @Size(max = 150, message = "Event name cannot exceed 150 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Max participants is required")
    @Min(value = 1, message = "Event must have at least 1 participant")
    @Max(value = 10000, message = "Max participants cannot exceed 10,000")
    private Integer maxParticipants;

    @NotBlank(message = "Privacy type is required")
    @Pattern(
            regexp = "^(OPEN|CLOSED)$",
            message = "Privacy type must be OPEN or CLOSED"
    )
    private String privacyType;

    @NotBlank(message = "Location is required")
    @Size(max = 255, message = "Location cannot exceed 255 characters")
    private String location;

    @NotBlank(message = "Event date is required")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}([ T]\\d{2}:\\d{2}(:\\d{2})?)?$",
            message = "Event date must be in format yyyy-MM-dd or yyyy-MM-ddTHH:mm"
    )
    private String eventDate;

    @NotNull(message = "Event type is required")
    private Long eventTypeId;
}
