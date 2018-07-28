package gene.domain;

import java.util.Objects;

public class Puumaja implements Comparable<Puumaja> {
    private int id;
    private int sukupolvi;
    private int pystylinja;
    private int sisarus;
    private int sukua;

    public Puumaja() {

    }

    public Puumaja(int id, int sukupolvi, int sukua, int sisarus, int pystylinja) {
        this.id = id;
        this.sukupolvi = sukupolvi;
        this.sukua = sukua;
        this.sisarus = sisarus;
        this.pystylinja = pystylinja;
    }

    public boolean onNollasolu() {
        return this.id != 0 && this.sukua == 0 && this.sisarus == 0 && this.pystylinja == 0;
    }

    @Override
    public int compareTo(Puumaja puumaja) {
        if (this.equals(puumaja)) {
            return 0;
        } else if (this.sukupolvi == puumaja.sukupolvi && this.sisarus == puumaja.sisarus && this.pystylinja == puumaja.pystylinja && this.sukua < puumaja.sukua) {
            return -1;
        } else if (this.sukupolvi < puumaja.sukupolvi || this.sisarus < puumaja.sisarus || this.pystylinja < puumaja.pystylinja) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        if (this.id == 0) {
            return "";
        }
        return this.id + ":" + this.sukupolvi + "," + this.sukua + "," + this.sisarus + "," + this.pystylinja;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Puumaja puumaja = (Puumaja) o;
        return sukupolvi == puumaja.sukupolvi &&
                pystylinja == puumaja.pystylinja &&
                sisarus == puumaja.sisarus &&
                sukua == puumaja.sukua;
    }

    @Override
    public int hashCode() {

        return Objects.hash(sukupolvi, pystylinja, sisarus, sukua);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSukupolvi() {

        return sukupolvi;
    }

    public void setSukupolvi(int sukupolvi) {
        this.sukupolvi = sukupolvi;
    }

    public int getPystylinja() {
        return pystylinja;
    }

    public void setPystylinja(int pystylinja) {
        this.pystylinja = pystylinja;
    }

    public int getSisarus() {
        return sisarus;
    }

    public void setSisarus(int sisarus) {
        this.sisarus = sisarus;
    }

    public int getSukua() {
        return sukua;
    }

    public void setSukua(int sukua) {
        this.sukua = sukua;
    }
}
