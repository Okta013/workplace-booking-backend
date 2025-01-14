package com.anikeeva.traineeship.workplacebooking.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="workplaces")
public class WorkplaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="number")
    private int number;

    @Column(name="description")
    private String description;

    @Column(name="workspace_id")
    private UUID workspaceId;

    @Column(name="is_deleted")
    private Boolean isDeleted;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="workplace_id")
    private List<BookingEntity> bookings;

    public WorkplaceEntity(int number, String description, UUID workspaceId, boolean isDeleted) {
        this.number = number;
        this.description = description;
        this.workspaceId = workspaceId;
        this.isDeleted = isDeleted;
    }

    public WorkplaceEntity(UUID id, int number, String description, UUID workspaceId, boolean isDeleted) {
        this.id = id;
        this.number = number;
        this.description = description;
        this.workspaceId = workspaceId;
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Workplace number " + number;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + number;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((workspaceId == null) ? 0 : workspaceId.hashCode());
        result = prime * result + (isDeleted ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkplaceEntity that)) return false;
        return number == that.number &&
                isDeleted == that.isDeleted &&
                Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                Objects.equals(workspaceId, that.workspaceId);
    }
}