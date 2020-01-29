package ru.kershov.blogapp.repositories.calendar;

import java.util.List;
import java.util.Map;

public interface CalendarRepositoryInterface {
    /*
     * Custom Interface to get all the Years & Posts with help of my own custom CalendarRepository.
     * Can extend any other interface if needed.
     *
     * Example:
     *   @Repository public interface SomeRepository extends JpaRepository<Entity, Long>,
     *                                                       CalendarRepositoryInterface
     */
    List<Integer> findAllYears(String year);
    Map<String, Long> findAllPosts(String year);
}
