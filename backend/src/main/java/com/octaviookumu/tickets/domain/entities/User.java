package com.octaviookumu.tickets.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
// problem with using @Data for the case of bidirectional relationships
// using @Data could lead to a stack overflow exception
// it is better to specify the annotations we need rather than use @Data
// and then manually implement our equals and hashcode methods
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id; // will be specified in keycloak

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
    // the info about this relationship can be found on the instance variable organizer inside of event
    // CascadeType.ALL - any changes made to events the user references is saved.
    // Be CAREFUL with CascadeType.ALL because if we delete the user all events associated with them are deleted
    // in a real system consider soft delete
    private List<Event> organizedEvents = new ArrayList<>();

    @ManyToMany
    // For a (ManyToMany - there isn't a convention. Goes with whichever entity owns the relationship)
    @JoinTable(
            name = "user_attending_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )  // create a third table called user_attending_events with two columns: user_id and event_id
    // The name of the join table in the DB
    // user_id - Foreign Key to the User table
    // event_id - Foreign Key to the Event table
    private List<Event> attendingEvents = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_staffing_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    ) // In a @JoinTable configuration,
    // inverseJoinColumns defines the foreign key (FK) column in the join table that points to the non-owning (inverse) entity
    // While joinColumns links the join table to the "Owner" (e.g., User), inverseJoinColumns links it to the "Target" (e.g., Event).
    private List<Event> staffingEvents = new ArrayList<>();

    // Audit fields. to know when an entity has been created and updated
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(createdAt, user.createdAt) && Objects.equals(updatedAt, user.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, createdAt, updatedAt);
    }
}
