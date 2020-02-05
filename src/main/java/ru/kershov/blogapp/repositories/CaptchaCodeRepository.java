package ru.kershov.blogapp.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kershov.blogapp.model.CaptchaCode;

import javax.transaction.Transactional;
import java.time.Instant;

@Repository
@Transactional
public interface CaptchaCodeRepository extends CrudRepository<CaptchaCode, Long> {
    @Modifying
    void deleteByTimeBefore(Instant time);

    CaptchaCode findBySecretCode(String secretCode);
}
