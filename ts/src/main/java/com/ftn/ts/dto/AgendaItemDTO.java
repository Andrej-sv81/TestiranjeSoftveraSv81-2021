package com.ftn.ts.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
public class AgendaItemDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Start time is required")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}([ T]\\d{2}:\\d{2}(:\\d{2})?)?$",
            message = "Start time must be in format yyyy-MM-dd or yyyy-MM-ddTHH:mm"
    )
    private String startTime;

    @NotBlank(message = "End time is required")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}([ T]\\d{2}:\\d{2}(:\\d{2})?)?$",
            message = "End time must be in format yyyy-MM-dd or yyyy-MM-ddTHH:mm"
    )
    private String endTime;

    @NotBlank(message = "Location is required")
    @Size(max = 255, message = "Location cannot exceed 255 characters")
    private String location;
}
