package Domain;

import Domain.BaseEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

public class Purchase extends BaseEntity<Long> {
    private Long purchaseID;
    private Long candyID;
    private Long clientID;
    private int quantity;

    public Purchase(Long purchaseID, Long clientID, Long candyID, Integer quantity) {
        super(purchaseID);
        this.purchaseID = purchaseID;
        this.candyID = candyID;
        this.clientID = clientID;
        this.quantity = quantity;
    }

    /**
     * Constructor
     * @param items, the List<String> from which the Purchase will be created
     */
    public Purchase(List<String> items){
        super(Long.parseLong(items.get(0)));
        this.purchaseID = Long.parseLong(items.get(0));
        this.candyID = Long.parseLong(items.get(1));
        this.clientID = Long.parseLong(items.get(2));
        this.quantity = (int) Long.parseLong(items.get(3));
    }

    /**
     * Constructor
     * @param element, the Element from which the Purchase will be created
     */
    public Purchase(Element element){
        super(Long.parseLong(element.getAttribute("purchaseID")));
        purchaseID = Long.parseLong(element.getAttribute("purchaseID"));
        candyID = Long.parseLong(element.getAttribute("candyID"));
        clientID = Long.parseLong(element.getAttribute("clientID"));
        quantity = (int) Long.parseLong(element.getAttribute("quantity"));

    }

    public Long getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(Long purchaseID) {
        this.purchaseID = purchaseID;
    }

    public Long getCandyID() {
        return candyID;
    }

    public void setCandyID(Long candyID) {
        this.candyID = candyID;
    }

    public Long getClientID() {
        return clientID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String writeAsCSV() {
        return purchaseID + "," + candyID + "," + clientID + "," + quantity + "\n";
    }

    @Override
    public Node writeAsXML(Document document) {
        Element purchaseElement = document.createElement("Purchase");
        purchaseElement.setAttribute("purchaseID", purchaseID.toString());

        Element childElementCandyID = document.createElement("candyID");
        childElementCandyID.setTextContent(candyID.toString());
        purchaseElement.appendChild(childElementCandyID);

        Element childElementClientID = document.createElement("clientID");
        childElementClientID.setTextContent(clientID.toString());
        purchaseElement.appendChild(childElementClientID);

        Element childElementQuantity = document.createElement("quantity");
        childElementQuantity.setTextContent(candyID.toString());
        purchaseElement.appendChild(childElementQuantity);

        return purchaseElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Purchase purc = (Purchase) o;

        if (!candyID.equals(purc.candyID)) return false;
        return purc.quantity == this.quantity;

    }
    @Override
    public int hashCode() {
        int result = candyID.hashCode();
        result = 31 * result + clientID.hashCode();
        result = 31 * result + quantity;
        return result;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "clientID='" + clientID + '\'' +
                ", candyID='" + candyID + '\'' +
                ", quantity='" + quantity + '\'' +
                "} " + super.toString();
    }
}
