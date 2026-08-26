package requestflow.statushistory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import requestflow.request.Request;
import requestflow.request.RequestStatus;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "status_history")
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status")
    private RequestStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    private RequestStatus newStatus;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;
}
