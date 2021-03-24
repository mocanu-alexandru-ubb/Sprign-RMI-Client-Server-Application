package Domain;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

public class Client extends BaseEntity<Long>{
    private Long clientID;
    private String name;

    public Client(Long clientID, String name) {
        super(clientID);
        this.clientID = clientID;
        this.name = name;
    }

    public Client(List<String> items){
        super(Long.parseLong(items.get(0)));
        this.clientID = Long.parseLong(items.get(0));
        this.name = items.get(1);
    }

    public Client(Element element){
        super(Long.parseLong(element.getAttribute("clientID")));
        clientID = Long.parseLong(element.getAttribute("clientID"));
        name = element.getAttribute("name");
    }

    public Long getClientID() {
        return clientID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String writeAsCSV() {
        return clientID + "," + name + "\n";
    }

    @Override
    public Node writeAsXML(Document document) {
        Element clientElement = document.createElement("Client");
        clientElement.setAttribute("clientID", clientID.toString());

        Element childElement = document.createElement("name");
        childElement.setTextContent(name);

        clientElement.appendChild(childElement);

        return clientElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client Client = (Client) o;

        if (!clientID.equals(Client.clientID)) return false;
        return name.equals(Client.name);

    }

    @Override
    public int hashCode() {
        int result = clientID.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientID='" + clientID + '\'' +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }
}
