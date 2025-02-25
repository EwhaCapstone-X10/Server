package x10.drivemate.domain.chat.dto;

import x10.drivemate.domain.chat.entity.Role;
import x10.drivemate.domain.member.entity.MemberSex;
import x10.drivemate.domain.member.entity.Mode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ChatRequestDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageDto {

        @NotNull(message = "역할(user or gpt)은 필수입니다.")
        private Role role;

        private String chat;

        @NotNull(message = "인덱스값은 필수입니다.")
        private Integer idx;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatLogDto {
        private Long memberId;
        private LocalDateTime date;

        @Valid // 리스트 내부의 요소까지 검증하도
        @NotNull(message = "채팅 리스트는 필수입니다.")
        private List<ChatMessageDto> chatting;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatSummaryDto {
        @NotNull(message = "chatId는 필수입니다.")
        private Long chatId;

        private String summary;
        private String keywords;

    }
}
