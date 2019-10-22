package neu.edu.csye6225.assignment2.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "image")
public class ImageRepository {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @NotNull
    private String url;
    @NotNull
    @JSONField(serialize = false)
    private String region;
    @NotNull
    @JSONField(serialize = false)
    private String bucketName;
    @NotNull
    @JSONField(serialize = false)
    private String fileName;
    @NotNull
    @JSONField(serialize = false)
    private Date created_ts;
    @NotNull
    @JSONField(serialize = false)
    private String size;
    @NotNull
    @JSONField(serialize = false)
    private String md5;
    @NotNull
    @JSONField(serialize = false)
    private Date updated_ts;
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)//可选属性optional=false,表示recipe不能为空。删除步骤，不影响食谱
    @JoinColumn(name="recipe_id")//设置在orderedList表中的关联字段(外键)
    @JSONField(serialize = false)
    private RecipeRepository recipe;//所属recipe

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public ImageRepository(){ }
    public ImageRepository(RecipeRepository recipeRepository){
        this.recipe=recipeRepository;
    }
    public ImageRepository(String id,String url)
    {
        this.id=id;
        this.url=url;
    }
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public Date getCreated_ts() {
        return created_ts;
    }

    public void setCreated_ts(Date created_ts) {
        this.created_ts = created_ts;
    }

    public Date getUpdated_ts() {
        return updated_ts;
    }

    public void setUpdated_ts(Date updated_ts) {
        this.updated_ts = updated_ts;
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

    public RecipeRepository getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeRepository recipe) {
        this.recipe = recipe;
    }
}
