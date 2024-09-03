package com.greaticker.demo.service.project;

import com.greaticker.demo.constants.enums.history.HistoryKind;
import com.greaticker.demo.constants.enums.project.ProjectState;
import com.greaticker.demo.model.history.History;
import com.greaticker.demo.model.project.Project;
import com.greaticker.demo.repository.history.HistoryRepository;
import com.greaticker.demo.repository.project.ProjectRepository;
import com.greaticker.demo.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import com.greaticker.demo.model.user.User;

import java.util.Optional;

import static com.greaticker.demo.constants.KafkaTopicNames.USERS_TO_REFRESH_PROJECT;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectKafkaConsumerService {
    private static final int MAX_RETRY_ATTEMPTS = 1;
    private int retryCount = 0;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ProjectRepository projectRepository;
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final ProjectService projectService;

    @KafkaListener(topics = USERS_TO_REFRESH_PROJECT, groupId = "myGroup")
    public void listen(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try {
            System.out.println("Kafka Listen And Start Event");
            String refreshTargetUserId = record.value();
            projectService.processRefreshTargetUser(refreshTargetUserId);
            acknowledgment.acknowledge();
            retryCount = 0; // reset retry count after successful processing
        } catch (Exception e) {
            if (retryCount < MAX_RETRY_ATTEMPTS) {
                retryCount++;
                listen(record, acknowledgment); // retry processing
            } else {
                // Send to Dead Letter Queue
                sendToDeadLetterQueue(record);
                acknowledgment.acknowledge(); // mark original message as processed
                retryCount = 0; // reset retry count
            }
        }
    }



    private void processMessage(String message) {
        // Add actual message processing logic here
        // Throw an exception if processing fails
        throw new RuntimeException("Processing failed");
    }

    private void sendToDeadLetterQueue(ConsumerRecord<String, String> failedRecord) {
        System.out.println("Sending to DLQ: " + failedRecord.value());
        kafkaTemplate.send("yourTopic.DLQ", failedRecord.value());
    }
}
