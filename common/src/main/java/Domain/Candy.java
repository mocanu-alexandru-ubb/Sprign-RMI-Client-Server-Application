package Domain;
import java.io.Serializable;

public class Candy extends BaseEntity<Long> implements Serializable {
    private final Long candyID;
    private final String name;
    private final float price;

    public Candy(Long candyID, String name, float price) {
        super(candyID);
        this.candyID = candyID;
        this.name = name;
        this.price = price;
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
