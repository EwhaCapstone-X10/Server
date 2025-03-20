package x10.drivemate.domain.member.entity;

import x10.drivemate.domain.chat.entity.ChatLog;
import x10.drivemate.domain.memberKeyword.entity.MemberKeyword;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Member", indexes = {
        @Index(name = "idx_member_email", columnList = "email")
})
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    //@Column(name = "password", nullable = false)
    //private String password;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private MemberSex sex;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "mode")
    @Enumerated(EnumType.STRING)
    private Mode mode;

    @Column(name = "login_status")
    @Enumerated(EnumType.STRING)
    private LoginStatus loginStatus;

    @Column(nullable = false, unique = true)
    private String kakaoId;  // 카카오 고유 ID

    @Column(nullable = false)
    private String nickname;

    @Column
    private String profileImageUrl;

    @CreatedDate  // 생성 시간 자동 설정
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정 시간 자동 설정
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "member")
    private List<MemberKeyword> memberKeywords;

    @OneToMany(mappedBy = "member")
    private List<ChatLog> chats;

}
