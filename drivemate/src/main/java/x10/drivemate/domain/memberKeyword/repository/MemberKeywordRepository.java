package x10.drivemate.domain.memberKeyword.repository;

import x10.drivemate.domain.member.entity.Member;
import x10.drivemate.domain.memberKeyword.entity.MemberKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberKeywordRepository extends JpaRepository<MemberKeyword, Long> {
    void deleteAllByMember(Member member);

    List<MemberKeyword> findAllByMember(Member member);
}
