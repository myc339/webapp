package neu.edu.csye6225.assignment2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "image")
public class ImageRepository {
    @Id
    @Column(name = "id", nullable = false)
    String id;
    @NotNull
    String url;
    public ImageRepository(){ }
    public ImageRepository(String id,String url)
    {
        this.id=id;
        this.url=url;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
