package io.github.talelin.latticy.dto.book;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@Data
@NoArgsConstructor
public class CreateOrUpdateBookDTO {

    @NotEmpty(message = "{book.title.not-empty}")
    @Size(max = 50, message = "{book.title.size}")
    private String title;

    @NotEmpty(message = "{book.author.not-empty}")
    @Size(max = 50, message = "{book.author.size}")
    private String author;

    @NotEmpty(message = "{book.summary.not-empty}")
    @Size(max = 1000, message = "{book.summary.size}")
    private String summary;

    @Size(max = 100, message = "{book.image.size}")
    private String image;
}
