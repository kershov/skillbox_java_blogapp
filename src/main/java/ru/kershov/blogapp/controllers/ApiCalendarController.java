package ru.kershov.blogapp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kershov.blogapp.model.dto.CalendarDTO;
import ru.kershov.blogapp.services.CalendarService;
import ru.kershov.blogapp.utils.JsonViews;

@RestController
@RequestMapping("/api/calendar")
public class ApiCalendarController {
    @Autowired
    CalendarService calendarService;

    @GetMapping(value="", produces = "application/json")
    @JsonView(JsonViews.IdName.class)
    public CalendarDTO getCalendar(
            @RequestParam(name="year", required = false) String year) {
        return calendarService.getCalendar(year);
    }
}
