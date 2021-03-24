package Domain;

import Domain.BaseEntity;
import Exceptions.BadInputException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Candy extends BaseEntity<Long> {
    private final Long candyID;
    private final String name;
    private final float price;

    public Candy(Long candyID, String name, float price) {
        super(candyID);
        this.candyID = candyID;
        this.name = name;
        this.price = price;
    }
    /**
     * Constructor
     * @param items, the List<String> from which the Candy will be created
     */
    public Candy(List<String> items){
        super(Long.parseLong(items.get(0)));
        this.candyID = Long.parseLong(items.get(0));
        this.name = items.get(1);
        this.price = Float.parseFloat(items.get(2));
    }

    /**
     * Constructor
     * @param element, the Element from which the Candy will be created
     */
    public Candy(Element element){
        super(Long.parseLong(element.getAttribute("candyID")));
        candyID = Long.parseLong(element.getAttribute("candyID"));
        name = element.getAttribute("name");
        price = Float.parseFloat(element.getAttribute("quantity"));
    }

    public Candy(ResultSet r) throws BadInputException {
        super(-1L);
        try {
            this.candyID = r.getLong("id");
            super.setId(this.candyID);
            this.name = r.getString("name");
            this.price = r.getFloat("price");
        }
        catch (SQLException e) {
            throw new BadInputException("bad sql querry");
        }
    }

    public Long getCandyID() {
        return candyID;
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    @Override
    public String writeAsCSV() {
        return candyID + "," + name + "," + price +"\n";
    }

    @Override
    public Node writeAsXML(Document document) {
        Element candyElement = document.createElement("Candy");
        candyElement.setAttribute("candyID", candyID.toString());

        Element childElementName = document.createElement("name");
        childElementName.setTextContent(name);
        candyElement.appendChild(childElementName);

        Element childElementPrice = document.createElement("price");
        childElementPrice.setTextContent(String.valueOf(price));
        candyElement.appendChild(childElementPrice);

        return candyElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Candy candy = (Candy) o;

        if (!candyID.equals(candy.candyID)) return false;
        return name.equals(candy.name);

    }
    @Override
    public int hashCode() {
        int result = candyID.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Candy{" +
                "candyId='" + candyID + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                "} " + super.toString();
    }
}
