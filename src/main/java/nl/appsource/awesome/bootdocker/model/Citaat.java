package nl.appsource.awesome.bootdocker.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Wither;

/**
 * 
 * Citaat.
 *
 */

@Entity
@Getter
@Setter
@Wither
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, value = "new")
public class Citaat extends Persistable {

    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "spreker", nullable = false)
    private Spreker spreker;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categorie", nullable = false)
    private Categorie categorie;

    public Citaat(final Long id) {
        super(id);
    }

    public Citaat(final Long id, final String name) {
        this(id);
        setName(name);
    }

    @Override
    public String toString() {
        return name;
    }
    
}
