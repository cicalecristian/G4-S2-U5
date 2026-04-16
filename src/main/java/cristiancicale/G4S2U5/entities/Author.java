package cristiancicale.G4S2U5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "authors")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false)
    private String email;

    @Column(name = "data_di_nascita", nullable = false)
    private LocalDate dataDiNascita;

    private String avatar;

    @OneToMany(mappedBy = "author")
    private List<BlogPost> blogPosts = new ArrayList<>();

    public Author(String nome, String cognome, String email, LocalDate dataDiNascita) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.dataDiNascita = dataDiNascita;
        this.avatar = "https://ui-avatars.com/api/?name=" + nome + "+" + cognome;
        Random random = new Random();
        this.id = random.nextInt(1, 1000);
    }
}
