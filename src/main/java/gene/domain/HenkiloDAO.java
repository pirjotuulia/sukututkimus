package gene.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class HenkiloDAO {
    private JdbcTemplate jdbcTemplate;

    public HenkiloDAO(@Autowired JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Henkilo> kaikkiHenkilot() {
        List<Henkilo> henkilot = jdbcTemplate.query(
                "SELECT * FROM henkilo",
                new RowMapper<Henkilo>() {
                    @Override
                    public Henkilo mapRow(ResultSet rs, int i) throws SQLException {
                        Henkilo henkilo = new Henkilo(
                                rs.getString("etunimi"),
                                rs.getString("sukunimi"),
                                rs.getDate("syntymaaika").toLocalDate());
                        henkilo.setId(rs.getInt("id"));
                        return henkilo;
                    }
                });
        return henkilot;
    }

    public List<Henkilo> haetutHenkilot(String hakusana) {
        List<Henkilo> henkilot = jdbcTemplate.query(
                "SELECT * FROM henkilo WHERE etunimi LIKE '%" + hakusana + "%' or sukunimi LIKE '%" + hakusana + "%'",
                new RowMapper<Henkilo>() {
                    @Override
                    public Henkilo mapRow(ResultSet rs, int i) throws SQLException {
                        Henkilo henkilo = new Henkilo(
                                rs.getString("etunimi"),
                                rs.getString("sukunimi"),
                                rs.getDate("syntymaaika").toLocalDate());
                        henkilo.setId(rs.getInt("id"));
                        return henkilo;
                    }
                });
        return henkilot;
    }

    public Henkilo lisaaHenkilo(Henkilo h) {
        if (haeHenkilo(h) == null) {
            int onnistui = jdbcTemplate.update("INSERT INTO henkilo(etunimi, sukunimi, syntymaaika) VALUES (?, ?, ?);", new Object[]{h.getEtunimi(), h.getSukunimi(), h.getSyntymaAika()});
            if (onnistui == 0) {
                return null;
            }
            h = (haeHenkilo(h).get(0));
            return h;
        }
        return null;
    }

    public Henkilo paivitaHenkilo(Henkilo paivitettava, Map<String, String> paivitettavat) {
        if (haeHenkilo(paivitettava) != null) {
            List<String> avaimet = new ArrayList<>();
            for (String kentta : paivitettavat.keySet()) {
                if (!paivitettavat.get(kentta).isEmpty()) {
                    avaimet.add(kentta);
                }
            }
            StringBuilder sql = new StringBuilder("UPDATE henkilo SET ");
            Object[] arvot = new Object[avaimet.size() + 1];
            int i = 0;
            for (String avain : avaimet) {
                sql.append(avain + "=?");
                arvot[i] = paivitettavat.get(avain);
                i++;
                if (i < avaimet.size()) {
                    sql.append(", ");
                } else {
                    sql.append(" ");
                }

            }
            sql.append("WHERE id=?;");
            arvot[i] = paivitettava.getId();
            int onnistui = jdbcTemplate.update(sql.toString(), arvot);
            if (onnistui > 0) {
                paivitettava = haeHenkiloIdlla(String.valueOf(paivitettava.getId()));
                return paivitettava;
            }
        }
        return null;
    }

    public List<Henkilo> haeHenkilo(Henkilo h) {
        List<Henkilo> henkilot = jdbcTemplate.query(
                "SELECT * FROM henkilo WHERE etunimi=? AND sukunimi=? AND syntymaaika=?", new Object[]{h.getEtunimi(), h.getSukunimi(), h.getSyntymaAika()},
                new RowMapper<Henkilo>() {
                    @Override
                    public Henkilo mapRow(ResultSet rs, int i) throws SQLException {
                        Henkilo henkilo = new Henkilo(
                                rs.getString("etunimi"),
                                rs.getString("sukunimi"),
                                rs.getDate("syntymaaika").toLocalDate());
                        henkilo.setId(rs.getInt("id"));
                        return henkilo;
                    }
                });
        if (henkilot.size() < 1) {
            return null;
        }
        return henkilot;
    }

    public Henkilo haeHenkiloIdlla(String id) {
        Henkilo h = (Henkilo) jdbcTemplate.queryForObject(
                "SELECT * FROM henkilo WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper(Henkilo.class));
        return h;
    }

    public void lisaaVanhemmatHenkiloina(Henkilo henkilo) {
        if (henkilo.getAiti() != 0) {
            henkilo.setAitiHenkilo(haeHenkiloIdlla(String.valueOf(henkilo.getAiti())));
        }
        if (henkilo.getIsa() != 0) {
            henkilo.setIsaHenkilo(haeHenkiloIdlla(String.valueOf(henkilo.getIsa())));
        }
    }
}
