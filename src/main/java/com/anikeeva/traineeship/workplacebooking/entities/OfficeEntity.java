package com.anikeeva.traineeship.workplacebooking.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "offices")
public class OfficeEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "address")
    private String address;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "office_id")
    private List<WorkspaceEntity> workspaceEntities;

    public OfficeEntity(UUID id, String address, String name, boolean isDeleted) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public OfficeEntity(String address, String name, boolean isDeleted) {
        this.address = address;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Office " + name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + (isDeleted ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((workspaceEntities == null) ? 0 : workspaceEntities.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfficeEntity that)) return false;
        return isDeleted == that.isDeleted &&
               Objects.equals(id, that.id) &&
               Objects.equals(address, that.address) &&
               Objects.equals(name, that.name);
    }
}
