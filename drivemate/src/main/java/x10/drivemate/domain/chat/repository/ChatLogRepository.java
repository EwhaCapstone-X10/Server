package x10.drivemate.domain.chat.repository;

import x10.drivemate.domain.chat.entity.ChatLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

    @Query("SELECT c FROM ChatLog c WHERE FUNCTION('YEAR', c.date) = :year")
    Page<ChatLog> findByYear(@Param("year") int year, Pageable pageable);
}
