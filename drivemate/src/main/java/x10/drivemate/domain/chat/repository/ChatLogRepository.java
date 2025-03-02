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

    @Query("SELECT c FROM ChatLog c JOIN c.member m WHERE FUNCTION('YEAR', c.date) = :year AND m.memberId = :memberId")
    Page<ChatLog> findByYearAndMember(@Param("year") int year, @Param("memberId") Long memberId, Pageable pageable);
}
