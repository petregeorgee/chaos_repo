package image.encrypt.decrypt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="image")
public class Image
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable=false, unique=true)
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "image", unique = false, nullable = false, length = 100000000)
    private byte[] image;

    @Column(name="username", nullable = false)
    private String username;

    @Column(name="date")
    private Date dateUploaded;

    @Override
    public String toString()
    {
        return "Image{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
