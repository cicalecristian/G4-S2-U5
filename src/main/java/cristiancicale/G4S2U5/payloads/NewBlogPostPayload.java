package cristiancicale.G4S2U5.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class NewBlogPostPayload {

    private String categoria;
    private String titolo;
    private String contenuto;
    private int tempoDiLettura;
    private long authorId;
}
