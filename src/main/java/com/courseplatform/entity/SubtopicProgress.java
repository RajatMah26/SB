package com.courseplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subtopic_progress", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "subtopic_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubtopicProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subtopic_id", nullable = false)
    @JsonIgnore
    private Subtopic subtopic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    @JsonIgnore
    private Enrollment enrollment;

    @Column(nullable = false)
    @Builder.Default
    private Boolean completed = false;

    private LocalDateTime completedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        if (completed && completedAt == null) {
            completedAt = LocalDateTime.now();
        }
    }
}
