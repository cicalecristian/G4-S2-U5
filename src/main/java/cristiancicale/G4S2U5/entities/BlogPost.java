package cristiancicale.G4S2U5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Random;

@Entity
@Table(name = "blog_posts")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String titolo;

    private String cover;

    @Column(nullable = false)
    private String contenuto;

    @Column(name = "tempo_di_lettura", nullable = false)
    private int tempoDiLettura;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public BlogPost(String categoria, String titolo, String contenuto, int tempoDiLettura) {
        this.categoria = categoria;
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.tempoDiLettura = tempoDiLettura;
        this.cover = "https://picsum.photos/200/300";
        Random random = new Random();
        this.id = random.nextInt(1, 1000);
    }
}
