package x10.drivemate.domain.chat.service;

import x10.drivemate.common.response.ApiResponse;
import x10.drivemate.domain.chat.dto.ChatRequestDto;
import x10.drivemate.domain.chat.dto.ChatResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChatService {
    ChatResponseDto.ChatLogResultDto saveChatLog(ChatRequestDto.ChatLogDto request);
    void saveChatSummary(ChatRequestDto.ChatSummaryDto request);
    ChatResponseDto.ChatResultDto getChat(Long chatId);
    void deleteChat(Long chatId);
    ResponseEntity<ApiResponse> getChatList(Pageable pageable, Integer year, Long memberId);
}
