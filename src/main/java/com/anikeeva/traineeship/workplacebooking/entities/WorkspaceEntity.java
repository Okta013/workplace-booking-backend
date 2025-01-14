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
@Table(name="workspaces")
public class WorkspaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="name")
    private String name;

    @Column(name="floor_number")
    private short floorNumber;

    @Column(name="room_number")
    private short roomNumber;

    @Column(name="is_deleted")
    private Boolean isDeleted;

    @Column(name="office_id")
    private UUID officeId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "workspace_id")
    private List<WorkplaceEntity> workplaces;

    public WorkspaceEntity(UUID id, String name, short floorNumber, short roomNumber, boolean isDeleted, UUID officeId) {
        this.id = id;
        this.name = name;
        this.floorNumber = floorNumber;
        this.roomNumber = roomNumber;
        this.isDeleted = isDeleted;
        this.officeId = officeId;
    }

    public WorkspaceEntity(String name, short floorNumber, short roomNumber, boolean isDeleted, UUID officeId) {
        this.name = name;
        this.floorNumber = floorNumber;
        this.roomNumber = roomNumber;
        this.isDeleted = isDeleted;
        this.officeId = officeId;
    }

    @Override
    public String toString() {
        return "WorkSpace " + name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + floorNumber;
        result = prime * result + roomNumber;
        result = prime * result + ((officeId == null) ? 0 : officeId.hashCode());
        result = prime * result + (isDeleted ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkspaceEntity)) return false;
        WorkspaceEntity that = (WorkspaceEntity) o;
        return floorNumber == that.floorNumber &&
                roomNumber == that.roomNumber &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(isDeleted, that.isDeleted) &&
                Objects.equals(officeId, that.officeId);
    }
}