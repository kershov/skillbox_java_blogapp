package ru.kershov.blogapp.repositories.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CalendarRepository implements CalendarRepositoryInterface {
    @Autowired
    private EntityManager em;

    final static String WHERE = "WHERE p.isActive = 1 " +
                                "AND p.moderationStatus = 'ACCEPTED' " +
                                "AND p.time <= NOW() ";

    final String QUERY_YEARS = "SELECT YEAR(p.time) as years " +
                               "FROM Post p %s " +
                               "GROUP BY years " +
                               "ORDER BY p.time DESC";

    final String QUERY_POSTS = "SELECT " +
                               "    DATE_FORMAT(p.time,'%%Y-%%m-%%d') as post_date, " +
                               "    count(*) as num_posts " +
                               "FROM Post p %s " +
                               "GROUP BY post_date ";

    @Override
    public List<Integer> findAllYears(String year) {
        return em.createQuery(getQuery(year, QUERY_YEARS), Integer.class).getResultList();
    }

    @Override
    public Map<String, Long> findAllPosts(String year) {
        return em.createQuery(getQuery(year, QUERY_POSTS), Tuple.class).getResultStream()
                .collect(
                    Collectors.toMap(
                        tuple -> tuple.get("post_date").toString(),
                        tuple -> (Long) tuple.get("num_posts"))
                );
    }

    private static String getQuery(String year, String query) {
        return (year == null || year.isEmpty()) ?
                String.format(query, WHERE) :
                String.format(query, WHERE + "AND YEAR(p.time) = " + year);
    }
}
