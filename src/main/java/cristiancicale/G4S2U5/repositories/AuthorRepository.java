package cristiancicale.G4S2U5.repositories;

import cristiancicale.G4S2U5.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByEmail(String email);
}
