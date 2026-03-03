package com.octaviookumu.tickets.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // not in keycloak. will be generated

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start")
    private LocalDate start;

    @Column(name = "end")
    private LocalDate end;

    @Column(name = "venue", nullable = false)
    private String venue;

    // when the event tickets go on sale
    @Column(name = "sales_start")
    private LocalDate salesStart;

    @Column(name = "sales_end")
    private LocalDate salesEnd;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING) // defaults to string (representation of the enum will be stored in the db)
    private EventStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    // organizer can organize many events. LAZY is for efficiency reasons (helps combat the 'n+1 problem')
    @JoinColumn(name = "organizer_id") // will reference the id of the organizer, which is a user
    private User organizer; // it is typical for the many side of the relationship to have the configuration (building with JPA)

    // not putting the configuration/ all the info inside the event
    // it makes sense for the user to own this relationship
    @ManyToMany(mappedBy = "attendingEvents") // Tells Hibernate: "I am not in charge of the relationship.
    // Look at the attendingEvents field in the User class to find the configuration."
    private List<User> attendees = new ArrayList<>(); // helps prevent nullPointerExceptions

    @ManyToMany(mappedBy = "staffingEvents")
    private List<User> staff = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<TicketType> ticketTypes = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // when generating the equals & hashcode methods:
    // I excluded the entities (potentially)
    // This will help us to sidestep those stack overflow errors from that bidirectional relationship

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(start, event.start) && Objects.equals(end, event.end) && Objects.equals(venue, event.venue) && Objects.equals(salesStart, event.salesStart) && Objects.equals(salesEnd, event.salesEnd) && status == event.status && Objects.equals(createdAt, event.createdAt) && Objects.equals(updatedAt, event.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, start, end, venue, salesStart, salesEnd, status, createdAt, updatedAt);
    }
}
