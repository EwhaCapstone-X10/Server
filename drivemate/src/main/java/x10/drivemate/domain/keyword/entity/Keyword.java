package x10.drivemate.domain.keyword.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "Keyword", indexes = {
        @Index(name = "idx_keywords_name", columnList = "name"),
        @Index(name = "idx_keywords_field", columnList = "field")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long keywordId;

    @Enumerated(EnumType.STRING)
    @Column(name = "field", nullable = false)
    private Field field;

    @Column(name = "name")
    private String name;

    public enum Field {
        ENTERTAINMENT_ARTS, SPORTS_ACTIVITY, HOBBY, STUDY_DEVELOPMENT, OTHERS; // 한글 x
    }

}
