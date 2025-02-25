package x10.drivemate.domain.chat.entity;

import x10.drivemate.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ChatLogId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private LocalDateTime date;

    private String summary;

    private String keywords;

    @OneToMany(mappedBy = "chatLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatting;
}
