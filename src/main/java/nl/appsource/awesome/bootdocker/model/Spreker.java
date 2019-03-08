package nl.appsource.awesome.bootdocker.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Wither;

/**
 * 
 * Spreker.
 *
 */

@Entity
@Getter
@Setter
@Wither
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, value = "new")
public class Spreker extends Persistable {

    @NotNull
    @Size(max = 255)
    private String name;

    public Spreker(final Long id, final String name) {
        super(id);
        setName(name);
    }
    
}
