package x10.drivemate.domain.stretching.repository;

import x10.drivemate.domain.stretching.entity.Stretching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StretchingRepository extends JpaRepository<Stretching, Long> {
}
