package Domain;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.Serializable;

/**
 * @author -.
 */
public class BaseEntity<ID> implements Serializable {
    private ID id;

    public BaseEntity(ID id){
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public String writeAsCSV() {
        return null;
    }

    public Node writeAsXML(Document document) {
        return null;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }
}

