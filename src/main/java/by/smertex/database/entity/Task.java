package by.smertex.database.entity;

import by.smertex.database.entity.enums.Priority;
import by.smertex.database.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"comments", "metaInfo", "performer"})
@Builder
@Entity
@Table(name = "task")
public class Task implements BaseEntity<UUID>{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metainfo_id")
    private Metainfo metaInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id")
    private User performer;

    @Builder.Default
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
}
