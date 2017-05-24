package pl.io4.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Basic;
import org.json.JSONObject;
import pl.io4.model.Cachable;

/**
 * Created by jacob on 25.04.2017.
 */
@Entity
@Table(name = "Category", schema = "dbo", catalog = "io4")
public final class Category extends Cachable {
    private int id;
    private String name;

    private static final int NAME_LENGTH = 50;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = true, length = NAME_LENGTH)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Category that = (Category) o;
        return id == that.id && (that.name == null
            || (name != null && name.equals(that.name)));
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    protected JSONObject cache() {
        JSONObject ret = new JSONObject();
        ret.put("id", this.id);
        ret.put("name", this.name);
        return ret;
    }

    @Override
    protected void load(JSONObject data) {
        id = data.getInt("id");
        name = data.getString("name");
    }
}