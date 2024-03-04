package id.segari.ortools.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/v1/applications")
public class ApplicationController {
    private final String buildAt;

    public ApplicationController() {
        buildAt = ZonedDateTime.now(ZoneId.of("UTC+7")).format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @GetMapping("/versions")
    public String getVersions(){
        return "build at: " + buildAt;
    }
}
