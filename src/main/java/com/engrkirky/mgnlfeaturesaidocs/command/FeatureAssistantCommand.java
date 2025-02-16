package com.engrkirky.mgnlfeaturesaidocs.command;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.Resource;
import org.springframework.shell.command.annotation.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command
public class FeatureAssistantCommand {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    @Value("classpath:/prompts/feature-reference.st")
    private Resource featurePromptTemplate;

    @Autowired
    public FeatureAssistantCommand(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @Command(command = "q")
    public String question(@DefaultValue(value = "What is Magnolia CMS") String message) {
        PromptTemplate promptTemplate = new PromptTemplate(featurePromptTemplate);
        Map<String, Object> promptParameters = new HashMap<>();
        promptParameters.put("input", message);
        promptParameters.put("documents", String.join("\n", findSimilarDocuments(message)));
        Prompt prompt = promptTemplate.create(promptParameters);

        return chatClient.prompt()
                .user(prompt.toString())
                .call()
                .content();
    }

    private List<String> findSimilarDocuments(String message) {
        SearchRequest searchRequest = SearchRequest.builder().query(message).topK(3).build();
        List<Document> similarDocuments = vectorStore.similaritySearch(searchRequest);
        return similarDocuments.stream().map(Document::getContent).toList();
    }
}
