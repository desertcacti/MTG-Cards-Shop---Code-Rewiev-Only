package pl.desertcacti.mtgcardsshopsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Objects;

/**
 * MediaEntity class represents a media file in the system.
 */
@Entity
@Table(name = "media")
@Getter
@Setter
@NoArgsConstructor
public class MediaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the media entity

    private String fileId;

    public String getFileId() {
        return fileId;
    }

    @Column(nullable = false, unique = true)
    private String fileName;  // The name of the file, must be unique

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaEntity that = (MediaEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName);
    }

    @Override
    public String toString() {
        return "MediaEntity{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}