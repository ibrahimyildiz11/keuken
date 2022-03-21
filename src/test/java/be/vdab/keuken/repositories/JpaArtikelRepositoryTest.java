package be.vdab.keuken.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import be.vdab.keuken.domain.Artikel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;

@DataJpaTest(showSql = false)
@Import(JpaArtikelRepository.class)
@Sql("/insertArtikel.sql")
class JpaArtikelRepositoryTest
extends AbstractTransactionalJUnit4SpringContextTests {
    private final JpaArtikelRepository repository;

    JpaArtikelRepositoryTest(JpaArtikelRepository repository) {
        this.repository = repository;
    }

    private static final String ARTIKELS = "artikels";
    private Artikel artikel;

    private long idVanTestArtikel() {
        return jdbcTemplate.queryForObject(
                "select id from artikels where naam = 'test'", Long.class);
    }

    @BeforeEach
    void beforeEach() {
        artikel = new Artikel("test2", BigDecimal.ONE,BigDecimal.TEN);
    }

    @Test
    void create() {
        repository.create(artikel);
        assertThat(artikel.getId()).isPositive();
        assertThat(countRowsInTableWhere(ARTIKELS, "id = " + artikel.getId())).isOne();
    }

    @Test
    void findById() {
        assertThat(repository.findById(idVanTestArtikel()))
                .hasValueSatisfying(
                        artikel -> assertThat(artikel.getNaam()).isEqualTo("test"));
    }

    @Test
    void findByOnbestaandeId() {
        assertThat(repository.findById(-1)).isEmpty();
    }
}