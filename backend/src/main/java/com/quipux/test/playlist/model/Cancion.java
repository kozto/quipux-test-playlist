package com.quipux.test.playlist.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.FetchType;


/**
 * @author Oscar Chamorro
 */
@Entity
@Data       // @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CANCION")
public class Cancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITULO", length = 150, nullable = false)
    private String titulo;

    @Column(name = "ARTISTA", length = 100, nullable = false)
    private String artista;

    @Column(name = "ALBUM", length = 100, nullable = false)
    private String album;

    @Column(name = "ANNO", nullable = false)
    private Integer anno;

    @Column(name = "GENERO", length = 50, nullable = true)
    private String genero;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LISTA_REPRODUCCION_ID")
    private ListaReproduccion listaReproduccion;

}
