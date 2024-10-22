package com.yurjinia.platform.project_structure.comment.entity;

import com.yurjinia.platform.project_structure.ticket.entity.TicketEntity;
import com.yurjinia.platform.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private TicketEntity ticket;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false)
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @PrePersist
    protected void onCreate() {
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = LocalDateTime.now();
    }

}
