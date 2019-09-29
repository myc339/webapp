package neu.edu.csye6225.assignment2.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

public class OrderedListRepository {
    private Integer position;
    private String items;
    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

}
