package com.qgtest.diary.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AIService {
    private final ChatClient chatClient;

    public AIService(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder.build();
    }
    // 普通对话
    public String chat(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }
    // 生成笔记摘要
    public String generateSummary(String noteContent) {
        return chatClient.prompt()
                .system("你是专业的笔记摘要生成助手")
                .user("请为以下笔记生成摘要（50字以内）\n" +
                        "笔记内容: \n"+noteContent)
                .call()
                .content();
    }
    public String generateKeyPoint(String noteContent) {
        return chatClient.prompt()
                .system("你是专业的笔记要点生成助手")
                .user("请为以下笔记提取3-5个要点，每点15字以内，用逗号分隔：\n" +
                        "笔记内容: \n"+noteContent)
                .call()
                .content();
    }
    public String generateTags(String noteContent) {
        return chatClient.prompt()
                .system("你是专业的笔记标签生成助手")
                .user("请为以下笔记推荐3-5个标签，用逗号分隔：\n" +
                        "笔记内容: \n"+noteContent)
                .call()
                .content();
    }
    //后续可以做一下流式传输
}
