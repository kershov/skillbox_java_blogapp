package ru.kershov.blogapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kershov.blogapp.model.dto.CalendarDTO;
import ru.kershov.blogapp.repositories.calendar.CalendarRepository;

import java.util.List;
import java.util.Map;

@Service
public class CalendarService {
    @Autowired
    private CalendarRepository calendarRepository;

    public CalendarDTO getCalendar(String year) {
        // TODO: Check if year doesn't exist >> 404
        List<Integer> years = calendarRepository.findAllYears(year);
        Map<String, Long> posts = calendarRepository.findAllPosts(year);

        return new CalendarDTO(years, posts);
    }
}
