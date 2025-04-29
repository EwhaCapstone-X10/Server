package x10.drivemate.domain.chat.service;

import x10.drivemate.common.exception.GeneralException;
import x10.drivemate.domain.member.entity.Member;
import x10.drivemate.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import x10.drivemate.common.response.ApiResponse;
import x10.drivemate.common.response.PageInfo;
import x10.drivemate.common.status.ErrorStatus;
import x10.drivemate.common.status.SuccessStatus;
import x10.drivemate.domain.chat.dto.ChatRequestDto;
import x10.drivemate.domain.chat.dto.ChatResponseDto;
import x10.drivemate.domain.chat.entity.ChatLog;
import x10.drivemate.domain.chat.entity.ChatMessage;
import x10.drivemate.domain.chat.repository.ChatLogRepository;
import x10.drivemate.domain.chat.repository.ChatMessageRepository;
import x10.drivemate.global.security.CustomUserPrincipal;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final MemberRepository memberRepository;
    private final ChatLogRepository chatLogRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public ChatResponseDto.ChatLogResultDto saveChatLog(ChatRequestDto.ChatLogDto request, CustomUserPrincipal userPrincipal) {
        Member member = memberRepository.findById(userPrincipal.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        ChatLog chatLog = ChatLog.builder()
                .member(member)
                .date(request.getDate())
                .build();
        ChatLog savedChatLog = chatLogRepository.save(chatLog);

        List<ChatMessage> messages = request.getChatting().stream()
                .map(chatMessageDto -> ChatMessage.builder()
                        .idx(chatMessageDto.getIdx())
                        .role(chatMessageDto.getRole())
                        .chat(chatMessageDto.getChat())
                        .chatLog(savedChatLog)
                        .build())
                .collect(Collectors.toList());
        chatMessageRepository.saveAll(messages);

        List<ChatRequestDto.ChatMessageDto> chatMessageDtos = messages.stream()
                .map(msg -> new ChatRequestDto.ChatMessageDto(msg.getRole(), msg.getChat(), msg.getIdx()))
                .collect(Collectors.toList());

        return ChatResponseDto.ChatLogResultDto.builder()
                .chatId(chatLog.getChatLogId())
                .build();
    }

    @Override
    public void saveChatSummary(ChatRequestDto.ChatSummaryDto request) {
        ChatLog chatLog = chatLogRepository.findById(request.getChatId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHAT_NOT_FOUND));

        chatLog.setSummary(request.getSummary());
        chatLog.setKeywords(request.getKeywords());
        chatLogRepository.save(chatLog);
    }

    @Override
    public ChatResponseDto.ChatResultDto getChat(Long chatId, CustomUserPrincipal userPrincipal) {

        Member member = memberRepository.findById(userPrincipal.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        ChatLog chatLog = chatLogRepository.findById(chatId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHAT_NOT_FOUND));

        // 조회 권한 확인
        if (!member.equals(chatLog.getMember())) {
            throw new GeneralException(ErrorStatus.CHAT_FORBIDDEN);
        }

        List<ChatMessage> messages = chatLog.getChatting();
        List<ChatRequestDto.ChatMessageDto> chatMessageDtos = messages.stream()
                .map(msg -> new ChatRequestDto.ChatMessageDto(msg.getRole(), msg.getChat(), msg.getIdx()))
                .collect(Collectors.toList());

        return ChatResponseDto.ChatResultDto.builder()
                .chatId(chatLog.getChatLogId())
                .date(chatLog.getDate())
                .summary(chatLog.getSummary())
                .keywords(chatLog.getKeywords())
                .chatting(chatMessageDtos)
                .build();
    }

    @Override
    public void deleteChat(Long chatId, CustomUserPrincipal userPrincipal) {

        Member member = memberRepository.findById(userPrincipal.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        ChatLog chatLog = chatLogRepository.findById(chatId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHAT_NOT_FOUND));

        if (!member.equals(chatLog.getMember())) {
            throw new GeneralException(ErrorStatus.CHAT_FORBIDDEN);
        }

        chatLogRepository.delete(chatLog);
    }

    @Override
    public ResponseEntity<ApiResponse> getChatList(Pageable pageable, Integer year, CustomUserPrincipal userPrincipal) {
        Page<ChatLog> chatPage = chatLogRepository.findByYearAndMember(year, userPrincipal.getMemberId(), pageable);

        PageInfo pageInfo = new PageInfo(chatPage.getNumber(), chatPage.getSize(),
                chatPage.hasNext(), chatPage.getTotalElements(), chatPage.getTotalPages());

        List<ChatResponseDto.ChatListDto> chatListDtos = chatPage.getContent().stream()
                .map(chat -> ChatResponseDto.ChatListDto.builder()
                        .chatId(chat.getChatLogId())
                        .summary(chat.getSummary())
                        .date(chat.getDate())
                        .build())
                .collect(Collectors.toList());


        return ApiResponse.onSuccess(SuccessStatus._OK, pageInfo, chatListDtos);
    }
}
